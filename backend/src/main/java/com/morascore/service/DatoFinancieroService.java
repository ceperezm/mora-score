package com.morascore.service;

import com.morascore.dto.DatoFinancieroRequestDTO;
import com.morascore.dto.DatoFinancieroResponseDTO;
import com.morascore.entity.Cliente;
import com.morascore.entity.DatoFinanciero;
import com.morascore.mapper.DatoFinancieroMapper;
import com.morascore.repository.ClienteRepository;
import com.morascore.repository.DatoFinancieroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Componente de servicio que centraliza la lógica de negocio aplicable a los datos fiancierso de un cliente.
 *
 */

@Service
@RequiredArgsConstructor // Genera el constructor
public class DatoFinancieroService {

    private final DatoFinancieroRepository datoFinancieroRepository;
    private final DatoFinancieroMapper datoFinancieroMapper;
    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public DatoFinancieroResponseDTO getPorCliente(Long idCliente) {
        DatoFinanciero datoFinanciero = datoFinancieroRepository.findByCliente_Id(idCliente).orElseThrow(
                () -> new NoSuchElementException("Datos financieros del cliente no encontrados"));
        return datoFinancieroMapper.entidadAResponse(datoFinanciero);
    }

    @Transactional(readOnly = true)
    public List<DatoFinancieroResponseDTO> getPorNiverAhorro(Integer nivelahorro) {
        List<DatoFinanciero> nivelAhorros = datoFinancieroRepository.findByNivelAhorro(nivelahorro);

        if (nivelAhorros.isEmpty()) {
            throw new NoSuchElementException("No hay datos para el nivelahorro " + nivelahorro);
        }
        return nivelAhorros.stream().map(datoFinancieroMapper::entidadAResponse).toList();
    }

    // crear dato financiero
    @Transactional
    public DatoFinancieroResponseDTO crearDatoFinanciero(Long idCliente, DatoFinancieroRequestDTO requestDTO) {
        Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(
                () -> new NoSuchElementException("Cliente no encontrado, no se puede agregar un dato financiero..."));
        DatoFinanciero datoFinanciero = datoFinancieroMapper.requestAEntidad(requestDTO);

        datoFinanciero.setCliente(cliente);
        DatoFinanciero datoGuardado = datoFinancieroRepository.save(datoFinanciero);
        return datoFinancieroMapper.entidadAResponse(datoGuardado);
    }

    @Transactional
    public DatoFinancieroResponseDTO editarDatoFinanciero(Long idCliente, Long idDatoFinanciero, DatoFinancieroRequestDTO requestDTO) {
        DatoFinanciero existente = datoFinancieroRepository.findById(idDatoFinanciero).orElseThrow(
                () -> new NoSuchElementException("Dato financiero no encontrado"));

        //  Verificar que el dato financiero pertenezca al cliente especificado
        // Compara el idCliente de la URL con el ID del cliente dueño del dato financiero
        if (!existente.getCliente().getId().equals(idCliente)) {
            throw new IllegalArgumentException("El dato financiero no corresponde al cliente proporcionado.");
        }
        datoFinancieroMapper.actualizarEntidadDesdeDto(requestDTO, existente);

        DatoFinanciero datoFinanciero = datoFinancieroRepository.save(existente);
        return datoFinancieroMapper.entidadAResponse(datoFinanciero);
    }

}
