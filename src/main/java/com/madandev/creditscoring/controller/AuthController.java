package com.madandev.creditscoring.controller;

import com.madandev.creditscoring.domain.entity.User;
import com.madandev.creditscoring.domain.repository.UserRepository;
import com.madandev.creditscoring.domain.service.AuditLogService;
import com.madandev.creditscoring.security.jwt.JwtService;
import com.madandev.creditscoring.security.model.AuthRequest;
import com.madandev.creditscoring.security.model.AuthResponse;
import com.madandev.creditscoring.security.model.RegisterRequest;
import com.madandev.creditscoring.security.model.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuditLogService auditLogService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(
                    AuthResponse.builder()
                            .error("USERNAME_ALREADY_EXISTS")
                            .build()
            );
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(
                    AuthResponse.builder()
                            .error("EMAIL_ALREADY_EXISTS")
                            .build()
            );
        }

        Role role = Role.USER;
        if ("ANALYST".equalsIgnoreCase(request.getRole())) {
            role = Role.ANALYST;
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(role)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .token(token)
                        .id(user.getId())
                        .username(user.getUsername())
                        .role(user.getRole().name())
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {

        Authentication auth;

        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {

            auditLogService.logLogin(
                    userRepository.findByUsername(request.getUsername()).orElse(null),
                    false
            );

            return ResponseEntity.status(401).body(
                    AuthResponse.builder()
                            .error("INVALID_CREDENTIALS")
                            .build()
            );
        }

        User user = (User) auth.getPrincipal();
        String token = jwtService.generateToken(user);

        auditLogService.logLogin(user, true);

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .token(token)
                        .id(user.getId())
                        .username(user.getUsername())
                        .role(user.getRole().name())
                        .build()
        );
    }
}
