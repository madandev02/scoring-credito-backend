package com.madandev.creditscoring.controller;

import com.madandev.creditscoring.domain.dto.FinancialDataRequest;
import com.madandev.creditscoring.domain.dto.RiskResult;
import com.madandev.creditscoring.domain.service.FinancialDataService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial-data")
public class FinancialDataController {

    private final FinancialDataService financialDataService;

    public FinancialDataController(FinancialDataService financialDataService) {
        this.financialDataService = financialDataService;
    }

    @PostMapping
    public ResponseEntity<RiskResult> procesarScore(@Valid @RequestBody FinancialDataRequest request) {
        RiskResult result = financialDataService.guardarYCalcularScore(request);
        return ResponseEntity.ok(result);
    }


}
