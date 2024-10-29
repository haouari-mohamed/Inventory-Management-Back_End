package com.example.backend.service;

import com.example.backend.model.Avancement;
import com.example.backend.model.Mission;
import com.example.backend.repository.AvancementRepository;
import com.example.backend.repository.MissionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvancementService {

    @Autowired
    private AvancementRepository avancementRepository;

    @Autowired
    private MissionRepository missionRepository;

    public List<Avancement> getAvancementsByMission(Long id_mission) {
        return avancementRepository.findAvancementsByMissionIdOrderByDateMiseAJourDesc(id_mission);
    }

    public Avancement saveAvancement(Avancement avancement) {
        Mission mission = missionRepository.findById(avancement.getMission().getId_mission())
                .orElseThrow(() -> new EntityNotFoundException("Mission not found"));

        avancement.setMission(mission);
        avancement.setDateMiseAJour(LocalDateTime.now());

        return avancementRepository.save(avancement);
    }


    @Transactional
    public Avancement updateAvancement(Long id_avancement, Avancement avancement) {
        Avancement existingAvancement = avancementRepository.findById(id_avancement)
                .orElseThrow(() -> new EntityNotFoundException("Avancement not found"));

        Double currentTotal = getCurrentTotalAvancement(existingAvancement.getMission().getId_mission());
        Double newTotal = currentTotal - existingAvancement.getMontantAvancement() + avancement.getMontantAvancement();

        if (newTotal > existingAvancement.getMission().getPrixMissionTotal()) {
            throw new IllegalArgumentException("The avancement amount exceeds the total mission amount");
        }

        existingAvancement.setMontantAvancement(avancement.getMontantAvancement());
        existingAvancement.setCommentaire(avancement.getCommentaire());
        existingAvancement.setDateMiseAJour(LocalDateTime.now());

        return avancementRepository.save(existingAvancement);
    }

    @Transactional
    public void deleteAvancement(Long id_avancement) {
        Avancement existingAvancement = avancementRepository.findById(id_avancement)
                .orElseThrow(() -> new EntityNotFoundException("Avancement not found"));

        avancementRepository.delete(existingAvancement);
    }

    public Double getCurrentTotalAvancement(Long id_mission) {
        return avancementRepository.sumMontantAvancementByMissionId(id_mission) != null
                ? avancementRepository.sumMontantAvancementByMissionId(id_mission)
                : 0.0;
    }

    public Double calculateAvancementPercentage(Long id_mission) {
        Mission mission = missionRepository.findById(id_mission)
                .orElseThrow(() -> new EntityNotFoundException("Mission not found"));

        Double currentTotal = getCurrentTotalAvancement(id_mission);

        if (mission.getPartMissionCID() != null && mission.getPartMissionCID() > 0) {
            return (currentTotal / mission.getPartMissionCID()) * 100;
        }
        return 0.0;
    }
}