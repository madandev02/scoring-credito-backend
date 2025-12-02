package com.madandev.creditscoring.domain.dto;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record CreditRequestDto(
        Long id,
        Double amountRequested,
        String status,
        LocalDateTime createdAt
) {}
