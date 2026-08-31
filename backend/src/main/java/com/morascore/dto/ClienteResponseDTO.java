package com.morascore.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de salida con los datos de un cliente.
 * Es lo que la API retorna al consumidor tras consultar un cliente.
 */
public record ClienteResponseDTO(

        /** Id del cliente. */
        Long idCliente,

        /** ID del usuario analista que registró al cliente. */
        Long idUsuario,

        /** Tipo de documento de identidad (ej: DNI, RUC, CE). */
        String tipoDocumento,

        /** Número de documento único del cliente. */
        String numeroDocumento,

        /** Nombre(s) del cliente. */
        String nombre,

        /** Apellido(s) del cliente. */
        String apellido,

        /** Fecha de nacimiento del cliente. */
        LocalDate fechaNacimiento,

        /** Número de teléfono de contacto. */
        String telefono,

        /** Correo electrónico del cliente. */
        String email,

        /** Dirección de residencia del cliente. */
        String direccion,

        /** Indica si el cliente está activo en el sistema. */
        Boolean activo,

        /** Fecha y hora de creación del registro. */
        LocalDateTime createdAt,

        /** Fecha y hora de la última actualización del registro. */
        LocalDateTime updatedAt

) {}
