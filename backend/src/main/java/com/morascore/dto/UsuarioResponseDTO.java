package com.morascore.dto;

import com.morascore.entity.Rol;

import java.time.LocalDateTime;

/**
 * DTO de salida con los datos de un usuario analista.
 * No expone la contraseña ni el hash. Es lo que la API retorna al consultar un usuario.
 */
public record UsuarioResponseDTO(

        /** ID único del usuario. */
        Long idUsuario,

        /** Nombre(s) del usuario. */
        String nombre,

        /** Apellido(s) del usuario. */
        String apellido,

        /** Correo electrónico del usuario. */
        String email,

        /** Rol asignado al usuario (ej: ADMIN, ANALISTA). */
        Rol rol,

        /** Indica si el usuario está activo en el sistema. */
        boolean activo,

        /** Fecha y hora de creación del registro. */
        LocalDateTime createdAt
) {}
