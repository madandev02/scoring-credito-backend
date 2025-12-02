package com.madandev.creditscoring.controller;

import com.madandev.creditscoring.domain.dto.ClientCreateRequest;
import com.madandev.creditscoring.domain.dto.ClientListResponse;
import com.madandev.creditscoring.domain.entity.User;
import com.madandev.creditscoring.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ============================
    // USUARIOS INTERNOS (ADMIN / ANALYST / ETC.)
    // ============================

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ============================
    // CLIENTES (role = CLIENT)
    // ============================

    @GetMapping("/clients")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClientListResponse>> listClients() {
        return ResponseEntity.ok(userService.listClients());
    }

    @PostMapping("/clients")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientListResponse> createClient(
            @Valid @RequestBody ClientCreateRequest request
    ) {
        ClientListResponse created = userService.createClient(request);
        return ResponseEntity.ok(created);
    }
}
