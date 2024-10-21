package com.example.backend.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissionPartenaireDTO {
    private Long id;
    private Long missionId;
    private Long partenaireId;
    private PartenaireDTO partenaire;
    private Double partMission;
}