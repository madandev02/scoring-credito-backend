package com.madandev.creditscoring.domain.service;

import com.madandev.creditscoring.domain.dto.CreditApplicationRequest;
import com.madandev.creditscoring.domain.dto.CreditDecisionResponse;
import com.madandev.creditscoring.domain.entity.CreditRequest;
import com.madandev.creditscoring.domain.entity.ScoreHistory;
import com.madandev.creditscoring.domain.entity.User;
import com.madandev.creditscoring.domain.repository.CreditRequestRepository;
import com.madandev.creditscoring.domain.repository.ScoreHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class CreditRequestService {

    private final CreditRequestRepository creditRequestRepository;
    private final ScoreHistoryRepository scoreHistoryRepository;
    private final AuditLogService auditLogService;

    public CreditRequestService(CreditRequestRepository creditRequestRepository,
                                ScoreHistoryRepository scoreHistoryRepository,
                                AuditLogService auditLogService) {
        this.creditRequestRepository = creditRequestRepository;
        this.scoreHistoryRepository = scoreHistoryRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public CreditDecisionResponse applyForCredit(User user, CreditApplicationRequest request) {

        // Buscar último score del usuario
        ScoreHistory lastScore = scoreHistoryRepository
                .findTopByUserIdOrderByCreatedAtDesc(user.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "No se encontró score para el usuario. Primero debe calcular su score."));

        int scoreValue = lastScore.getScoreValue();
        String riskLevel = mapRiskLevel(scoreValue);

        double requested = request.getAmountRequested();
        int termMonths = request.getTermMonths();

        double approvedAmount = 0.0;
        double annualRate = 0.0;
        String status;
        String decisionReason;

        // Reglas simples tipo fintech, basadas en score:
        switch (riskLevel) {
            case "MUY BAJO" -> {
                approvedAmount = requested;
                annualRate = 18.0;
                status = "APPROVED";
                decisionReason = "Cliente de muy bajo riesgo. Se aprueba el 100% del monto solicitado.";
            }
            case "BAJO" -> {
                approvedAmount = requested * 0.8;
                annualRate = 24.0;
                status = "APPROVED";
                decisionReason = "Cliente de riesgo bajo. Se aprueba el 80% del monto solicitado.";
            }
            case "MEDIO" -> {
                approvedAmount = requested * 0.5;
                annualRate = 30.0;
                status = "APPROVED";
                decisionReason = "Cliente de riesgo medio. Se aprueba el 50% del monto solicitado.";
            }
            default -> {
                approvedAmount = 0.0;
                annualRate = 0.0;
                status = "REJECTED";
                decisionReason = "Cliente de alto riesgo según score. No es posible aprobar el crédito.";
            }
        }

        double monthlyInstallment = 0.0;
        if ("APPROVED".equals(status) && approvedAmount > 0 && termMonths > 0) {
            monthlyInstallment = calculateMonthlyInstallment(approvedAmount, annualRate, termMonths);
        }

        Instant now = Instant.now();

        CreditRequest entity = CreditRequest.builder()
                .user(user)
                .amountRequested(requested)
                .approvedAmount(approvedAmount)
                .interestRateAnnual(annualRate)
                .termMonths(termMonths)
                .monthlyInstallment(monthlyInstallment)
                .riskLevel(riskLevel)
                .decisionReason(decisionReason)
                .status(status)
                .requestDate(now)
                .resolutionDate(now)
                .build();

        CreditRequest saved = creditRequestRepository.save(entity);

        // AUDITORÍA DE DECISIÓN DE CRÉDITO
        auditLogService.logCreditDecision(user, saved, scoreValue, riskLevel);

        CreditDecisionResponse response = new CreditDecisionResponse();
        response.setId(saved.getId());
        response.setAmountRequested(saved.getAmountRequested());
        response.setApprovedAmount(saved.getApprovedAmount());
        response.setInterestRateAnnual(saved.getInterestRateAnnual());
        response.setTermMonths(saved.getTermMonths());
        response.setMonthlyInstallment(saved.getMonthlyInstallment());
        response.setStatus(saved.getStatus());
        response.setRiskLevel(saved.getRiskLevel());
        response.setDecisionReason(saved.getDecisionReason());
        response.setRequestDate(saved.getRequestDate());
        response.setResolutionDate(saved.getResolutionDate());
        response.setScoreAtDecision(scoreValue);

        return response;
    }

    public List<CreditRequest> getCreditsForUser(User user) {
        return creditRequestRepository.findByUserId(user.getId());
    }

    // ----------------------------------------------
    // Helpers
    // ----------------------------------------------

    private String mapRiskLevel(int score) {
        if (score > 900) return "MUY BAJO";
        if (score > 600) return "BAJO";
        if (score > 300) return "MEDIO";
        return "ALTO";
    }

    private double calculateMonthlyInstallment(double principal, double annualRate, int termMonths) {
        if (annualRate <= 0 || termMonths <= 0) {
            return principal / termMonths;
        }

        double monthlyRate = (annualRate / 100.0) / 12.0;

        double denominator = 1 - Math.pow(1 + monthlyRate, -termMonths);
        if (denominator == 0) {
            return principal / termMonths;
        }

        return principal * monthlyRate / denominator;
    }
}
