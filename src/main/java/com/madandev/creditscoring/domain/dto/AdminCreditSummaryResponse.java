package com.madandev.creditscoring.domain.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class AdminCreditSummaryResponse {

    private Long id;
    private Long userId;
    private String username;

    private Double amountRequested;
    private Double approvedAmount;
    private Double interestRateAnnual;
    private Integer termMonths;
    private Double monthlyInstallment;

    private String status;      // APPROVED / REJECTED
    private String riskLevel;   // MUY BAJO / BAJO / MEDIO / ALTO

    private Instant requestDate;
    private Instant resolutionDate;
}
