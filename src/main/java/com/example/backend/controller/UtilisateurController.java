package com.example.backend.controller;


import com.example.backend.DTO.LoginRequestDTO;
import com.example.backend.config.JwtAuth;
import com.example.backend.enums.Role;
import com.example.backend.model.Utilisateur;
import com.example.backend.service.UtilisateurService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Long id) {
        return utilisateurService.getUtilisateurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/registre")
    public ResponseEntity<?> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur savedUtilisateur = utilisateurService.createUtilisateur(utilisateur);
            return ResponseEntity.ok(savedUtilisateur);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error creating user: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur updatedUtilisateur = utilisateurService.updateUtilisateur(id, utilisateur);
            return ResponseEntity.ok(updatedUtilisateur);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUtilisateur(@PathVariable Long id) {
        try {
            utilisateurService.deleteUtilisateur(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody LoginRequestDTO loginRequestDTO) {
        System.out.println("//////////:::"+loginRequestDTO.getUsername()+"///::"+loginRequestDTO.getPassword());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
        );
        Utilisateur utilisateur = utilisateurService.findByUsername(loginRequestDTO.getUsername());
        Role role= utilisateur.getRole();
        String token = JwtAuth.generateToken(loginRequestDTO.getUsername(),role);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", utilisateur.getId_utilisateur());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chefs-de-projet")
    public ResponseEntity<List<Utilisateur>> getChefsDeProjet() {
        List<Utilisateur> chefsDeProjet = utilisateurService.getChefsDeProjet();
        return ResponseEntity.ok(chefsDeProjet);
    }
}


