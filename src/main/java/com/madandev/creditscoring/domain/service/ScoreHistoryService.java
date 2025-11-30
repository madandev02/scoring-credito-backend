package com.madandev.creditscoring.domain.service;

import com.madandev.creditscoring.domain.dto.RiskResult;
import com.madandev.creditscoring.domain.entity.ScoreHistory;
import com.madandev.creditscoring.domain.entity.User;
import com.madandev.creditscoring.domain.repository.ScoreHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScoreHistoryService {

    private final ScoreHistoryRepository repository;
    private final AuditLogService auditLogService;

    public ScoreHistoryService(ScoreHistoryRepository repository,
                               AuditLogService auditLogService) {
        this.repository = repository;
        this.auditLogService = auditLogService;
    }

    /**
     * Guarda un nuevo registro de score para un usuario
     * y registra auditoría (score_before, score_after, reasoning).
     */
    public ScoreHistory guardar(User user, RiskResult result) {

        Integer scoreBefore = repository
                .findTopByUserIdOrderByCreatedAtDesc(user.getId())
                .map(ScoreHistory::getScoreValue)
                .orElse(null);

        ScoreHistory history = new ScoreHistory();
        history.setUser(user);
        history.setScoreValue(result.getScoreFinal());
        history.setChangeReason("Cálculo automático del motor de riesgo");

        ScoreHistory saved = repository.save(history);

        auditLogService.logScoreEvaluation(
                user,
                scoreBefore,
                result.getScoreFinal(),
                result.getExplicacion()
        );

        return saved;
    }

    public List<ScoreHistory> obtenerHistorialPorUsuario(Long userId) {
        return repository.findByUserId(userId);
    }

    public Optional<ScoreHistory> obtenerUltimoScore(Long userId) {
        return repository.findTopByUserIdOrderByCreatedAtDesc(userId);
    }
}
