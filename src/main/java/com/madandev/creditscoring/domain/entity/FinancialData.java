package com.madandev.creditscoring.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "financial_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación 1:1 con User
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // RUT asociado a esta ficha financiera
    @Column(length = 20)
    private String rut;

    // ==============================
    // INGRESOS
    // ==============================

    @Column(name = "ingresos_liquidos", precision = 15, scale = 2, nullable = false)
    private BigDecimal ingresosLiquidos;

    @Column(name = "ingresos_brutos", precision = 15, scale = 2, nullable = false)
    private BigDecimal ingresosBrutos;

    // Tipo contrato: INDEFINIDO / PLAZO FIJO / HONORARIOS, etc.
    @Column(name = "tipo_contrato", length = 50, nullable = false)
    private String tipoContrato;

    // Años de estabilidad laboral
    @Column(name = "anos_estabilidad")
    private Integer anosEstabilidad;

    // ==============================
    // VIVIENDA
    // ==============================

    @Column(name = "tipo_vivienda", length = 50, nullable = false)
    private String tipoVivienda;

    // ==============================
    // GASTOS / DEUDAS
    // ==============================

    @Column(name = "gastos_fijos", precision = 15, scale = 2, nullable = false)
    private BigDecimal gastosFijos;

    @Column(name = "deudas_totales", precision = 15, scale = 2, nullable = false)
    private BigDecimal deudasTotales;

    @Column(name = "numero_tarjetas")
    private Integer numeroTarjetas;

    @Column(name = "historial_atrasos")
    private Boolean historialAtrasos;

    @Column(name = "dicom")
    private Boolean dicom;

    // ==============================
    // ACTIVOS / UBICACIÓN
    // ==============================

    @Column(name = "activos", length = 200)
    private String activos;

    @Column(name = "region", length = 50)
    private String region;

    // ==============================
    // AUDITORÍA
    // ==============================

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
