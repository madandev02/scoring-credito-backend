package com.madandev.creditscoring.domain.repository;

import com.madandev.creditscoring.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    // 👇 NUEVO
    Optional<User> findByRut(String rut);

    boolean existsById(Long id);
}
