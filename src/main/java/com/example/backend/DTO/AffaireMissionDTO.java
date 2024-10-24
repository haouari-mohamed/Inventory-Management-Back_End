package com.example.backend.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AffaireMissionDTO {

    private String libelleAffaire;
    private String dateDebut;
    private String dateFin;
    private Double partCID;


    private String libelleMission;
    private String nomDivisionPrincipale;
    private Double partDivisionPrincipale;


    private String divisionSecondaireNom;
    private Double partDivisionSecondaire;


    private String sousTraitantNom;
    private Double partSousTraitant;


    private String partenaireNom;
    private Double partPartenaire;
}
