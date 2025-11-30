package com.madandev.creditscoring.domain.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class AdminUserSummaryResponse {

    private Long id;
    private String username;
    private String email;
    private String role;

    private Instant createdAt;

    private Long totalScores;
    private Integer lastScore;
    private String lastRiskLevel; // MUY BAJO / BAJO / MEDIO / ALTO / SIN_DATOS
}
