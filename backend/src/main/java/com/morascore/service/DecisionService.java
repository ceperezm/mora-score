package com.morascore.service;

import com.morascore.dto.DecisionRequestDTO;
import com.morascore.dto.DecisionResponseDTO;

import com.morascore.entity.Decision;
import com.morascore.entity.Usuario;
import com.morascore.mapper.DecisionMapper;
import com.morascore.repository.DecisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Componente de servicio que centraliza la lógica de negocio aplicable decisiones tomadas por un usuario.
 *
 */
@Service
@RequiredArgsConstructor // Genera el constructor
public class DecisionService {

    private final DecisionRepository decisionRepository;
    private final DecisionMapper decisionMapper;



    //Servicio para mis decisiones, las decisiones del usuario logueado
    @Transactional(readOnly = true)
    public List<DecisionResponseDTO> getMisDecisiones(){
        Usuario usuarioLogueado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Decision> evaluaciones = decisionRepository.findByUsuario_IdUsuario(usuarioLogueado.getIdUsuario());
        return  evaluaciones.stream().map(decisionMapper::entidadAResponse).toList();
    }

    @Transactional(readOnly = true)
    public DecisionResponseDTO getDecisionporId(Long id) {
        Decision decision = decisionRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Decision con el id " + id + " no existe"));
        return decisionMapper.entidadAResponse(decision);
    }

    @Transactional(readOnly = true)
    public DecisionResponseDTO getDecisionporEvaluacion(Long idEvaluacion) {
        Decision decision = decisionRepository.findByEvaluacion_IdEvaluacion(idEvaluacion).orElseThrow(
                () -> new NoSuchElementException("Decision con evaluacion no existe"));
        return decisionMapper.entidadAResponse(decision);
    }

    @Transactional(readOnly = true)
    public List<DecisionResponseDTO> getDecicionesUsuario(Long idUsuario) {
        List<Decision> decisiones = decisionRepository.findByUsuario_IdUsuario(idUsuario);
        if (decisiones.isEmpty()) {
            throw new NoSuchElementException("Usuario " + idUsuario + " no ha registrado decisiones.");
        }
        return decisiones.stream().map(decisionMapper::entidadAResponse).toList();
    }

    @Transactional
    public DecisionResponseDTO crearDecision(Long idEvaluacion, DecisionRequestDTO decisionRequestDTO) {

        Usuario usuarioLogueado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Decision decision = decisionMapper.requestAEntidad(decisionRequestDTO, idEvaluacion);
        decision.setUsuario(usuarioLogueado);

        Decision decisionGuardado = decisionRepository.save(decision);
        return decisionMapper.entidadAResponse(decisionGuardado);
    }

    @Transactional
    public DecisionResponseDTO editarDecision(Long idDecision, DecisionRequestDTO decisionRequestDTO) {
        Decision existente = decisionRepository.findById(idDecision).orElseThrow(
                () -> new NoSuchElementException("Decision no existe"));
        decisionMapper.actualizarEntidadDesdeDto(decisionRequestDTO, existente);

        Decision decisionGuardado = decisionRepository.save(existente);
        return decisionMapper.entidadAResponse(decisionGuardado);
    }

}
