package com.madandev.creditscoring.controller;

import com.madandev.creditscoring.domain.dto.DashboardSummaryResponse;
import com.madandev.creditscoring.domain.dto.ScoreHistoryResponse;
import com.madandev.creditscoring.domain.entity.ScoreHistory;
import com.madandev.creditscoring.domain.service.ScoreHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard/score")
public class ScoreDashboardController {

    private final ScoreHistoryService scoreHistoryService;

    public ScoreDashboardController(ScoreHistoryService scoreHistoryService) {
        this.scoreHistoryService = scoreHistoryService;
    }

    /**
     * Historial para gráfico del frontend
     */
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<ScoreHistoryResponse>> getHistory(@PathVariable Long userId) {

        List<ScoreHistory> historyList =
                scoreHistoryService.obtenerHistorialPorUsuario(userId);

        List<ScoreHistoryResponse> response = historyList.stream().map(h -> {
            ScoreHistoryResponse dto = new ScoreHistoryResponse();
            dto.setId(h.getId());
            dto.setScoreValue(h.getScoreValue());
            dto.setChangeReason(h.getChangeReason());
            dto.setCreatedAt(h.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Nuevo resumen para Dashboard del frontend
     * Aquí devolvemos: totalEvaluations, approvalRate, rejectRate, avgScore
     */
    @GetMapping("/summary/{userId}")
    public ResponseEntity<DashboardSummaryResponse> getDashboardSummary(@PathVariable Long userId) {

        List<ScoreHistory> historyList =
                scoreHistoryService.obtenerHistorialPorUsuario(userId);

        int totalEvaluations = historyList.size();

        double avgScore = 0;
        double approvalRate = 0;
        double rejectRate = 0;

        if (!historyList.isEmpty()) {
            // PROMEDIO
            DoubleSummaryStatistics stats = historyList.stream()
                    .mapToDouble(ScoreHistory::getScoreValue)
                    .summaryStatistics();

            avgScore = stats.getAverage();

            // APROBADOS (score > 600)
            long approved = historyList.stream()
                    .filter(x -> x.getScoreValue() > 600)
                    .count();

            approvalRate = (approved * 100.0) / totalEvaluations;
            rejectRate = 100 - approvalRate;
        }

        DashboardSummaryResponse summary = new DashboardSummaryResponse(
                totalEvaluations,
                approvalRate,
                rejectRate,
                avgScore
        );

        return ResponseEntity.ok(summary);
    }
}
