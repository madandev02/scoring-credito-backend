package com.madandev.creditscoring.domain.repository;

import com.madandev.creditscoring.domain.entity.ScoreHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScoreHistoryRepository extends JpaRepository<ScoreHistory, Long> {

    List<ScoreHistory> findByUserId(Long userId);

    Optional<ScoreHistory> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
