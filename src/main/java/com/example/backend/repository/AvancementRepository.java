package com.example.backend.repository;

import com.example.backend.model.Avancement;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


    @Repository
    public interface AvancementRepository extends JpaRepository<Avancement, Long> {
        @Query("SELECT a FROM Avancement a WHERE a.mission.id_mission = :id_mission ORDER BY a.dateMiseAJour DESC")
        List<Avancement> findAvancementsByMissionIdOrderByDateMiseAJourDesc(@Param("id_mission") Long id_mission);


        @Query("SELECT SUM(a.montantAvancement) FROM Avancement a WHERE a.mission.id_mission = :missionId")
        Double sumMontantAvancementByMissionId(@Param("missionId") Long missionId);

}