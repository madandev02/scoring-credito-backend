package com.madandev.creditscoring.domain.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record FinancialDataDto(
        BigDecimal ingresosLiquidos,
        BigDecimal ingresosBrutos,
        String tipoContrato,
        Integer anosEstabilidad,
        String tipoVivienda,
        BigDecimal gastosFijos,
        BigDecimal deudasTotales,
        Integer numeroTarjetas,
        Boolean historialAtrasos,
        Boolean dicom,
        String activos,
        String region,
        Instant createdAt
) {}
