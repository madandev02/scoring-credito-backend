package com.madandev.creditscoring.controller;

import com.madandev.creditscoring.domain.dto.CreditApplicationRequest;
import com.madandev.creditscoring.domain.dto.CreditDecisionResponse;
import com.madandev.creditscoring.domain.entity.User;
import com.madandev.creditscoring.domain.service.AuditLogService;
import com.madandev.creditscoring.domain.service.CreditRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/credits")
@RequiredArgsConstructor
@Tag(name = "Créditos", description = "Solicitudes y decisiones crediticias")
public class CreditController {

    private final CreditRequestService creditRequestService;
    private final AuditLogService auditLogService;

    @PostMapping("/apply")
    @PreAuthorize("hasAnyRole('USER','ANALYST','ADMIN')")
    public ResponseEntity<CreditDecisionResponse> apply(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody CreditApplicationRequest request
    ) {
        CreditDecisionResponse response =
                creditRequestService.applyForCredit(currentUser, request);

        // AUDITORÍA: registrar decisión
        auditLogService.logCreditDecision(
                currentUser,
                null,                                  // creditRequest (tu servicio lo guarda solo)
                response.getScoreAtDecision(),          // ahora sí existe
                response.getRiskLevel()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ANALYST','ADMIN')")
    public ResponseEntity<List<?>> myCredits(
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(
                creditRequestService.getCreditsForUser(currentUser)
        );
    }
}
