package com.example.backend.repository;

import com.example.backend.model.Mission;
import com.example.backend.model.MissionDivision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionDivisionRepository extends JpaRepository<MissionDivision, Long> {
    void deleteAllByMission(Mission mission); // Method to delete divisions by mission
    @Query("SELECT md FROM MissionDivision md inner join Utilisateur u on u.division=md.division where u.id_utilisateur =:id")
    List<MissionDivision> findMissionDivisionByUtilisateurId(@Param("id") Long id);

}