package com.madandev.creditscoring.domain.dto;

import lombok.Builder;
import java.time.Instant;

@Builder
public record ScoreHistoryDto(
        Long id,
        Integer scoreValue,
        String changeReason,
        Instant createdAt
) {}
