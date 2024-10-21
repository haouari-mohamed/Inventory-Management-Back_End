package com.example.backend.service;

import com.example.backend.model.Role;
import com.example.backend.model.Utilisateur;
import com.example.backend.model.Pays;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UtilisateurRepository;
import com.example.backend.repository.PaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PaysRepository paysRepository;

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id);
    }

    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur updateUtilisateur(Long id, Utilisateur utilisateur) {
        return utilisateurRepository.findById(id)
                .map(existingUtilisateur -> {
                    existingUtilisateur.setPrenom(utilisateur.getPrenom());
                    existingUtilisateur.setNom(utilisateur.getNom());
                    existingUtilisateur.setEmail(utilisateur.getEmail());
                    existingUtilisateur.setNum_telephone(utilisateur.getNum_telephone());
                    existingUtilisateur.setUsername(utilisateur.getUsername());
                    existingUtilisateur.setMot_de_passe(utilisateur.getMot_de_passe());
                    existingUtilisateur.setDate_naissance(utilisateur.getDate_naissance());
                    existingUtilisateur.setSexe(utilisateur.getSexe());
                    existingUtilisateur.setAdresse(utilisateur.getAdresse());

                    // Handle Pole and Division
                    existingUtilisateur.setPole(utilisateur.getPole());
                    existingUtilisateur.setDivision(utilisateur.getDivision());

                    // Handle Pays
                    if (utilisateur.getPays() != null) {
                        Long paysId = utilisateur.getPays().getId_pays();
                        if (paysId != null) {
                            Pays pays = paysRepository.findById(paysId)
                                    .orElseThrow(() -> new RuntimeException("Country not found with id: " + paysId));
                            existingUtilisateur.setPays(pays);
                        } else {
                            existingUtilisateur.setPays(null);
                        }
                    } else {
                        existingUtilisateur.setPays(null);
                    }

                    // Handle Roles
                    if (utilisateur.getRoles() != null) {
                        Set<Role> updatedRoles = new HashSet<>();
                        utilisateur.getRoles().forEach(role -> {
                            if (role != null && role.getId_role() != null) {
                                Role existingRole = roleRepository.findById(role.getId_role())
                                        .orElseThrow(() -> new RuntimeException("Role not found with id: " + role.getId_role()));
                                updatedRoles.add(existingRole);
                            }
                        });
                        existingUtilisateur.setRoles(updatedRoles);
                    } else {
                        existingUtilisateur.setRoles(null);
                    }

                    return utilisateurRepository.save(existingUtilisateur);
                })
                .orElseThrow(() -> new RuntimeException("Utilisateur not found with id: " + id));
    }

    public void deleteUtilisateur(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur not found with id: " + id));
        utilisateurRepository.delete(utilisateur);
    }

    public boolean checkEmailAvailability(String email) {
        return !utilisateurRepository.findByEmail(email).isPresent();
    }

    public boolean checkUsernameAvailability(String username) {
        return !utilisateurRepository.findByUsername(username).isPresent();
    }

    public Optional<Utilisateur> login(String identifier, String password) {
        String lowercaseIdentifier = identifier.trim().toLowerCase();
        Utilisateur utilisateur = utilisateurRepository.findByEmailIgnoreCaseOrUsernameIgnoreCase(lowercaseIdentifier, lowercaseIdentifier)
                .orElse(null);

        if (utilisateur != null && utilisateur.getMot_de_passe().equals(password)) {
            return Optional.of(utilisateur);
        }
        return Optional.empty();
    }

    public Utilisateur getUserDetails(String identifier) {
        return utilisateurRepository.findByEmailIgnoreCaseOrUsernameIgnoreCase(identifier, identifier)
                .orElse(null);
    }

    public List<Utilisateur> getChefsDeProjet() {
        return utilisateurRepository.findAll().stream()
                .filter(utilisateur -> utilisateur.getRoles().stream()
                        .anyMatch(role -> role.getNom_role().equalsIgnoreCase("Chef de Projet")))
                .collect(Collectors.toList());
    }
}
