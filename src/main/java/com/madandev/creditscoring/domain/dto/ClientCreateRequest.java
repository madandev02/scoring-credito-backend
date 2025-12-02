package com.madandev.creditscoring.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientCreateRequest(

        @NotBlank(message = "El RUT es obligatorio")
        String rut,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100)
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 100)
        String lastName,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Formato de email inválido")
        String email,

        // Opcional: si no viene, lo generamos desde el RUT
        String username

) {
}
