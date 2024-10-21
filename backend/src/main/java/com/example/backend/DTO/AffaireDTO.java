package com.example.backend.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffaireDTO {
    private Long idAffaire;
    private String libelle_affaire;
    private Double prixGlobal;
    private String statusAffaire;
    private String marche;
    private Date dateDebut;
    private Date dateFin;
    private Date dateArret;
    private Date dateRecommencement;
    private Long clientId;
    private Long polePrincipaleId;
    private Long divisionPrincipaleId;
    private Double partCID;
    private Long chefProjetId;
}