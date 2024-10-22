package com.example.backend.model;

import com.example.backend.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "Utilisateur")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_utilisateur;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(length = 20)
    private String num_telephone;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = true, length = 255)
    private String password;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date_naissance;

    @Column(nullable = false, length = 1)
    private char sexe;

    @Column(nullable = false, length = 255)
    private String adresse;

    @ManyToOne
    @JoinColumn(name = "pole", nullable = true)
    private Pole pole;

    @ManyToOne
    @JoinColumn(name = "division", nullable = true)
    private Division division;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "pays")
    private Pays pays;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {

            System.out.println("Role is not initialized.");
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}

