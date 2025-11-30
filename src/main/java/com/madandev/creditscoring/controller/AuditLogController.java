package com.madandev.creditscoring.controller;

import com.madandev.creditscoring.domain.entity.AuditLog;
import com.madandev.creditscoring.domain.repository.AuditLogRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@Tag(name = "Auditoría", description = "Logs de eventos financieros, scoring y decisiones del sistema")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    /**
     * Obtener todos los eventos de auditoría.
     * SOLO ADMIN puede ver esto.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AuditLog> getAll() {
        return auditLogRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Obtener eventos de auditoría asociados a un usuario específico.
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AuditLog> getByUser(@PathVariable Long userId) {
        return auditLogRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Obtener auditorías por tipo de acción.
     * Ejemplos:
     * - LOGIN
     * - SCORE_CALC
     * - CREDIT_DECISION
     */
    @GetMapping("/action/{actionType}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AuditLog> getByAction(@PathVariable String actionType) {
        return auditLogRepository.findByActionTypeOrderByCreatedAtDesc(actionType);
    }
}
