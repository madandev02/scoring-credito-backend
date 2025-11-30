package com.madandev.creditscoring.domain.entity;

import com.madandev.creditscoring.security.model.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Login único
    @Column(nullable = false, unique = true, length = 255)
    private String username;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role;

    private String firstName;
    private String lastName;

    private Instant createdAt;
    private Instant updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private FinancialData financialData;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ScoreHistory> scoreHistory = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CreditRequest> creditRequests = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // ====== Métodos de UserDetails (Spring Security) ======

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // podrías añadir lógica más avanzada después
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // podrías añadir flags de bloqueo
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // podrías expirar credenciales si quieres
    }

    @Override
    public boolean isEnabled() {
        return true; // podrías tener un campo "enabled"
    }
}
