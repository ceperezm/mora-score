package com.morascore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO de entrada para registrar el resultado de una evaluación de riesgo.
 * Contiene el snapshot de features enviadas al modelo ML y la respuesta obtenida.
 */
public record EvaluacionRequestDTO(

        /** ID del cliente evaluado. */
        @NotNull
        Long idCliente,

        /**
         * Snapshot de los datos financieros enviados al modelo ML.
         * Se almacena como JSONB para preservar el estado exacto en el momento de la evaluación.
         */
        @NotNull(message = "Los datos de entrada son obligatorios")
        Map<String, Object> datosEntrada,

        /** Predicción devuelta por el modelo (ej: "mora", "no_mora"). */
        @NotBlank(message = "Prediccion es obligatoria")
        String prediccion,

        /** Probabilidad de mora calculada por el modelo (valor entre 0 y 1). */
        @NotNull(message = "Probabilidad es obligatoria")
        BigDecimal probabilidad,

        /** Categoría de riesgo asignada (ej: ALTO, MEDIO, BAJO). */
        @NotBlank(message = "Categoria riesgo es obligatoria")
        String categoriaRiesgo,

        /** Versión del modelo ML utilizado en la evaluación. */
        @NotBlank(message = "Version modelo es obligatorio")
        String versionModelo

) {}
