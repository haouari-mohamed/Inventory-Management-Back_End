package com.example.backend.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDTO {
    private Long id_utilisateur;
    private String prenom;
    private String nom;
    private String email;
    private String num_telephone;
    private String username;
    private Date date_naissance;
    private char sexe;
    private String adresse;
    private boolean isDeleted;
    private Long poleId;
    private Long divisionId;
    private Set<Long> roleIds;
    private Long paysId;
}