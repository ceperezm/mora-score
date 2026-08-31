package com.morascore.repository;

import com.morascore.entity.Cliente;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // @EntityGraph para evitar N+1: en ClienteMapper
    @EntityGraph(attributePaths = { "usuario" })
    List<Cliente> findByUsuario_IdUsuario(Long idUsuario);

    boolean existsByNumeroDocumento(String numeroDocumento);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = { "usuario" })
    Optional<Cliente> findByEmail(String email);

    @EntityGraph(attributePaths = { "usuario" })
    Optional<Cliente> findByNumeroDocumento(String numeroDocumento);

    @EntityGraph(attributePaths = { "usuario" })
    List<Cliente> findByActivo(boolean activo);

    long countByActivo(boolean activo);
}
