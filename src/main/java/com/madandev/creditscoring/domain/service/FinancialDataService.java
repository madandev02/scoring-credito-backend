package com.madandev.creditscoring.domain.service;

import com.madandev.creditscoring.domain.dto.FinancialDataRequest;
import com.madandev.creditscoring.domain.dto.RiskResult;
import com.madandev.creditscoring.domain.entity.FinancialData;
import com.madandev.creditscoring.domain.entity.User;
import com.madandev.creditscoring.domain.repository.FinancialDataRepository;
import jakarta.transaction.Transactional;
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

    /**
     * Guarda o actualiza los datos financieros del usuario,
     * luego calcula el score y registra la operación en el historial.
     */
    @Transactional
    public RiskResult guardarYCalcularScore(FinancialDataRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("FinancialDataRequest no puede ser null");
        }

        // 1) Buscar usuario
        User user = userService.findById(request.getUserId());

        // 2) Buscar si ya existen datos previos
        FinancialData existing = repository.findByUserId(request.getUserId())
                .stream()
                .findFirst()
                .orElse(null);

        FinancialData data = (existing != null) ? existing : new FinancialData();
        data.setUser(user);

        // 3) Mapear todos los campos desde el request
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
        data.setRut(request.getRut());

        // 4) Persistir en BD
        repository.save(data);

        // 5) Calcular score con la info actualizada
        RiskResult result = riskEngineService.calcularScore(data);

        // 6) Registrar historial/auditoría
        scoreHistoryService.guardar(user, result);

        // 7) Retornar resultado final
        return result;
    }
}
