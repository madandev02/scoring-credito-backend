package com.madandev.creditscoring.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardSummaryResponse {

    private int totalEvaluations;   // total registros
    private double approvalRate;    // porcentaje aprobados
    private double rejectRate;      // porcentaje rechazados
    private double avgScore;        // score promedio
}
