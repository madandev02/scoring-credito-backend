package com.madandev.creditscoring.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScoreSummaryResponse {

    private Integer lastScore;
    private String riskLevel;
    private LocalDateTime lastUpdated;
    private Long totalRecords;
    private Double averageScore;
}
