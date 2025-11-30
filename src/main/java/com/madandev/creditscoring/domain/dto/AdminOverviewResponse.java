package com.madandev.creditscoring.domain.dto;

import lombok.Data;

@Data
public class AdminOverviewResponse {

    private long totalUsers;
    private long totalScores;
    private long totalCredits;

    private Double averageScore;          // promedio de score global
    private Double totalApprovedAmount;   // suma total de montos aprobados
    private Double approvalRate;          // % créditos aprobados
    private Double rejectionRate;         // % créditos rechazados
}
