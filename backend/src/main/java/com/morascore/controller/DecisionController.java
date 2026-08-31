package com.morascore.controller;

import com.morascore.dto.DecisionRequestDTO;
import com.morascore.dto.DecisionResponseDTO;

import com.morascore.service.DecisionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expone los endpoints HTTP públicos y privados para la gestión de decisiones.

 */

@RestController
@AllArgsConstructor
@RequestMapping("/api/decisiones")
public class DecisionController {

    private final DecisionService decisionService;


    @GetMapping("/mis-decisiones")
    public ResponseEntity<List<DecisionResponseDTO>> obtenerMisDecisiones(){
        List<DecisionResponseDTO> decisiones = decisionService.getMisDecisiones();
        return ResponseEntity.ok(decisiones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DecisionResponseDTO> obtenerPorId(@PathVariable Long id) {
        DecisionResponseDTO decision = decisionService.getDecisionporId(id);
        return ResponseEntity.ok(decision);
    }

    @GetMapping("/evaluacion/{idEvaluacion}")
    public ResponseEntity<DecisionResponseDTO> obtenerDecisionPorEvaluacion(@PathVariable Long idEvaluacion) { // obtiene
        DecisionResponseDTO decision = decisionService.getDecisionporEvaluacion(idEvaluacion);
        return ResponseEntity.ok(decision);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<DecisionResponseDTO>> obtenerDecisionesPorUsuario(@PathVariable Long idUsuario) { // obtiene las decisiones tomadas por un usuario
        List<DecisionResponseDTO> decisiones = decisionService.getDecicionesUsuario(idUsuario);
        return ResponseEntity.ok(decisiones);
    }

    @PostMapping("/evaluacion/{idEvaluacion}")
    public ResponseEntity<DecisionResponseDTO> crearDecision(
            @PathVariable Long idEvaluacion,
            @Valid @RequestBody DecisionRequestDTO decisionRequestDTO) {
        DecisionResponseDTO nuevaDecision = decisionService.crearDecision(idEvaluacion, decisionRequestDTO);

        return new ResponseEntity<>(nuevaDecision, HttpStatus.CREATED);
    }

    @PutMapping("/{idDecision}")
    public ResponseEntity<DecisionResponseDTO> editarDecision(@PathVariable Long idDecision,
            @Valid @RequestBody DecisionRequestDTO decisionRequestDTO) {
        DecisionResponseDTO decision = decisionService.editarDecision(idDecision, decisionRequestDTO);
        return ResponseEntity.ok(decision);
    }

}
