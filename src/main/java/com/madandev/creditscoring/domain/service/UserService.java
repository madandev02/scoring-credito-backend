package com.madandev.creditscoring.domain.service;

import com.madandev.creditscoring.domain.entity.User;
import com.madandev.creditscoring.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        // Sino existe duplicado -> llamar a userRepository.save()
        return userRepository.save(user);

    }

        // method findById()
        public User findById (Long id){
            // Retorna el usuario si existe, si no existe lanza excepción
            return userRepository.findById(id)
                    .orElseThrow(() -> new IllegalStateException("Usuario no encontrado con ID: " + id));
        }

        // méthod findAll()
        public List<User> findAll () {
            // Retorna una lista de usuarios usando userRepository.findAll()
            return userRepository.findAll();
        }

        // method deleteById()
        public void deleteById (Long id){
            // Verificar que el usuario exista antes de borrar
            if (!userRepository.existsById(id)) {
                throw new IllegalStateException("No se puede eliminar. Usuario no encontrado con ID: " + id);
            }

            // Si existe -> llamar a userRepository.deleteById(id)
            userRepository.deleteById(id);

        }
    }