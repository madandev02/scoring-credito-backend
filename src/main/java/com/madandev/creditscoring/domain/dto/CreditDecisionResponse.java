package com.madandev.creditscoring.domain.dto;

import lombok.Data;
import java.time.Instant;

/**
 * Respuesta final cuando el usuario solicita un crédito.
 */
@Data
public class CreditDecisionResponse {

    private Long id;
    private Double amountRequested;
    private Double approvedAmount;
    private Double interestRateAnnual;
    private Integer termMonths;
    private Double monthlyInstallment;

    private String status;          // APPROVED / REJECTED
    private String riskLevel;       // ALTO / MEDIO / BAJO
    private String decisionReason;  // texto explicativo

    private Integer scoreAtDecision;
    private Instant requestDate;
    private Instant resolutionDate;
}
