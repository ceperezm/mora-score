package com.morascore.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para el endpoint de registro de nuevos usuarios.
 * El rol se asigna como ANALISTA por defecto.
 */
public record RegisterRequest(

        /** Nombre(s) del usuario. */
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        /** Apellido(s) del usuario. */
        @NotBlank(message = "El apellido es obligatorio")
        String apellido,

        /** Email único del usuario, usado como identificador de acceso. */
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        String email,

        /** Contraseña en texto plano. Se hasheará con BCrypt antes de persistir. */
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password
) {}
