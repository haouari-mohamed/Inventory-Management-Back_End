package com.example.backend.repository;

import com.example.backend.enums.Role;
import com.example.backend.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Utilisateur findByEmail(String email);
    Utilisateur findByUsername(String username);
    Utilisateur findByEmailIgnoreCaseOrUsernameIgnoreCase(String email, String username);
    List<Utilisateur> findByRole(Role role);
    @Query("SELECT u.id_utilisateur from Utilisateur u where u.username=:username")
    Integer findIdByUsername(@Param("username") String username);

    @Query("SELECT u FROM Utilisateur u " +
            "WHERE u.division.id_division = (SELECT d.id_division FROM Utilisateur dir JOIN dir.division d WHERE dir.id_utilisateur = :id) " +
            "AND u.role = :role")
    List<Utilisateur> findChefsDeProjetByDivision(@Param("id") Long id, @Param("role") Role role);

}
