package com.madandev.creditscoring.domain.service;

import com.madandev.creditscoring.domain.dto.ClientListResponse;
import com.madandev.creditscoring.domain.entity.User;
import com.madandev.creditscoring.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ============================
    // CREAR USUARIO
    // ============================
    public User createUser(User user) {

        // Verificar username repetido
        Optional<User> existingUserByUsername = userRepository.findByUsername(user.getUsername());
        if (existingUserByUsername.isPresent()) {
            throw new IllegalStateException("Ya existe un usuario con el username: " + user.getUsername());
        }

        // Verificar email repetido
        Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserByEmail.isPresent()) {
            throw new IllegalStateException("Ya existe un usuario con el email: " + user.getEmail());
        }

        // Verificar RUT repetido
        if (user.getRut() != null) {
            Optional<User> existingByRut = userRepository.findByRut(user.getRut());
            if (existingByRut.isPresent()) {
                throw new IllegalStateException("Ya existe un usuario con el RUT: " + user.getRut());
            }
        }

        // Encriptar contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // ============================
    // OBTENER POR ID
    // ============================
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado con ID: " + id));
    }

    // ============================
    // LISTAR TODOS
    // ============================
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // ============================
    // ELIMINAR USUARIO
    // ============================
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalStateException("No se puede eliminar. Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    // ============================
    // LISTAR SOLO CLIENTES (DTO)
    // ============================
    public List<ClientListResponse> findAllClients() {

        return userRepository.findAll().stream()
                .filter(u -> u.getRole().name().equals("CLIENT"))
                .map(u -> {

                    Integer latestScore = null;
                    String riskLevel = null; // POR AHORA null hasta implementar motor
                    String status = "Pendiente";

                    // Obtener último score_value
                    if (u.getScoreHistory() != null && !u.getScoreHistory().isEmpty()) {
                        var last = u.getScoreHistory()
                                .stream()
                                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                                .findFirst()
                                .get();

                        latestScore = last.getScoreValue();
                        status = "Evaluado";
                    }

                    return ClientListResponse.builder()
                            .id(u.getId())
                            .rut(u.getRut())
                            .firstName(u.getFirstName())
                            .lastName(u.getLastName())
                            .username(u.getUsername())
                            .role(u.getRole().name())
                            .latestScore(latestScore)
                            .riskLevel(riskLevel)
                            .status(status)
                            .build();
                })
                .toList();
    }
}
