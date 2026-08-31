package com.morascore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO de entrada para registrar o actualizar los datos financieros de un cliente.
 * Estos datos son las features que se envían al modelo ML para generar una evaluación.
 */
public record DatoFinancieroRequestDTO(


        /** Días de atraso en pagos reportados. */
        @NotNull(message = "Atraso es obligatorio")
        Integer atraso,

        /** Tipo de vivienda del cliente (ej: propia, alquilada). */
        @NotBlank(message = "Vivienda es obligatorio")
        String vivienda,

        /** Edad del cliente en años. */
        @NotNull(message = "Edad es obligatorio")
        Integer edad,

        /** Días laborados en el empleo actual. */
        @NotNull(message = "Dias laborados es obligatorio")
        Integer diasLab,

        /** Experiencia en el sistema financiero en años (opcional). */
        BigDecimal expSf,

        /** Nivel de ahorro del cliente (escala numérica). */
        @NotNull(message = "Nivel ahorro es obligatorio")
        Integer nivelAhorro,

        /** Ingreso mensual del cliente. */
        @NotNull(message = "Ingreso es obligatorio")
        BigDecimal ingreso,

        /** Línea de crédito en el sistema financiero (opcional). */
        BigDecimal lineaSf,

        /** Deuda total en el sistema financiero (opcional). */
        BigDecimal deudaSf,

        /** Score crediticio del cliente. */
        @NotNull(message = "Score es obligatorio")
        Integer score,

        /** Zona geográfica del cliente. */
        @NotBlank(message = "Zona es obligatorio")
        String zona,

        /** Clasificación SBS del cliente (opcional). */
        Integer clasifSbs,

        /** Nivel de educación del cliente (ej: primaria, secundaria, superior). */
        @NotBlank(message = "Nivel educacion es obligatorio")
        String nivelEduc
) {}
