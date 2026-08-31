package com.morascore.dto;

import java.time.LocalDateTime;

/**
 * DTO de salida con los datos de una decisión registrada.
 * Es lo que la API retorna al consultar la decisión de un analista sobre una evaluación.
 */
public record DecisionResponseDTO(

        /** ID único de la decisión. */
        Long idDecision,

        /** ID de la evaluación sobre la que se tomó la decisión. */
        Long idEvaluacion,

        /** ID del usuario analista que tomó la decisión. */
        Long idUsuario,

        /** Decisión tomada (ej: APROBADO, RECHAZADO, EN_REVISION). */
        String decision,

        /** Comentario o justificación del analista. */
        String comentario,

        /** Fecha y hora en que se registró la decisión. */
        LocalDateTime createdAt
) {}
