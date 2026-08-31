package com.morascore.controller;

import com.morascore.dto.DatoFinancieroRequestDTO;
import com.morascore.dto.DatoFinancieroResponseDTO;
import com.morascore.service.DatoFinancieroService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expone los endpoints HTTP públicos y privados para la gestión de datos financieros.

 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/datos-financieros")
public class DatoFinancieroController {

    private final DatoFinancieroService datoFinancieroService;

    @GetMapping("/{idCliente}")
    public ResponseEntity<DatoFinancieroResponseDTO> obtenerDatoFinancieroPorCliente(@PathVariable Long idCliente) {
        DatoFinancieroResponseDTO datofianciero = datoFinancieroService.getPorCliente(idCliente);
        return ResponseEntity.ok(datofianciero);
    }

    @GetMapping("/nivel-ahorro/{nivelAhorro}")
    public ResponseEntity<List<DatoFinancieroResponseDTO>> obtenerDatosFinancierosPorNivelAhorro(
            @PathVariable Integer nivelAhorro) {
        List<DatoFinancieroResponseDTO> datosFinancieros = datoFinancieroService.getPorNiverAhorro(nivelAhorro);
        return ResponseEntity.ok(datosFinancieros);
    }

    @PostMapping("/cliente/{idCliente}")
    public ResponseEntity<DatoFinancieroResponseDTO> crearDatoFinanciero(@PathVariable Long idCliente,
            @Valid @RequestBody DatoFinancieroRequestDTO datoFinanciero) {
        DatoFinancieroResponseDTO nuevoDato = datoFinancieroService.crearDatoFinanciero(idCliente, datoFinanciero);
        return new ResponseEntity<>(nuevoDato, HttpStatus.CREATED);
    }

    @PutMapping("/cliente/{idCliente}/dato-financiero/{idDato}")
    public ResponseEntity<DatoFinancieroResponseDTO> editarDatoFinanciero(@PathVariable Long idCliente,@PathVariable Long idDato,
            @RequestBody DatoFinancieroRequestDTO datoFinanciero) {
        DatoFinancieroResponseDTO datoActualizado = datoFinancieroService.editarDatoFinanciero(idCliente,idDato, datoFinanciero);
        return ResponseEntity.ok(datoActualizado);
    }

}
