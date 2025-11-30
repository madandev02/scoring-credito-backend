package com.madandev.creditscoring.domain.service;

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

    // method createUser()
    public User createUser(User user) {

        // Verificar si existe un usuario con el mismo username
        Optional<User> existingUserByUsername = userRepository.findByUsername(user.getUsername());
        if (existingUserByUsername.isPresent()) {
            throw new IllegalStateException("Ya existe un usuario con el username: " + user.getUsername());
        }

        // Verificar si existe un usuario con el mismo email
        Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserByEmail.isPresent()) {
            throw new IllegalStateException("Ya existe un usuario con el email: " + user.getEmail());
        }

        // 🔑 IMPORTANTE: encriptar contraseña ANTES de guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // method findById()
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado con ID: " + id));
    }

    // method findAll()
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // method deleteById()
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalStateException("No se puede eliminar. Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
