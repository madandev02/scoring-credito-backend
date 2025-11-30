package com.madandev.creditscoring.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "financial_data")
public class FinancialData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal ingresosLiquidos;
    private BigDecimal ingresosBrutos;
    private String tipoContrato;
    private Integer anosEstabilidad;
    private String tipoVivienda;
    private BigDecimal gastosFijos;
    private BigDecimal deudasTotales;
    private Integer numeroTarjetas;
    private Boolean historialAtrasos;
    private Boolean dicom;
    private String activos;
    private String region;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
