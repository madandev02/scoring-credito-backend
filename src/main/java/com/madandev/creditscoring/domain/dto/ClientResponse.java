package com.madandev.creditscoring.domain.dto;

public record ClientResponse(
        Long id,
        String rut,
        String firstName,
        String lastName,
        String email,
        String region,
        Integer lastScore,
        String lastRiskLevel,
        String financialStatus
) {}
