package com.madandev.creditscoring.domain.dto;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record ScoreHistoryDto(
        Long id,
        Integer scoreValue,
        String changeReason,
        LocalDateTime createdAt
) {}
