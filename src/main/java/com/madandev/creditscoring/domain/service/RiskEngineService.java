package com.madandev.creditscoring.domain.service;

import com.madandev.creditscoring.domain.dto.FinancialDataRequest;
import com.madandev.creditscoring.domain.dto.RiskResult;
import com.madandev.creditscoring.domain.entity.FinancialData;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class RiskEngineService {

    // --- Overload opcional por si algún día quieres usar el DTO directamente ---
    public RiskResult calcularScore(FinancialDataRequest request) {
        FinancialData data = new FinancialData();
        data.setIngresosLiquidos(request.getIngresosLiquidos());
        data.setIngresosBrutos(request.getIngresosBrutos());
        data.setTipoContrato(request.getTipoContrato());
        data.setAnosEstabilidad(request.getAnosEstabilidad());
        data.setTipoVivienda(request.getTipoVivienda());
        data.setGastosFijos(request.getGastosFijos());
        data.setDeudasTotales(request.getDeudasTotales());
        data.setNumeroTarjetas(request.getNumeroTarjetas());
        data.setHistorialAtrasos(request.getHistorialAtrasos());
        data.setDicom(request.getDicom());
        data.setActivos(request.getActivos());
        data.setRegion(request.getRegion());
        // data.setRut(request.getRut()); // si tu entidad lo tiene

        return calcularScore(data);
    }

    // --- Método principal que usa la entidad ---
    public RiskResult calcularScore(FinancialData data) {

        BigDecimal ratioGasto = calcularRatioGastoIngreso(data);
        BigDecimal ratioDeuda = calcularRatioDeudaIngreso(data);

        int score = 0;

        score += puntajeBaseIngresos(data);
        score += puntajePorEstabilidad(data);
        score += puntajePorActivos(data);
        score += puntajePorDICOM(data);

        // Penalizaciones
        if (ratioGasto.compareTo(new BigDecimal("0.50")) > 0) score -= 150;
        if (ratioDeuda.compareTo(new BigDecimal("0.40")) > 0) score -= 200;

        // Limitar score 0–1000
        if (score < 0) score = 0;
        if (score > 1000) score = 1000;

        String riesgo = determinarNivelRiesgo(score);

        RiskResult result = new RiskResult();
        result.setScoreFinal(score);
        result.setNivelRiesgo(riesgo);
        result.setRatioGastoIngreso(ratioGasto);
        result.setRatioDeudaIngreso(ratioDeuda);
        result.setTieneDICOM(Boolean.TRUE.equals(data.getDicom()));
        result.setExplicacion("Cálculo completado correctamente.");

        return result;
    }

    // ----------------------------------------------
    // RATIO GASTO / INGRESO
    // ----------------------------------------------
    private BigDecimal calcularRatioGastoIngreso(FinancialData data) {
        BigDecimal ingreso = safe(data.getIngresosLiquidos());
        BigDecimal gastos  = safe(data.getGastosFijos());

        if (ingreso.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return gastos.divide(ingreso, 4, RoundingMode.HALF_UP);
    }

    // ----------------------------------------------
    // RATIO DEUDA / INGRESO
    // ----------------------------------------------
    private BigDecimal calcularRatioDeudaIngreso(FinancialData data) {
        BigDecimal ingreso = safe(data.getIngresosLiquidos());
        BigDecimal deuda   = safe(data.getDeudasTotales());

        if (ingreso.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return deuda.divide(ingreso, 4, RoundingMode.HALF_UP);
    }

    private BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    // ----------------------------------------------
    // PUNTAJE POR INGRESOS
    // ----------------------------------------------
    private int puntajeBaseIngresos(FinancialData data) {
        BigDecimal ingreso = safe(data.getIngresosLiquidos());

        if (ingreso.compareTo(new BigDecimal("1500000")) >= 0) return 400;
        if (ingreso.compareTo(new BigDecimal("1000000")) >= 0) return 250;
        if (ingreso.compareTo(new BigDecimal("700000")) >= 0) return 150;

        return 50;
    }

    // ----------------------------------------------
    // DICOM
    // ----------------------------------------------
    private int puntajePorDICOM(FinancialData data) {
        return Boolean.TRUE.equals(data.getDicom()) ? -300 : 0;
    }

    // ----------------------------------------------
    // ESTABILIDAD LABORAL
    // ----------------------------------------------
    private int puntajePorEstabilidad(FinancialData data) {
        Integer anos = data.getAnosEstabilidad();
        if (anos == null) return 0;

        if (anos >= 5) return 200;
        if (anos >= 3) return 120;
        if (anos >= 1) return 60;

        return 20;
    }

    // ----------------------------------------------
    // ACTIVOS
    // ----------------------------------------------
    private int puntajePorActivos(FinancialData data) {
        String activos = data.getActivos();
        if (activos == null) return 0;

        activos = activos.toLowerCase();

        if (activos.contains("casa")) return 350;
        if (activos.contains("departamento")) return 250;
        if (activos.contains("auto") && activos.contains("ahorro")) return 120;
        if (activos.contains("auto")) return 50;

        return 0;
    }

    // ----------------------------------------------
    // NIVEL DE RIESGO (interno)
    // ----------------------------------------------
    private String determinarNivelRiesgo(int score) {
        if (score > 900) return "MUY BAJO";
        if (score > 600) return "BAJO";
        if (score > 300) return "MEDIO";
        return "ALTO";
    }

    // ----------------------------------------------
    // NIVEL DE RIESGO (público p/ Admin)
    // ----------------------------------------------
    public String determinarNivelRiesgoPublic(int score) {
        return determinarNivelRiesgo(score);
    }
}
