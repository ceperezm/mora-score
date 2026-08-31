package com.morascore.auth.controller;

import com.morascore.auth.dto.LoginRequest;
import com.morascore.auth.dto.RegisterRequest;
import com.morascore.auth.dto.TokenResponse;
import com.morascore.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para los endpoints públicos de autenticación.
 * Las rutas /api/auth/** están configuradas como públicas en SecurityConfig.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/register
     * Registra un nuevo usuario y devuelve los tokens JWT.
     */
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    /**
     * POST /api/auth/login
     * Autentica las credenciales y devuelve los tokens JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
