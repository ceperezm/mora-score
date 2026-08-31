package com.morascore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Representacion del Usuario dentro del sistema de scoring
 * Mapea la tabla usarios de la base de datos
 *
 * @Author Camilo
 * @Version 1
 * */


@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idUsuario;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    @Builder.Default
    private Rol rol = Rol.ANALISTA;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private boolean activo = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ─── UserDetails ──────────────────────────────────────────────────────────

    /** El email es el identificador de usuario para Spring Security. */
    @Override
    public String getUsername() {
        return email;
    }

    /** Retorna el hash de la contraseña almacenado. */
    @Override
    public String getPassword() {
        return passwordHash;
    }

    /** Mapea el Rol JPA a una autoridad de Spring Security. */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** Delega en el campo `activo` de la BD. */
    @Override
    public boolean isEnabled() {
        return activo;
    }
}
