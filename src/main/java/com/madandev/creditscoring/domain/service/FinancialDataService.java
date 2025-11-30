package com.madandev.creditscoring.domain.service;

import com.madandev.creditscoring.domain.dto.FinancialDataRequest;
import com.madandev.creditscoring.domain.dto.RiskResult;
import com.madandev.creditscoring.domain.entity.FinancialData;
import com.madandev.creditscoring.domain.entity.User;
import com.madandev.creditscoring.domain.repository.FinancialDataRepository;
import org.springframework.stereotype.Service;

@Service
public class FinancialDataService {

    private final FinancialDataRepository repository;
    private final UserService userService;
    private final RiskEngineService riskEngineService;
    private final ScoreHistoryService scoreHistoryService;

    public FinancialDataService(
            FinancialDataRepository repository,
            UserService userService,
            RiskEngineService riskEngineService,
            ScoreHistoryService scoreHistoryService
    ) {
        this.repository = repository;
        this.userService = userService;
        this.riskEngineService = riskEngineService;
        this.scoreHistoryService = scoreHistoryService;
    }

    public RiskResult guardarYCalcularScore(FinancialDataRequest request) {

        // 1) Buscar usuario
        User user = userService.findById(request.getUserId());

        // 2) Crear objeto FinancialData
        FinancialData data = new FinancialData();
        data.setUser(user);
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

        // 3) Guardar en BD
        repository.save(data);

        // 4) Calcular score
        RiskResult result = riskEngineService.calcularScore(data);

        // 5) Guardar en historial
        scoreHistoryService.guardar(user, result);

        // 6) Retornar
        return result;
    }
}
