package com.morascore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de entrada para registrar la decisión de un analista sobre una evaluación.
 * Una decisión es la resolución manual que el analista toma tras revisar la evaluación de riesgo.
 */
public record DecisionRequestDTO(
        
        /** Decisión tomada (ej: APROBADO, RECHAZADO, EN_REVISION). */
        @NotBlank(message = "Decision es obligatorio")
        String decision,

        /** Comentario o justificación del analista sobre la decisión. */
        @NotBlank(message = "Comentario es obligatorio")
        String comentario

) {}
