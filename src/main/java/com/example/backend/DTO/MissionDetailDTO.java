package com.example.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MissionDetailDTO {
    private Long idMission;
    private String libelleMission;
    private String username;
    private String nomDivision;
    private Double partMission;
}
