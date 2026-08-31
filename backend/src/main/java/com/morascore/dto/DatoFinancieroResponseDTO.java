package com.morascore.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de salida con los datos financieros de un cliente.
 * Es lo que la API retorna al consultar los datos financieros registrados.
 */
public record DatoFinancieroResponseDTO(

        /** ID único del registro de datos financieros. */
        Long idDatoFinanciero,

        /** ID del cliente al que pertenecen estos datos. */
        Long idCliente,

        /** Días de atraso en pagos reportados. */
        Integer atraso,

        /** Tipo de vivienda del cliente. */
        String vivienda,

        /** Edad del cliente en años. */
        Integer edad,

        /** Días laborados en el empleo actual. */
        Integer diasLab,

        /** Experiencia en el sistema financiero en años. */
        BigDecimal expSf,

        /** Nivel de ahorro del cliente. */
        Integer nivelAhorro,

        /** Ingreso mensual del cliente. */
        BigDecimal ingreso,

        /** Línea de crédito en el sistema financiero. */
        BigDecimal lineaSf,

        /** Deuda total en el sistema financiero. */
        BigDecimal deudaSf,

        /** Score crediticio del cliente. */
        Integer score,

        /** Zona geográfica del cliente. */
        String zona,

        /** Clasificación SBS del cliente. */
        Integer clasifSbs,

        /** Nivel de educación del cliente. */
        String nivelEduc,

        /** Fecha y hora de la última actualización del registro. */
        LocalDateTime updatedAt
) {}
