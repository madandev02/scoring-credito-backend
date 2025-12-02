package com.madandev.creditscoring.domain.service;

import com.madandev.creditscoring.domain.dto.*;
import com.madandev.creditscoring.domain.entity.CreditRequest;
import com.madandev.creditscoring.domain.entity.FinancialData;
import com.madandev.creditscoring.domain.entity.ScoreHistory;
import com.madandev.creditscoring.domain.entity.User;
import com.madandev.creditscoring.domain.repository.*;
import com.madandev.creditscoring.security.model.Role;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final UserRepository userRepository;
    private final ScoreHistoryRepository scoreHistoryRepository;
    private final CreditRequestRepository creditRequestRepository;
    private final FinancialDataRepository financialDataRepository;
    private final PasswordEncoder passwordEncoder;

    // ============================================================
    // CREAR CLIENTE
    // ============================================================
    public ClientDetailResponse createClient(ClientCreateRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent())
            throw new IllegalStateException("Ya existe un cliente con ese email.");

        if (userRepository.findByRut(request.rut()).isPresent())
            throw new IllegalStateException("Ya existe un cliente con ese RUT.");

        String username = (request.username() == null || request.username().isBlank())
                ? request.rut().replace(".", "").replace("-", "")
                : request.username();

        User client = User.builder()
                .username(username)
                .email(request.email())
                .rut(request.rut())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(Role.CLIENT)
                .password(passwordEncoder.encode("Temporal123!"))
                .build();

        client = userRepository.save(client);

        return getClientDetail(client.getId());
    }

    // ============================================================
    // LISTA DE CLIENTES
    // ============================================================
    public List<ClientListResponse> listClients() {

        List<User> clients = userRepository.findByRole(Role.CLIENT);

        return clients.stream()
                .map(user -> {

                    ScoreHistory latest = scoreHistoryRepository
                            .findTopByUserIdOrderByCreatedAtDesc(user.getId())
                            .orElse(null);

                    Integer score = (latest == null ? null : latest.getScoreValue());

                    String risk =
                            score == null ? "SIN EVALUAR" :
                                    score >= 750 ? "BAJO" :
                                            score >= 600 ? "MEDIO" : "ALTO";

                    return ClientListResponse.builder()
                            .id(user.getId())
                            .rut(user.getRut())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .username(user.getUsername())
                            .latestScore(score)
                            .riskLevel(risk)
                            .status(score == null ? "PENDIENTE" : "EVALUADO")
                            .role("CLIENT")
                            .build();
                })
                .toList();
    }

    // ============================================================
    // DETALLE CLIENTE
    // ============================================================
    public ClientDetailResponse getClientDetail(Long id) {

        User client = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado."));

        // ------- FINANCIAL DATA -------
        List<FinancialData> fin = financialDataRepository.findByUserId(id);
        FinancialData fd = fin.isEmpty() ? null : fin.get(0);

        FinancialDataDto financialDto = (fd == null)
                ? null
                : FinancialDataDto.builder()
                .ingresosLiquidos(fd.getIngresosLiquidos())
                .ingresosBrutos(fd.getIngresosBrutos())
                .tipoContrato(fd.getTipoContrato())
                .anosEstabilidad(fd.getAnosEstabilidad())
                .tipoVivienda(fd.getTipoVivienda())
                .gastosFijos(fd.getGastosFijos())
                .deudasTotales(fd.getDeudasTotales())
                .numeroTarjetas(fd.getNumeroTarjetas())
                .historialAtrasos(fd.getHistorialAtrasos())
                .dicom(fd.getDicom())
                .activos(fd.getActivos())
                .region(fd.getRegion())
                .createdAt(fd.getCreatedAt())
                .build();

        // ------- SCORE HISTORY -------
        List<ScoreHistoryDto> scoreHistoryDtos = scoreHistoryRepository
                .findByUserId(id)
                .stream()
                .sorted(Comparator.comparing(ScoreHistory::getCreatedAt).reversed())
                .map(s -> ScoreHistoryDto.builder()
                        .scoreValue(s.getScoreValue())
                        .changeReason(s.getChangeReason())
                        .createdAt(s.getCreatedAt())
                        .build()
                ).toList();

        ScoreHistory latest = scoreHistoryRepository
                .findTopByUserIdOrderByCreatedAtDesc(id)
                .orElse(null);

        Integer latestScore = (latest == null ? null : latest.getScoreValue());

        String riskLevel =
                latestScore == null ? "SIN EVALUAR" :
                        latestScore >= 750 ? "BAJO" :
                                latestScore >= 600 ? "MEDIO" : "ALTO";

        // ------- CREDIT REQUEST -------
        List<CreditRequestDto> creditDtos = creditRequestRepository
                .findByUserId(id)
                .stream()
                .map(cr -> CreditRequestDto.builder()
                        .id(cr.getId())
                        .amountRequested(cr.getAmountRequested())
                        .status(cr.getStatus())
                        .createdAt(cr.getCreatedAt() == null ? null :
                                cr.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime())
                        .build()
                ).toList();

        return ClientDetailResponse.builder()
                .id(client.getId())
                .rut(client.getRut())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(client.getEmail())
                .username(client.getUsername())
                .financialData(financialDto)
                .scoreHistory(scoreHistoryDtos)
                .creditRequests(creditDtos)
                .latestScore(latestScore)
                .riskLevel(riskLevel)
                .build();
    }
}
