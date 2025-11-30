package com.madandev.creditscoring.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO para que el usuario pida un crédito.
 */
@Data
public class CreditApplicationRequest {

    @NotNull
    @Min(50000) // mínimo 50.000 CLP por ejemplo
    private Double amountRequested;

    @NotNull
    @Min(6) // mínimo 6 meses
    private Integer termMonths;
}
