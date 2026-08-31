package com.morascore.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO de entrada para el endpoint de login.
 * El email actúa como identificador único del usuario.
 */
public record LoginRequest(

        /** Email del usuario registrado. */
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        String email,

        /** Contraseña en texto plano para verificar contra el hash almacenado. */
        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {}
