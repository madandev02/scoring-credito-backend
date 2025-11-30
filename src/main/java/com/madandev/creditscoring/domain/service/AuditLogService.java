package com.madandev.creditscoring.domain.service;

import com.madandev.creditscoring.domain.entity.AuditLog;
import com.madandev.creditscoring.domain.entity.CreditRequest;
import com.madandev.creditscoring.domain.entity.User;
import com.madandev.creditscoring.domain.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    // ============================================
    // 1) LOGIN
    // ============================================
    public void logLogin(User user, boolean success) {

        AuditLog log = AuditLog.builder()
                .user(user)
                .actionType("LOGIN")
                .endpoint("/api/auth/login")
                .status(success ? "SUCCESS" : "ERROR")
                .accessLogScore(false)
                .build();

        auditLogRepository.save(log);
    }

    // ============================================
    // 2) SCORE EVALUATION
    // ============================================
    public void logScoreEvaluation(User user,
                                   Integer scoreBefore,
                                   Integer scoreAfter,
                                   String reasoning) {

        AuditLog log = AuditLog.builder()
                .user(user)
                .actionType("SCORE_EVALUATION")
                .endpoint("/api/score/calculate")
                .scoreBefore(scoreBefore)
                .scoreAfter(scoreAfter)
                .scoreReasoning(reasoning)
                .status("SUCCESS")
                .accessLogScore(true)
                .build();

        auditLogRepository.save(log);
    }


    // ============================================
    // 3) CREDIT DECISION
    // ============================================
    public void logCreditDecision(User user,
                                  CreditRequest credit,
                                  Integer scoreAtDecision,
                                  String riskLevel) {

        AuditLog log = AuditLog.builder()
                .user(user)
                .actionType("CREDIT_DECISION")
                .endpoint("/api/credits/apply")
                .scoreAfter(scoreAtDecision)
                .creditDecisionReason(credit.getDecisionReason())
                .status(credit.getStatus()) // APPROVED / REJECTED
                .accessLogScore(true)
                .build();

        auditLogRepository.save(log);
    }


    // ============================================
    // 4) ADMIN ACCESS
    // ============================================
    public void logAdminAccess(User adminUser, String endpoint) {

        AuditLog log = AuditLog.builder()
                .user(adminUser) // Administrador que accedió
                .actionType("ADMIN_ACCESS")
                .endpoint(endpoint)
                .status("SUCCESS")
                .accessLogScore(false)
                .build();

        auditLogRepository.save(log);
    }
}
