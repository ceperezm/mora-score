package com.morascore.repository;

import com.morascore.entity.Evaluacion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    // @EntityGraph en todos los métodos que retornan listas o entidades mapeadas
    // para evitar N+1 queries: el mapper accede a cliente.id y usuario.idUsuario,
    // ambas relaciones LAZY, por lo que deben cargarse en el mismo JOIN.

    @EntityGraph(attributePaths = {"cliente", "usuario"})
    List<Evaluacion> findByUsuario_IdUsuario(Long id);

    @EntityGraph(attributePaths = {"cliente", "usuario"})
    List<Evaluacion> findByCliente_Id(Long id);

    @EntityGraph(attributePaths = {"cliente", "usuario"})
    List<Evaluacion> findByCategoriaRiesgo(String categoriaRiesgo);

    @EntityGraph(attributePaths = {"cliente", "usuario"})
    List<Evaluacion> findByProbabilidad(BigDecimal probabilidad);

    @EntityGraph(attributePaths = {"cliente", "usuario"})
    Optional<Evaluacion> findById(Long id);

    @Query("SELECT COUNT(DISTINCT e.cliente.id) FROM Evaluacion e WHERE UPPER(e.prediccion) = 'MORA'")
    long countClientesConMora();

    // Top 5 más recientes para dashboard — incluye ambas relaciones para evitar N+1
    @EntityGraph(attributePaths = {"cliente", "usuario"})
    List<Evaluacion> findTop5ByOrderByCreatedAtDesc();
}

