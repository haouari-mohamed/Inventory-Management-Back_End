package com.example.backend.controller;

import com.example.backend.model.Mission;
import com.example.backend.model.MissionChefProjet;
import com.example.backend.repository.MissionRepository;
import com.example.backend.service.MissionChefProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/mission/chefprojet")
public class MissionChefProjetController {
    @Autowired
    private MissionChefProjetService missionChefProjetService;
    @Autowired
    private MissionRepository missionRepository;

    @PostMapping
    public MissionChefProjet assignerMission(@RequestBody MissionChefProjet missionChefProjet) {
        System.out.println("chef de peojet id"+missionChefProjet.getChefProjet().getId_utilisateur());
        return missionChefProjetService.assignMissionToChefProjet(missionChefProjet);
    }
//    public MissionChefProjet assignMissionToChef(@RequestBody MissionChefProjet missionChefProjet) {
//        Mission mission = missionRepository.findById(missionChefProjet.getMission().getId_mission())
//                .orElseThrow(() -> new IllegalArgumentException("Mission non trouvée"));
//        missionChefProjet.setMission(mission);
//
//        return missionChefProjetService.assignMissionToChefProjet(missionChefProjet);
//    }
}
