package com.morascore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO de entrada para crear o actualizar un cliente.
 * Contiene los datos que el cliente envía a la API.
 */
public record ClienteRequestDTO(

        /** Tipo de documento de identidad (ej: DNI, RUC, CE). */
        @NotBlank(message = "El tipo de documento es obligatorio")
        String tipoDocumento,

        /** Número de documento único del cliente. */
        @NotBlank(message = "El número de documento es obligatorio")
        String numeroDocumento,

        /** Nombre(s) del cliente. */
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        /** Apellido(s) del cliente. */
        @NotBlank(message = "El apellido es obligatorio")
        String apellido,

        /** Fecha de nacimiento del cliente. */
        @NotNull(message = "La fecha de nacimiento es obligatoria")
        LocalDate fechaNacimiento,

        /** Número de teléfono de contacto. */
        @NotBlank(message = "El teléfono es obligatorio")
        String telefono,

        /** Correo electrónico único del cliente. */
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        String email,

        /** Dirección de residencia del cliente. */
        @NotBlank(message = "La dirección es obligatoria")
        String direccion,

        /** Indica si el cliente está activo en el sistema. */
        @NotNull(message = "El estado activo es obligatorio")
        Boolean activo
) {}
