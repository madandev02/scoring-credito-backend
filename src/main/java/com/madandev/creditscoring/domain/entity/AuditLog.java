package com.madandev.creditscoring.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario dueño de la operación
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Analista responsable (cuando aplique, de momento puede ser null)
    @ManyToOne
    @JoinColumn(name = "analyst_id")
    private User analyst;

    // LOGIN, SCORE_EVALUATION, CREDIT_DECISION, ADMIN_ACCESS, etc.
    @Column(name = "action_type", length = 100, nullable = false)
    private String actionType;

    // Endpoint o contexto lógico
    @Column(name = "endpoint", length = 255)
    private String endpoint;

    @Column(name = "score_before")
    private Integer scoreBefore;

    @Column(name = "score_after")
    private Integer scoreAfter;

    @Column(name = "score_reasoning", length = 1000)
    private String scoreReasoning;

    @Column(name = "credit_decision_reason", length = 1000)
    private String creditDecisionReason;

    // true si la acción implica acceso/uso de score
    @Column(name = "access_log_score")
    private Boolean accessLogScore;

    // SUCCESS / ERROR / REJECTED etc.
    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
