package com.madandev.creditscoring.domain.repository;

import com.madandev.creditscoring.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Method to find byUsername
    Optional<User> findByUsername(String username);

    // Method to find byEmail
    Optional<User> findByEmail(String email);

    // Method to verify existence by id
    boolean existsById(Long id);
}