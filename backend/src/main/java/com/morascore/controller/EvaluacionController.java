package com.morascore.controller;

import com.morascore.dto.EvaluacionResponseDTO;
import com.morascore.service.EvaluacionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;



/**
 * Controlador REST que expone los endpoints HTTP públicos y privados para la gestión de evaluaciones.

 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/evaluaciones")
public class EvaluacionController {
    private final EvaluacionService  evaluacionService;

    @GetMapping("/mis-evaluaciones")
    public ResponseEntity<List<EvaluacionResponseDTO>> obtenerMisEvaluaciones(){
        List<EvaluacionResponseDTO> evaluacion = evaluacionService.getMisEvaluaciones();
        return ResponseEntity.ok(evaluacion);
    }

    @GetMapping("/{idEvaluacion}")
    public ResponseEntity<EvaluacionResponseDTO> obtenerEvaluacionPorId(@PathVariable Long idEvaluacion){
        EvaluacionResponseDTO evaluacion = evaluacionService.getPorId(idEvaluacion);
        return ResponseEntity.ok(evaluacion);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<EvaluacionResponseDTO>> obtenerEvaluacionPorUsuario(@PathVariable Long idUsuario){ //obtiene las evaluaciones realizadas por un usuario
        List<EvaluacionResponseDTO> evaluaciones =evaluacionService.getPorUsuario(idUsuario);
        return ResponseEntity.ok(evaluaciones);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<EvaluacionResponseDTO>> obtenerEvaluacionesPorCliente(@PathVariable Long idCliente){ //obtiene las evaluaciones que se realizaron a un usuario
        List<EvaluacionResponseDTO> evaluaciones =evaluacionService.getPorCliente(idCliente);
        return ResponseEntity.ok(evaluaciones);
    }

    @GetMapping("/categoria-riesgo/{categoriaRiesgo}")
    public ResponseEntity<List<EvaluacionResponseDTO>> obtenerEvaluacionesPorCategoriaRiesgo(@PathVariable String categoriaRiesgo){
        List<EvaluacionResponseDTO> evaluaciones =evaluacionService.getCategoriaRiesgo(categoriaRiesgo);
        return ResponseEntity.ok(evaluaciones);
    }

    @GetMapping("/probabilidad/{probabilidad}")
    public ResponseEntity<List<EvaluacionResponseDTO>> obtenerEvaluacionesPorProbabilidad(@PathVariable BigDecimal probabilidad) {
        List<EvaluacionResponseDTO> evaluaciones = evaluacionService.getProbabilidadMora(probabilidad);
        return ResponseEntity.ok(evaluaciones);
    }

    @PostMapping("/cliente/{idCliente}")
    public ResponseEntity<EvaluacionResponseDTO> crearEvaluacion(
            @PathVariable Long idCliente) {
        EvaluacionResponseDTO evaluacion = evaluacionService.crearEvaluacion(idCliente);
        return ResponseEntity.status(201).body(evaluacion);
    }

}
