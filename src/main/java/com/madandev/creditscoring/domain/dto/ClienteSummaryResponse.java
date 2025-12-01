package com.madandev.creditscoring.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ClienteSummaryResponse {

    private Long id;

    private String username;
    private String firstName;
    private String lastName;
    private String rut;
    private String email;

    private Integer score;           // último score conocido
    private String riskLevel;        // BAJO / MEDIO / ALTO / MUY_ALTO

    private String financialStatus;  // COMPLETO / INCOMPLETO
    private Instant lastEvaluation;  // fecha última evaluación de score

    private String region;           // de financial_data
}
