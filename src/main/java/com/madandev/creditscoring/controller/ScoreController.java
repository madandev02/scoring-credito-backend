package com.madandev.creditscoring.controller;

import com.madandev.creditscoring.domain.dto.FinancialDataRequest;
import com.madandev.creditscoring.domain.dto.RiskResult;
import com.madandev.creditscoring.domain.entity.User;
import com.madandev.creditscoring.domain.service.AuditLogService;
import com.madandev.creditscoring.domain.service.FinancialDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/score")
@RequiredArgsConstructor
@Tag(name = "Scoring", description = "Cálculo de score crediticio")
public class ScoreController {

    private final FinancialDataService financialDataService;
    private final AuditLogService auditLogService;

    @PostMapping("/calculate")
    public ResponseEntity<RiskResult> calculateScore(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody FinancialDataRequest request
    ) {

        // Procesar score
        RiskResult result = financialDataService.guardarYCalcularScore(request);

        // AUDITORÍA
        auditLogService.logScoreEvaluation(
                currentUser,
                null,                                    // scoreBefore (no existe en tu lógica actual)
                result.getScoreFinal(),                  // scoreAfter
                result.getExplicacion()                  // reasoning
        );

        return ResponseEntity.ok(result);
    }
}
