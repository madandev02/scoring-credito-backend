package com.madandev.creditscoring.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "credit_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario que pide el crédito
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Monto solicitado por el usuario (en CLP)
    @Column(name = "amount_requested", nullable = false)
    private Double amountRequested;

    // Monto finalmente aprobado (en CLP)
    @Column(name = "approved_amount")
    private Double approvedAmount;

    // Tasa de interés anual (%)
    @Column(name = "interest_rate_annual")
    private Double interestRateAnnual;

    // Plazo del crédito en meses
    @Column(name = "term_months")
    private Integer termMonths;

    // Cuota mensual (amortización francesa)
    @Column(name = "monthly_installment")
    private Double monthlyInstallment;

    // Nivel de riesgo en el momento de la decisión (BAJO/MEDIO/ALTO/etc)
    @Column(name = "risk_level", length = 50)
    private String riskLevel;

    // Motivo de la decisión (para auditoría / explicación)
    @Column(name = "decision_reason", length = 500)
    private String decisionReason;

    // PENDING / APPROVED / REJECTED
    @Column(name = "status", length = 50)
    private String status;

    // Fecha en que se realizó la solicitud
    @Column(name = "request_date", nullable = false)
    private Instant requestDate;

    // Fecha en que se tomó la decisión (aprobado/rechazado)
    @Column(name = "resolution_date")
    private Instant resolutionDate;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.requestDate = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
