package com.madandev.creditscoring.domain.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record ClientDetailResponse(
        Long id,
        String rut,
        String firstName,
        String lastName,
        String email,
        String username,
        FinancialDataDto financialData,
        List<ScoreHistoryDto> scoreHistory,
        List<CreditRequestDto> creditRequests,
        Integer latestScore,
        String riskLevel
) {}
