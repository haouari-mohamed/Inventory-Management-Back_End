package com.example.backend.repository;

import com.example.backend.model.Mission;
import com.example.backend.model.MissionDivision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionDivisionRepository extends JpaRepository<MissionDivision, Long> {
    void deleteAllByMission(Mission mission); // Method to delete divisions by mission
}