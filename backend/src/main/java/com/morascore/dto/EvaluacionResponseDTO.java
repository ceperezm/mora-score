package com.morascore.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO de salida con los datos de una evaluación de riesgo.
 * Es lo que la API retorna al consultar una evaluación registrada.
 */
public record EvaluacionResponseDTO(

        /** Id de la evaluación. */
        Long idEvaluacion,

        /** ID del cliente evaluado. */
        Long idCliente,

        /** ID del usuario analista que generó la evaluación. */
        Long idUsuario,

        /** Snapshot de los datos financieros usados como input del modelo ML. */
        Map<String, Object> datosEntrada,

        /** Predicción devuelta por el modelo (ej: "mora", "no_mora"). */
        String prediccion,

        /** Probabilidad de mora calculada por el modelo. */
        BigDecimal probabilidad,

        /** Categoría de riesgo asignada (ej: ALTO, MEDIO, BAJO). */
        String categoriaRiesgo,

        /** Versión del modelo ML utilizado. */
        String versionModelo,

        /** Fecha y hora en que se registró la evaluación. */
        LocalDateTime createdAt
) {}
