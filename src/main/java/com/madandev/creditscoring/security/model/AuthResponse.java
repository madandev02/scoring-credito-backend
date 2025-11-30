package com.madandev.creditscoring.security.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String token;
    private Long id;
    private String username;
    private String role;

    // error
    private String error;
}
