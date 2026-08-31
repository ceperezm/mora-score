package com.morascore.auth.token;

import com.morascore.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA que persiste los tokens JWT emitidos.
 * Permite revocar tokens individuales (blacklist) sin necesidad de estado en servidor.
 */
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Valor completo del token JWT. */
    @Column(name = "token", unique = true, nullable = false, length = 512)
    private String token;

    /** Tipo de token (siempre BEARER en este sistema). */
    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false)
    @Builder.Default
    private TokenType tokenType = TokenType.BEARER;

    /** Indica si el token fue revocado manualmente (logout, cambio de contraseña). */
    @Column(name = "revocado", nullable = false)
    @Builder.Default
    private boolean revocado = false;

    /** Indica si el token venció por expiración de tiempo. */
    @Column(name = "expirado", nullable = false)
    @Builder.Default
    private boolean expirado = false;

    /** Usuario propietario del token. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
