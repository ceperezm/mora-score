package com.morascore.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO de salida para los endpoints de login y registro.
 * Contiene el access token y el refresh token generados.
 */
public record TokenResponse(

        /** Token JWT de acceso. Duración corta (24h por defecto). */
        @JsonProperty("access_token")
        String accessToken,

        /** Token JWT de refresco. Duración larga (7 días por defecto). */
        @JsonProperty("refresh_token")
        String refreshToken
) {}
