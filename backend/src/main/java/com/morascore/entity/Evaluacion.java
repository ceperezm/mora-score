package com.morascore.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Representacion de la evaluacion de un cliente y sus datos financieros dentro del sistema de scoring
 * Mapea la tabla evaluaciones de la base de datos
 *
 * @Author Camilo
 * @Version 1
 * */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "evaluaciones")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long idEvaluacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Snapshot de los datos enviados a FastAPI al momento de la evaluación.
    // Se guarda como JSONB para preservar el estado exacto de las features
    // sin que cambios futuros en los datos del cliente afecten el historial
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "datos_entrada", nullable = false, columnDefinition = "jsonb")
    private Map<String,Object> datosEntrada;

    @Column(name = "prediccion", nullable = false, length = 20)
    private String prediccion;

    @Column(name = "probabilidad", nullable = false)
    private BigDecimal probabilidad;

    @Column(name = "categoria_riesgo", nullable = false, length = 20)
    private String categoriaRiesgo;

    @Column(name = "version_modelo",length = 30)
    private String versionModelo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
