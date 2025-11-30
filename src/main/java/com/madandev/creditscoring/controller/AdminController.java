package com.madandev.creditscoring.controller;

import com.madandev.creditscoring.domain.dto.AdminCreditSummaryResponse;
import com.madandev.creditscoring.domain.dto.AdminOverviewResponse;
import com.madandev.creditscoring.domain.dto.AdminUserSummaryResponse;
import com.madandev.creditscoring.domain.entity.CreditRequest;
import com.madandev.creditscoring.domain.entity.ScoreHistory;
import com.madandev.creditscoring.domain.entity.User;
import com.madandev.creditscoring.domain.repository.CreditRequestRepository;
import com.madandev.creditscoring.domain.repository.ScoreHistoryRepository;
import com.madandev.creditscoring.domain.repository.UserRepository;
import com.madandev.creditscoring.domain.service.AuditLogService;
import com.madandev.creditscoring.domain.service.RiskEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Gestión administrativa y estadísticas")
public class AdminController {

    private final UserRepository userRepository;
    private final ScoreHistoryRepository scoreHistoryRepository;
    private final CreditRequestRepository creditRequestRepository;
    private final RiskEngineService riskEngineService;
    private final AuditLogService auditLogService;

    @GetMapping("/overview")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<AdminOverviewResponse> overview(
            @AuthenticationPrincipal User admin
    ) {
        auditLogService.logAdminAccess(admin, "/api/admin/overview");

        long totalUsers = userRepository.count();
        long totalScores = scoreHistoryRepository.count();
        long totalCredits = creditRequestRepository.count();

        List<ScoreHistory> scores = scoreHistoryRepository.findAll();
        DoubleSummaryStatistics scoreStats = scores.stream()
                .mapToDouble(ScoreHistory::getScoreValue)
                .summaryStatistics();

        List<CreditRequest> credits = creditRequestRepository.findAll();

        long approvedCount = credits.stream()
                .filter(c -> "APPROVED".equalsIgnoreCase(c.getStatus()))
                .count();

        long rejectedCount = credits.stream()
                .filter(c -> "REJECTED".equalsIgnoreCase(c.getStatus()))
                .count();

        double totalApprovedAmount = credits.stream()
                .filter(c -> "APPROVED".equalsIgnoreCase(c.getStatus()))
                .map(CreditRequest::getApprovedAmount)
                .filter(a -> a != null)
                .mapToDouble(Double::doubleValue)
                .sum();

        double approvalRate = (totalCredits > 0)
                ? (approvedCount * 100.0) / totalCredits
                : 0.0;

        double rejectionRate = (totalCredits > 0)
                ? (rejectedCount * 100.0) / totalCredits
                : 0.0;

        AdminOverviewResponse response = new AdminOverviewResponse();
        response.setTotalUsers(totalUsers);
        response.setTotalScores(totalScores);
        response.setTotalCredits(totalCredits);
        response.setAverageScore(scoreStats.getCount() > 0 ? scoreStats.getAverage() : null);
        response.setTotalApprovedAmount(totalApprovedAmount);
        response.setApprovalRate(approvalRate);
        response.setRejectionRate(rejectionRate);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<List<AdminUserSummaryResponse>> listUsers(
            @AuthenticationPrincipal User admin
    ) {
        auditLogService.logAdminAccess(admin, "/api/admin/users");

        List<User> users = userRepository.findAll();

        List<AdminUserSummaryResponse> response = users.stream().map(user -> {

            List<ScoreHistory> scoresOfUser = scoreHistoryRepository.findByUserId(user.getId());
            Long totalScores = (long) scoresOfUser.size();

            Integer lastScore = null;
            String lastRiskLevel = "SIN_DATOS";

            if (!scoresOfUser.isEmpty()) {
                ScoreHistory last = scoresOfUser.stream()
                        .max((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                        .orElse(null);

                if (last != null) {
                    lastScore = last.getScoreValue();
                    lastRiskLevel = riskEngineService.determinarNivelRiesgoPublic(lastScore);
                }
            }

            AdminUserSummaryResponse dto = new AdminUserSummaryResponse();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole().name());
            dto.setCreatedAt(user.getCreatedAt());
            dto.setTotalScores(totalScores);
            dto.setLastScore(lastScore);
            dto.setLastRiskLevel(lastRiskLevel);

            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/credits")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<List<AdminCreditSummaryResponse>> listCredits(
            @AuthenticationPrincipal User admin
    ) {
        auditLogService.logAdminAccess(admin, "/api/admin/credits");

        List<CreditRequest> credits = creditRequestRepository.findAll();

        List<AdminCreditSummaryResponse> response = credits.stream().map(c -> {
            AdminCreditSummaryResponse dto = new AdminCreditSummaryResponse();
            dto.setId(c.getId());
            dto.setUserId(c.getUser() != null ? c.getUser().getId() : null);
            dto.setUsername(c.getUser() != null ? c.getUser().getUsername() : null);
            dto.setAmountRequested(c.getAmountRequested());
            dto.setApprovedAmount(c.getApprovedAmount());
            dto.setInterestRateAnnual(c.getInterestRateAnnual());
            dto.setTermMonths(c.getTermMonths());
            dto.setMonthlyInstallment(c.getMonthlyInstallment());
            dto.setStatus(c.getStatus());
            dto.setRiskLevel(c.getRiskLevel());
            dto.setRequestDate(c.getRequestDate());
            dto.setResolutionDate(c.getResolutionDate());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/scores-by-risk")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<Map<String, Long>> scoresByRisk(
            @AuthenticationPrincipal User admin
    ) {
        auditLogService.logAdminAccess(admin, "/api/admin/scores-by-risk");

        List<ScoreHistory> scores = scoreHistoryRepository.findAll();

        Map<String, Long> grouped = scores.stream()
                .collect(Collectors.groupingBy(
                        s -> riskEngineService.determinarNivelRiesgoPublic(s.getScoreValue()),
                        Collectors.counting()
                ));

        return ResponseEntity.ok(grouped);
    }
}
