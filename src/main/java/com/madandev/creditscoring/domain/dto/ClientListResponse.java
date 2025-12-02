package com.madandev.creditscoring.domain.dto;

import lombok.Builder;

@Builder
public record ClientListResponse(
        Long id,
        String rut,
        String firstName,
        String lastName,
        String username,
        String role,
        Integer latestScore,
        String riskLevel,
        String status
) {}
