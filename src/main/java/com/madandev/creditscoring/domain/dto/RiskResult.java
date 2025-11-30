package com.madandev.creditscoring.domain.dto;

import java.math.BigDecimal;

public class RiskResult {
    private int scoreFinal;
    private String nivelRiesgo;
    private String explicacion;
    private BigDecimal ratioGastoIngreso;
    private BigDecimal ratioDeudaIngreso;
    private boolean tieneDICOM;

    // Constructor vacío
    public RiskResult() {
    }

    // Constructor con parámetros
    public RiskResult(int scoreFinal, String nivelRiesgo, String explicacion,
                      BigDecimal ratioGastoIngreso, BigDecimal ratioDeudaIngreso,
                      boolean tieneDICOM) {
        this.scoreFinal = scoreFinal;
        this.nivelRiesgo = nivelRiesgo;
        this.explicacion = explicacion;
        this.ratioGastoIngreso = ratioGastoIngreso;
        this.ratioDeudaIngreso = ratioDeudaIngreso;
        this.tieneDICOM = tieneDICOM;
    }

    // Getters y Setters
    public int getScoreFinal() {
        return scoreFinal;
    }

    public void setScoreFinal(int scoreFinal) {
        this.scoreFinal = scoreFinal;
    }

    public String getNivelRiesgo() {
        return nivelRiesgo;
    }

    public void setNivelRiesgo(String nivelRiesgo) {
        this.nivelRiesgo = nivelRiesgo;
    }

    public String getExplicacion() {
        return explicacion;
    }

    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
    }

    public BigDecimal getRatioGastoIngreso() {
        return ratioGastoIngreso;
    }

    public void setRatioGastoIngreso(BigDecimal ratioGastoIngreso) {
        this.ratioGastoIngreso = ratioGastoIngreso;
    }

    public BigDecimal getRatioDeudaIngreso() {
        return ratioDeudaIngreso;
    }

    public void setRatioDeudaIngreso(BigDecimal ratioDeudaIngreso) {
        this.ratioDeudaIngreso = ratioDeudaIngreso;
    }

    public boolean isTieneDICOM() {
        return tieneDICOM;
    }

    public void setTieneDICOM(boolean tieneDICOM) {
        this.tieneDICOM = tieneDICOM;
    }
}