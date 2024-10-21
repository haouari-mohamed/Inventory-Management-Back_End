package com.example.backend.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissionDTO {
    private Long id_mission;
    private String libelle_mission;
    private Integer quantite;
    private Long uniteId;
    private Double prixMissionTotal;
    private Double prixMissionUnitaire;
    private Double partMissionCID;
    private Double compteClient;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private LocalDate dateArret;
    private LocalDate dateRecommencement;
    private Long affaireId;
    private Long principalDivisionId;
    private DivisionDTO principalDivision;
    private Double partDivPrincipale;
    private List<MissionDivisionDTO> MissionDivisions;
    private List<MissionSTDTO> MissionSousTraitants;
    private List<MissionPartenaireDTO> MissionPartenaires;
}