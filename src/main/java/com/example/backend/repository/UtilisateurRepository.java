package com.example.backend.repository;

import com.example.backend.enums.Role;
import com.example.backend.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Utilisateur findByEmail(String email);
    Utilisateur findByUsername(String username);
    Utilisateur findByEmailIgnoreCaseOrUsernameIgnoreCase(String email, String username);
    List<Utilisateur> findByRole(Role role);
}
