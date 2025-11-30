package com.madandev.creditscoring.domain.repository;

import com.madandev.creditscoring.domain.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Todos los logs, ordenados por fecha DESC
    List<AuditLog> findAllByOrderByCreatedAtDesc();

    // Logs por usuario
    List<AuditLog> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Logs por tipo de acción
    List<AuditLog> findByActionTypeOrderByCreatedAtDesc(String actionType);
}
