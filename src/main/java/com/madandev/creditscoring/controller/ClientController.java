package com.madandev.creditscoring.controller;

import com.madandev.creditscoring.domain.dto.ClientCreateRequest;
import com.madandev.creditscoring.domain.dto.ClientDetailResponse;
import com.madandev.creditscoring.domain.dto.ClientListResponse;
import com.madandev.creditscoring.domain.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    // LISTA DE CLIENTES
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ANALYST','ROLE_OPERATOR','ROLE_AUDITOR','ROLE_SUPPORT')")
    public ResponseEntity<List<ClientListResponse>> listClients() {
        return ResponseEntity.ok(clientService.listClients());
    }

    // DETALLE CLIENTE
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ANALYST','ROLE_OPERATOR','ROLE_AUDITOR','ROLE_SUPPORT')")
    public ResponseEntity<ClientDetailResponse> getClient(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientDetail(id));
    }

    // CREAR CLIENTE
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ANALYST')")
    public ResponseEntity<ClientDetailResponse> createClient(
            @Valid @RequestBody ClientCreateRequest request
    ) {
        return ResponseEntity.ok(clientService.createClient(request));
    }
}
