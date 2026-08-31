package com.morascore.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Servicio encargado de la generación, validación y extracción de información
 * de los tokens JWT (JSON Web Tokens).
 */
@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    /**
     * Extrae el nombre de usuario (en nuestro caso, el email) desde el token JWT.
     * @param token El token JWT
     * @return El email del usuario contenido en el token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae un claim (propiedad) específico del token JWT usando una función de resolución.
     * @param token El token JWT
     * @param claimsResolver Función que define qué claim extraer
     * @return El valor del claim extraído
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Genera un token de acceso (Access Token) sin claims adicionales para el usuario proporcionado.
     * @param userDetails Detalles del usuario autenticado
     * @return Token JWT firmado
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token de acceso incluyendo claims (propiedades) adicionales.
     * @param extraClaims Mapa con propiedades extras a incluir en el token
     * @param userDetails Detalles del usuario autenticado
     * @return Token JWT firmado
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Genera un token de refresco (Refresh Token) con un tiempo de expiración mayor.
     * @param userDetails Detalles del usuario autenticado
     * @return Token JWT de refresco firmado
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    /**
     * Método interno para construir y firmar el token JWT.
     * @param extraClaims Claims adicionales
     * @param userDetails Detalles del usuario
     * @param expiration Tiempo de expiración en milisegundos
     * @return El token en formato String
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Verifica si un token es válido: el nombre de usuario coincide y el token no ha expirado.
     * @param token El token JWT a verificar
     * @param userDetails Los detalles del usuario obtenidos de la BD
     * @return true si el token es válido, false en caso contrario
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Verifica si la fecha actual es posterior a la fecha de expiración del token.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración configurada dentro del token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Desencripta el token y extrae todos sus claims (payload).
     * Si la firma es inválida o el token ha sido alterado, este método lanzará una excepción.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Genera la clave criptográfica en formato SecretKey a partir del valor en base64 de properties.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}