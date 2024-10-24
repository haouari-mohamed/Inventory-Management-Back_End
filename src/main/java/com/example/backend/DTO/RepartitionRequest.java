package com.example.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RepartitionRequest {
    private Double principalDivisionPart;
    private Set<MissionDivisionDTO> secondaryDivisions;
    private Set<MissionPartenaireDTO> partenaires;
    private Set<MissionSTDTO> sousTraitants;


}
