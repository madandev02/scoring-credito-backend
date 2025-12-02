package com.madandev.creditscoring.domain.service;

import com.madandev.creditscoring.domain.dto.ClientCreateRequest;
import com.madandev.creditscoring.domain.dto.ClientListResponse;
import com.madandev.creditscoring.domain.entity.ScoreHistory;
import com.madandev.creditscoring.domain.entity.User;
import com.madandev.creditscoring.domain.repository.ScoreHistoryRepository;
import com.madandev.creditscoring.domain.repository.UserRepository;
import com.madandev.creditscoring.security.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ScoreHistoryRepository scoreHistoryRepository;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            ScoreHistoryRepository scoreHistoryRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.scoreHistoryRepository = scoreHistoryRepository;
    }

    // ============================
    // USUARIOS INTERNOS (ADMIN / ANALYST)
    // ============================
    public User createUser(User user) {
        // username único
        userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new IllegalStateException("Ya existe un usuario con el username: " + user.getUsername());
        });

        // email único
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new IllegalStateException("Ya existe un usuario con el email: " + user.getEmail());
        });

        // RUT único si viene
        if (user.getRut() != null && !user.getRut().isBlank()) {
            userRepository.findByRut(user.getRut()).ifPresent(u -> {
                throw new IllegalStateException("Ya existe un usuario con el RUT: " + user.getRut());
            });
        }

        // Encriptar password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // ============================
    // CLIENTES (role = CLIENT)
    // ============================
    public ClientListResponse createClient(ClientCreateRequest request) {
        // username obligatorio o generado desde RUT
        String username = (request.username() == null || request.username().isBlank())
                ? generarUsernameDesdeRut(request.rut())
                : request.username();

        // Validar duplicados
        userRepository.findByUsername(username).ifPresent(u -> {
            throw new IllegalStateException("Ya existe un usuario con el username: " + username);
        });

        userRepository.findByEmail(request.email()).ifPresent(u -> {
            throw new IllegalStateException("Ya existe un usuario con el email: " + request.email());
        });

        if (request.rut() != null && !request.rut().isBlank()) {
            userRepository.findByRut(request.rut()).ifPresent(u -> {
                throw new IllegalStateException("Ya existe un usuario con el RUT: " + request.rut());
            });
        }

        // Creamos el User con role CLIENT y password dummy (no lo usará para loguearse)
        User client = User.builder()
                .username(username)
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .rut(request.rut())
                .role(Role.CLIENT)
                .password(passwordEncoder.encode("Temporal123!"))
                .build();

        User saved = userRepository.save(client);

        return ClientListResponse.builder()
                .id(saved.getId())
                .rut(saved.getRut())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .username(saved.getUsername())
                .role(saved.getRole().name())
                .latestScore(null)
                .riskLevel("SIN EVALUAR")
                .status("PENDIENTE")
                .build();
    }

    private String generarUsernameDesdeRut(String rut) {
        if (rut == null) {
            return "cliente_" + System.currentTimeMillis();
        }
        return rut.replace(".", "").replace("-", "");
    }

    // ============================
    // CRUD BÁSICO
    // ============================

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado con ID: " + id));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalStateException("No se puede eliminar. Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    // ============================
    // LISTADO DE CLIENTES (ROLE CLIENT)
    // ============================

    public List<ClientListResponse> listClients() {
        List<User> clients = userRepository.findByRole(Role.CLIENT);

        return clients.stream()
                .map(this::mapToClientListResponse)
                .toList();
    }

    private ClientListResponse mapToClientListResponse(User u) {
        // Último score del cliente (si existe)
        Optional<ScoreHistory> lastScoreOpt =
                scoreHistoryRepository.findTopByUserIdOrderByCreatedAtDesc(u.getId());

        Integer latestScore = lastScoreOpt.map(ScoreHistory::getScoreValue).orElse(null);
        String riskLevel = calcularNivelRiesgo(latestScore);
        String status = (latestScore == null) ? "PENDIENTE" : "EVALUADO";

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
    }

    private String calcularNivelRiesgo(Integer score) {
        if (score == null) {
            return "SIN EVALUAR";
        }
        if (score >= 750) return "BAJO";
        if (score >= 600) return "MEDIO";
        return "ALTO";
    }
}
