package com.madandev.creditscoring.domain.dto;

import com.madandev.creditscoring.domain.validation.ValidRut;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class FinancialDataRequest {

    @NotBlank(message = "El RUT es obligatorio")
    @ValidRut
    private String rut;

    @NotNull(message = "Los ingresos líquidos son obligatorios")
    @Positive(message = "Los ingresos líquidos")
    private BigDecimal ingresosLiquidos;

    @NotNull(message = "Los ingresos brutos son obligatorios")
    @Positive(message = "Los ingresos brutos deben ser mayores a 0")
    private BigDecimal ingresosBrutos;

    @NotBlank(message = "El tipo de contrato es obligatorio")
    @Size(max = 50, message = "El tipo de contrato no puede exceder los 50 caracteres")
    private String tipoContrato;

    @NotNull(message = "Los años de estabilidad son obligatorios")
    @Min(value = 0, message = "Los años de estabilidad no pueden ser negativos")
    @Max(value = 50, message = "Los años de estabilidad no pueden ser mayores a 50")
    private Integer anosEstabilidad;

    @NotBlank(message = "El tipo de vivienda es obligatorio")
    @Size(max = 50, message = "El tipo de vivienda no puede exceder los 50 caracteres")
    private String tipoVivienda;

    @NotNull(message = "Los gastos fijos son obligatorios")
    @PositiveOrZero(message = "Los gastos fijos no pueden ser negativos")
    private BigDecimal gastosFijos;

    @NotNull(message = "Las deudas totales son obligatorios")
    @PositiveOrZero(message = "Las deudas totales no pueden ser negativas")
    private BigDecimal deudasTotales;

    @NotNull(message = "El número de tarjetas es obligatorio")
    @Min(value = 0, message = "El número de tarjetas no puede ser negativo")
    @Max(value = 20, message = "El número de tarjetas no puede ser mayor a 20")
    private Integer numeroTarjetas;

    @NotNull(message = "El historial de atrasos es obligatorio")
    private Boolean historialAtrasos;

    @NotNull(message = "El campo DICOM es obligatorio")
    private Boolean dicom;

    @NotBlank(message = "Los activos son obligatorios")
    @Size(max = 200, message = "Los activos no pueden exceder los 200 caracteres")
    private String activos;

    @NotBlank(message = "La región es obligatoria")
    @Size(max = 50, message = "La región no puede exceder los 50 caracteres")
    private String region;

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;

    // Constructor vacío
    public FinancialDataRequest() {

    }

    // Getter y Setters
    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public BigDecimal getIngresosLiquidos() {
        return ingresosLiquidos;
    }

    public void setIngresosLiquidos(BigDecimal ingresosLiquidos) {
        this.ingresosLiquidos = ingresosLiquidos;
    }

    public BigDecimal getIngresosBrutos() {
        return ingresosBrutos;
    }

    public void setIngresosBrutos(BigDecimal ingresosBrutos) {
        this.ingresosBrutos = ingresosBrutos;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public Integer getAnosEstabilidad() {
        return anosEstabilidad;
    }

    public void setAnosEstabilidad(Integer anosEstabilidad) {
        this.anosEstabilidad = anosEstabilidad;
    }

    public String getTipoVivienda() {
        return tipoVivienda;
    }

    public void setTipoVivienda(String tipoVivienda) {
        this.tipoVivienda = tipoVivienda;
    }

    public BigDecimal getGastosFijos() {
        return gastosFijos;
    }

    public void setGastosFijos(BigDecimal gastosFijos) {
        this.gastosFijos = gastosFijos;
    }

    public BigDecimal getDeudasTotales() {
        return deudasTotales;
    }

    public void setDeudasTotales(BigDecimal deudasTotales) {
        this.deudasTotales = deudasTotales;
    }

    public Integer getNumeroTarjetas() {
        return numeroTarjetas;
    }

    public void setNumeroTarjetas(Integer numeroTarjetas) {
        this.numeroTarjetas = numeroTarjetas;
    }

    public Boolean getHistorialAtrasos() {
        return historialAtrasos;
    }

    public void setHistorialAtrasos(Boolean historialAtrasos) {
        this.historialAtrasos = historialAtrasos;
    }

    public Boolean getDicom() {
        return dicom;
    }

    public void setDicom(Boolean dicom) {
        this.dicom = dicom;
    }

    public String getActivos() {
        return activos;
    }

    public void setActivos(String activos) {
        this.activos = activos;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
