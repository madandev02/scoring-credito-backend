package com.madandev.creditscoring.domain.dto;

import lombok.Builder;
import java.time.Instant;

@Builder
public record CreditRequestDto(
        Long id,
        Double amountRequested,
        String status,
        Instant createdAt
) {}
