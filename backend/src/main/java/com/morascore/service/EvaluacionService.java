package com.morascore.service;

import com.morascore.dto.EvaluacionResponseDTO;
import com.morascore.entity.Cliente;
import com.morascore.entity.DatoFinanciero;
import com.morascore.entity.Evaluacion;
import com.morascore.entity.Usuario;
import com.morascore.mapper.EvaluacionMapper;
import com.morascore.repository.ClienteRepository;
import com.morascore.repository.DatoFinancieroRepository;
import com.morascore.repository.EvaluacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Componente de servicio que centraliza la lógica de negocio aplicable a las
 * evaluaciones realizadas por un usuario.
 *
 */

@Service
@RequiredArgsConstructor
public class EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;
    private final EvaluacionMapper evaluacionMapper;
    private final ClienteRepository clienteRepository;
    private final DatoFinancieroRepository datoFinancieroRepository;
    private final RestTemplate restTemplate;

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    @Transactional(readOnly = true)
    public EvaluacionResponseDTO getPorId(Long id) {
        Evaluacion evaluacion = evaluacionRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Evaluación con el id " + id + " no existe"));
        return evaluacionMapper.entidadAResponse(evaluacion);
    }

    @Transactional(readOnly = true)
    public List<EvaluacionResponseDTO> getPorUsuario(Long idUsuario) {
        List<Evaluacion> evaluaciones = evaluacionRepository.findByUsuario_IdUsuario(idUsuario);
        if (evaluaciones.isEmpty()) {
            throw new NoSuchElementException("El usuario con ID " + idUsuario + " no ha registrado evaluaciones.");
        }
        return evaluaciones.stream().map(evaluacionMapper::entidadAResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<EvaluacionResponseDTO> getPorCliente(Long idCliente) {
        List<Evaluacion> evaluaciones = evaluacionRepository.findByCliente_Id(idCliente);
        if (evaluaciones.isEmpty()) {
            throw new NoSuchElementException("El cliente no tiene evaluaciones.");
        }
        return evaluaciones.stream().map(evaluacionMapper::entidadAResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<EvaluacionResponseDTO> getCategoriaRiesgo(String categoriaRiesgo) {
        List<Evaluacion> evaluaciones = evaluacionRepository.findByCategoriaRiesgo(categoriaRiesgo);
        if (evaluaciones.isEmpty()) {
            throw new NoSuchElementException("No hay evaluaciones con riesgo " + categoriaRiesgo);
        }
        return evaluaciones.stream().map(evaluacionMapper::entidadAResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<EvaluacionResponseDTO> getProbabilidadMora(BigDecimal probabilidad) {
        List<Evaluacion> evaluaciones = evaluacionRepository.findByProbabilidad(probabilidad);
        if (evaluaciones.isEmpty()) {
            throw new NoSuchElementException("No hay evaluaciones con probabilidad " + probabilidad);
        }
        return evaluaciones.stream().map(evaluacionMapper::entidadAResponse).toList();
    }

    // Servicio para mis evaluaciones, las evaluaciones del usuario logueado
    @Transactional(readOnly = true)
    public List<EvaluacionResponseDTO> getMisEvaluaciones() {
        Usuario usuarioLogueado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Evaluacion> evaluaciones = evaluacionRepository.findByUsuario_IdUsuario(usuarioLogueado.getIdUsuario());
        return evaluaciones.stream().map(evaluacionMapper::entidadAResponse).toList();
    }

    @Transactional
    public EvaluacionResponseDTO crearEvaluacion(Long idCliente) {
        // Validar existencia del cliente y usuario
        Usuario usuarioLogueado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(
                () -> new NoSuchElementException("Cliente con id " + idCliente + " no encontrado"));

        // Obtener los datos financieros del cliente para enviar al modelo ML
        DatoFinanciero datoFinanciero = datoFinancieroRepository.findByCliente_Id(idCliente)
                .orElseThrow(() -> new NoSuchElementException(
                        "El cliente con id " + idCliente + " no tiene datos financieros registrados"));

        // Construir el map de features y llamar al servicio FastAPI
        Map<String, Object> features = datoFinanciero.toFeatures();
        System.out.println("Features enviadas a FastAPI: " + features); // <-- LOG 1

        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(features, headers);

        // ---- BLOQUE NUEVO ----
        Map<String, Object> resultado;
        try {
            resultado = restTemplate.exchange(
                    mlServiceUrl,
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    }).getBody();
        } catch (HttpClientErrorException ex) {
            System.err.println("Error FastAPI: " + ex.getResponseBodyAsString()); // <-- LOG 2
            throw new IllegalStateException("Error al llamar al modelo ML: " + ex.getResponseBodyAsString(), ex);
        }
        // ---- FIN BLOQUE NUEVO ----

        if (resultado == null) {
            throw new IllegalStateException("El modelo ML no retornó respuesta");
        }

        // Extraer los resultados devueltos por FastAPI
        int mora = (Integer) resultado.get("mora");
        double proba = ((Number) resultado.get("probabilidad")).doubleValue();
        String version = (String) resultado.get("version_modelo");

        // Construir y persistir la evaluación
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setCliente(cliente);
        evaluacion.setUsuario(usuarioLogueado);
        evaluacion.setDatosEntrada(features);
        evaluacion.setPrediccion(mora == 1 ? "MORA" : "AL_DIA");
        evaluacion.setProbabilidad(BigDecimal.valueOf(proba));
        evaluacion.setCategoriaRiesgo(calcularCategoria(proba));
        evaluacion.setVersionModelo(version);

        return evaluacionMapper.entidadAResponse(evaluacionRepository.save(evaluacion));
    }

    /**
     * Asigna categoría de riesgo según la probabilidad de mora devuelta por el
     * modelo.
     */
    private String calcularCategoria(double proba) {
        if (proba >= 0.75)
            return "ALTO";
        if (proba >= 0.45)
            return "MEDIO";
        return "BAJO";
    }

}
