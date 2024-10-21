package com.example.backend.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissionDivisionDTO {
    private Long id;
    private Long missionId;
    private Long divisionId;
    private DivisionDTO division;
    private Double partMission;
}