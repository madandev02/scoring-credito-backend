package com.madandev.creditscoring.domain.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
public class ScoreHistoryResponse {

    private Long id;
    private Integer scoreValue;
    private String changeReason;
    private Instant createdAt;

}
