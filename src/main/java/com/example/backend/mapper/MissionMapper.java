package com.example.backend.mapper;

import com.example.backend.DTO.MissionDTO;
import com.example.backend.model.Mission;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MissionMapper {

    /**
     * Convertit une entité Mission en MissionDTO
     * @param mission L'entité Mission à convertir
     * @return MissionDTO
     */
    public MissionDTO toDTO(Mission mission) {
        if (mission == null) {
            return null;
        }

        MissionDTO dto = new MissionDTO();

        // Informations de base
        dto.setId_mission(mission.getId_mission());
        dto.setLibelle_mission(mission.getLibelle_mission());
        dto.setQuantite(mission.getQuantite());
        dto.setPrixMissionTotal(mission.getPrixMissionTotal());
        dto.setPrixMissionUnitaire(mission.getPrixMissionUnitaire());
        dto.setPartMissionCID(mission.getPartMissionCID());
        dto.setCompteClient(mission.getCompteClient());

        // Conversion des dates avec vérification null
        if (mission.getDateDebut() != null) {
            dto.setDateDebut(mission.getDateDebut().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }

        if (mission.getDateFin() != null) {
            dto.setDateFin(mission.getDateFin().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }

        if (mission.getDateArret() != null) {
            dto.setDateArret(mission.getDateArret().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }

        if (mission.getDateRecommencement() != null) {
            dto.setDateRecommencement(mission.getDateRecommencement().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }

        // Relations
        if (mission.getAffaire() != null) {
            dto.setAffaireId(mission.getAffaire().getIdAffaire());
        }

        if (mission.getPrincipalDivision() != null) {
            dto.setPrincipalDivisionId(mission.getPrincipalDivision().getId_division());
        }

        // Initialisation explicite des collections à null
        dto.setPrincipalDivision(null);
        dto.setMissionDivisions(null);
        dto.setMissionSousTraitants(null);
        dto.setMissionPartenaires(null);

        return dto;
    }

    /**
     * Convertit un MissionDTO en entité Mission
     * @param dto Le MissionDTO à convertir
     * @return Mission
     */
    public Mission toEntity(MissionDTO dto) {
        if (dto == null) {
            return null;
        }

        Mission mission = new Mission();

        // Informations de base
        mission.setId_mission(dto.getId_mission());
        mission.setLibelle_mission(dto.getLibelle_mission());
        mission.setQuantite(dto.getQuantite());
        mission.setPrixMissionTotal(dto.getPrixMissionTotal());
        mission.setPrixMissionUnitaire(dto.getPrixMissionUnitaire());
        mission.setPartMissionCID(dto.getPartMissionCID());
        mission.setCompteClient(dto.getCompteClient());

        // Conversion des dates avec vérification null
        if (dto.getDateDebut() != null) {
            mission.setDateDebut(Date.from(dto.getDateDebut()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()));
        }

        if (dto.getDateFin() != null) {
            mission.setDateFin(Date.from(dto.getDateFin()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()));
        }

        if (dto.getDateArret() != null) {
            mission.setDateArret(Date.from(dto.getDateArret()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()));
        }

        if (dto.getDateRecommencement() != null) {
            mission.setDateRecommencement(Date.from(dto.getDateRecommencement()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()));
        }

        // Les relations seront gérées par le service
        mission.setAffaire(null);
        mission.setPrincipalDivision(null);

        return mission;
    }


    public List<MissionDTO> toDTOList(List<Mission> missions) {
        if (missions == null) {
            return null;
        }
        return missions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}