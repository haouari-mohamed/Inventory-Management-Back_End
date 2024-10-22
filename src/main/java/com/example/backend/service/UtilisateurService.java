package com.example.backend.service;

import com.example.backend.enums.Role;
import com.example.backend.model.Utilisateur;
import com.example.backend.model.Pays;
import com.example.backend.repository.UtilisateurRepository;
import com.example.backend.repository.PaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PaysRepository paysRepository;

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id);
    }

    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur updateUtilisateur(Long id, Utilisateur utilisateur) {
        return utilisateurRepository.findById(id)
                .map(existingUtilisateur -> {
                    existingUtilisateur.setEmail(utilisateur.getEmail());
                    existingUtilisateur.setNum_telephone(utilisateur.getNum_telephone());
                    existingUtilisateur.setUsername(utilisateur.getUsername());
                    existingUtilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
                    existingUtilisateur.setDate_naissance(utilisateur.getDate_naissance());
                    existingUtilisateur.setSexe(utilisateur.getSexe());
                    existingUtilisateur.setAdresse(utilisateur.getAdresse());
                    existingUtilisateur.setRole(utilisateur.getRole());

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

                    return utilisateurRepository.save(existingUtilisateur);
                })
                .orElseThrow(() -> new RuntimeException("Utilisateur not found with id: " + id));
    }

    public void deleteUtilisateur(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur not found with id: " + id));
        utilisateurRepository.delete(utilisateur);
    }

    public List<Utilisateur> getChefsDeProjet() {
       return utilisateurRepository.findByRole(Role.CHEF_PROJET);
    }
    public Utilisateur findByUsername(String username){
        return utilisateurRepository.findByUsername(username);
    }
}
