package com.morascore.auth.service;

import com.morascore.auth.dto.LoginRequest;
import com.morascore.auth.dto.RegisterRequest;
import com.morascore.auth.dto.TokenResponse;
import com.morascore.auth.token.Token;
import com.morascore.auth.token.TokenRepository;
import com.morascore.auth.token.TokenType;
import com.morascore.entity.Rol;
import com.morascore.entity.Usuario;
import com.morascore.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio de autenticación: maneja registro, login y gestión de tokens JWT.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registra un nuevo usuario, genera sus tokens y persiste el access token.
     */
    public TokenResponse register(RegisterRequest request) {
        var usuario = Usuario.builder()
                .nombre(request.nombre())
                .apellido(request.apellido())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .rol(Rol.ANALISTA)
                .activo(true)
                .build();

        var savedUsuario = usuarioRepository.save(usuario);
        var jwtToken = jwtService.generateToken(savedUsuario);
        var refreshToken = jwtService.generateRefreshToken(savedUsuario);

        saveUserToken(savedUsuario, jwtToken);

        return new TokenResponse(jwtToken, refreshToken);
    }

    /**
     * Autentica las credenciales, revoca tokens anteriores y emite nuevos tokens.
     */
    public TokenResponse login(LoginRequest request) {
        // Spring Security valida email + password contra la BD
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuario o contraseña incorrectos."));

        var jwtToken = jwtService.generateToken(usuario);
        var refreshToken = jwtService.generateRefreshToken(usuario);

        // Revocar tokens anteriores del usuario antes de emitir el nuevo
        revokeAllUserTokens(usuario);
        saveUserToken(usuario, jwtToken);

        return new TokenResponse(jwtToken, refreshToken);
    }

    // ─── Helpers privados ────────────────────────────────────────────────────

    private void saveUserToken(Usuario usuario, String jwtToken) {
        var token = Token.builder()
                .usuario(usuario)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expirado(false)
                .revocado(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Usuario usuario) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(usuario.getIdUsuario());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpirado(true);
            token.setRevocado(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}