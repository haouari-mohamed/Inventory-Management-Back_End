package com.example.backend.controller;

import com.example.backend.model.Utilisateur;
import com.example.backend.service.UtilisateurService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

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

    @PostMapping
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

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailAvailability(@RequestParam String email) {
        boolean isAvailable = utilisateurService.checkEmailAvailability(email);
        return ResponseEntity.ok(isAvailable);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameAvailability(@RequestParam String username) {
        boolean isAvailable = utilisateurService.checkUsernameAvailability(username);
        return ResponseEntity.ok(isAvailable);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<Utilisateur> utilisateurOpt = utilisateurService.login(loginRequest.getIdentifier(), loginRequest.getPassword());

        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            return ResponseEntity.ok(new LoginResponse("Login successful", null, utilisateur.getId_utilisateur()));
        }

        return ResponseEntity.badRequest().body("Invalid credentials");
    }

    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestParam String identifier) {
        Utilisateur user = utilisateurService.getUserDetails(identifier);
        if (user != null) {
            Map<String, String> userDetails = new HashMap<>();
            userDetails.put("firstName", user.getPrenom());
            userDetails.put("lastName", user.getNom());
            userDetails.put("email", user.getEmail());
            return ResponseEntity.ok(userDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/chefs-de-projet")
    public ResponseEntity<List<Utilisateur>> getChefsDeProjet() {
        List<Utilisateur> chefsDeProjet = utilisateurService.getChefsDeProjet();
        return ResponseEntity.ok(chefsDeProjet);
    }
}

@Data
class LoginRequest {
    private String identifier; // This can be either email or username
    private String password;
}

@Data
class LoginResponse {
    private String message;
    private String redirectionLink;
    private Long id_utilisateur;

    public LoginResponse(String message, String redirectionLink, Long id_utilisateur) {
        this.message = message;
        this.redirectionLink = redirectionLink;
        this.id_utilisateur = id_utilisateur;
    }
}
