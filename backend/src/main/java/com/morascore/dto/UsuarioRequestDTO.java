package com.morascore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para registrar un nuevo usuario analista.
 * El rol y estado activo se asignan automáticamente por defecto al crear el usuario.
 */
public record UsuarioRequestDTO(

        /** Nombre(s) del usuario. */
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        /** Apellido(s) del usuario. */
        @NotBlank(message = "El apellido es obligatorio")
        String apellido,

        /** Correo electrónico único del usuario, usado como identificador de acceso. */
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        String email,

        /** Contraseña en texto plano. Se almacena hasheada en la base de datos. */
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password

) {}
