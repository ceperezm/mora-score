package com.morascore.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Representacion del dato financiero de un cliente dentro del sistema de scoring
 * Mapea la tabla datos financieros de la base de datos
 *
 * @Author Camilo
 * @Version 1
 * */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "datos_financieros")
public class DatoFinanciero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private Long idDatoFinanciero;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "atraso", nullable = false)
    private Integer atraso;

    @Column(name = "vivienda", nullable = false,length = 30)
    private String vivienda;

    @Column(name = "edad", nullable = false)
    private Integer edad;

    @Column(name="dias_lab", nullable = false)
    private Integer diasLab;

    @Column(name= "exp_sf")
    private BigDecimal expSf;

    @Column(name = "nivel_ahorro", nullable = false)
    private Integer nivelAhorro;

    @Column(name = "ingreso", nullable = false)
    private BigDecimal ingreso;

    @Column(name = "linea_sf")
    private BigDecimal lineaSf;

    @Column(name = "deuda_sf")
    private BigDecimal deudaSf;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "zona",nullable = false)
    private String zona;

    @Column(name = "clasif_sbs",nullable = false)
    private Integer clasifSbs;

    @Column(name = "nivel_educ",nullable = false)
    private String nivelEduc;


    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    public Map<String, Object> toFeatures() {
        Map<String, Object> f = new LinkedHashMap<>();
        f.put("atraso",       this.atraso);
        f.put("edad",         this.edad);
        f.put("dias_lab",     this.diasLab);
        f.put("exp_sf",       this.expSf   != null ? this.expSf.doubleValue()   : null);
        f.put("ingreso",      this.ingreso != null ? this.ingreso.doubleValue()  : null);
        f.put("linea_sf",     this.lineaSf != null ? this.lineaSf.doubleValue()  : null);
        f.put("deuda_sf",     this.deudaSf != null ? this.deudaSf.doubleValue()  : null);
        f.put("score",        this.score);
        f.put("nivel_ahorro", this.nivelAhorro);
        f.put("nivel_educ",   this.nivelEduc != null ? this.nivelEduc.toUpperCase() : null);
        f.put("vivienda",     this.vivienda  != null ? this.vivienda.toUpperCase()  : null);
        f.put("zona",         this.zona); // <-- sin toUpperCase(), el modelo espera "Lima", "La Libertad", etc.
        f.put("clasif_sbs",   this.clasifSbs);
        return f;
    }

}