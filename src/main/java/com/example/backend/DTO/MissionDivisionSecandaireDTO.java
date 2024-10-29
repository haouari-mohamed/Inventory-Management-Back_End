package com.example.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionDivisionSecandaireDTO {
    private Long id_mission;
    private String libelle_mission;
    private Date dateDebut;
    private Date dateFin;
    private Double partMission;
}
