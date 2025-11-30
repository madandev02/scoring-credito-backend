package com.madandev.creditscoring.controller;

import com.madandev.creditscoring.domain.dto.ScoreHistoryResponse;
import com.madandev.creditscoring.domain.dto.ScoreSummaryResponse;
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
     * Historial de score de un usuario específico.
     * Ideal para gráfico de línea en el frontend.
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
     * Resumen del score de un usuario:
     * - último score
     * - nivel de riesgo (calculado aquí)
     * - fecha de última act.
     * - promedio
     * - cantidad de registros
     */
    @GetMapping("/summary/{userId}")
    public ResponseEntity<ScoreSummaryResponse> getSummary(@PathVariable Long userId) {

        List<ScoreHistory> historyList =
                scoreHistoryService.obtenerHistorialPorUsuario(userId);

        ScoreSummaryResponse summary = new ScoreSummaryResponse();
        summary.setTotalRecords((long) historyList.size());

        if (!historyList.isEmpty()) {
            // último por fecha
            ScoreHistory last = historyList.stream()
                    .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                    .findFirst()
                    .orElseThrow();

            summary.setLastScore(last.getScoreValue());
            summary.setLastUpdated(last.getCreatedAt());

            // Nivel de riesgo calculado según el score (misma lógica que el motor)
            int score = last.getScoreValue();
            String riskLevel;
            if (score > 900) {
                riskLevel = "MUY BAJO";
            } else if (score > 600) {
                riskLevel = "BAJO";
            } else if (score > 300) {
                riskLevel = "MEDIO";
            } else {
                riskLevel = "ALTO";
            }
            summary.setRiskLevel(riskLevel);

            // Estadísticas para promedio
            DoubleSummaryStatistics stats = historyList.stream()
                    .mapToDouble(ScoreHistory::getScoreValue)
                    .summaryStatistics();

            summary.setAverageScore(stats.getAverage());

        } else {
            summary.setLastScore(null);
            summary.setLastUpdated(null);
            summary.setRiskLevel("SIN_DATOS");
            summary.setAverageScore(null);
        }

        return ResponseEntity.ok(summary);
    }
}
