package com.madandev.creditscoring.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScoreHistoryResponse {

    private Long id;
    private Integer scoreValue;
    private String changeReason;
    private LocalDateTime createdAt;
}
