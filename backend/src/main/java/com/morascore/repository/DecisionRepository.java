package com.morascore.repository;

import com.morascore.entity.Decision;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DecisionRepository extends JpaRepository<Decision, Long> {

    // @EntityGraph evita N+1: el DecisionMapper accede a evaluacion.idEvaluacion y usuario.idUsuario
    @EntityGraph(attributePaths = {"evaluacion", "usuario"})
    Optional<Decision> findByEvaluacion_IdEvaluacion(Long id);

    @EntityGraph(attributePaths = {"evaluacion", "usuario"})
    List<Decision> findByUsuario_IdUsuario(Long id);

    long countByDecisionIgnoreCase(String decision);

    // Top 5 más recientes para dashboard — incluye evaluacion y usuario para evitar N+1
    @EntityGraph(attributePaths = {"evaluacion", "evaluacion.cliente", "usuario"})
    List<Decision> findTop5ByOrderByCreatedAtDesc();
}

