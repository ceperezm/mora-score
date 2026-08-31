package com.morascore.auth.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la gestión de tokens JWT persistidos.
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    /**
     * Devuelve todos los tokens válidos (no revocados ni expirados) de un usuario.
     * Se usa para revocarlos en masa antes de emitir un nuevo token al hacer login.
     */
    @Query("""
            SELECT t FROM Token t
            WHERE t.usuario.idUsuario = :userId
              AND t.expirado = false
              AND t.revocado = false
            """)
    List<Token> findAllValidTokensByUser(Long userId);

    /**
     * Busca un token por su valor JWT. Usado en el filtro para verificar
     * si el token fue revocado externamente.
     */
    Optional<Token> findByToken(String token);
}
