package com.example.backend.repository;

import com.example.backend.model.Mission;
import com.example.backend.model.MissionPartenaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionPartenaireRepository extends JpaRepository<MissionPartenaire, Long> {
    @Modifying
    @Query("DELETE FROM MissionPartenaire mp WHERE mp.mission = :mission")
    void deleteAllByMission(Mission mission);
}