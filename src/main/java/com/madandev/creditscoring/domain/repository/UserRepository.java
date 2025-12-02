package com.madandev.creditscoring.domain.repository;

import com.madandev.creditscoring.domain.entity.User;
import com.madandev.creditscoring.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByRut(String rut);

    List<User> findByRole(Role role);

    boolean existsById(Long id);
}
