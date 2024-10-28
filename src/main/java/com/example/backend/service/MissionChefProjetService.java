package com.example.backend.service;

import com.example.backend.model.MissionChefProjet;
import com.example.backend.repository.MissionChefProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MissionChefProjetService {
    @Autowired
    private MissionChefProjetRepository missionChefProjetRepository;

    public MissionChefProjet assignMissionToChefProjet(MissionChefProjet missionChefProjet){
        missionChefProjet.setDateAffectation(LocalDate.now());
        return missionChefProjetRepository.save(missionChefProjet);
    }

}
