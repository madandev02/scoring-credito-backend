package com.madandev.creditscoring.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientListResponse {

    private Long id;
    private String rut;
    private String firstName;
    private String lastName;
    private String username;
    private String role;

    // Datos de scoring actuales
    private Integer latestScore;
    private String riskLevel;

    // Estado: "Evaluado" o "Pendiente"
    private String status;
}
