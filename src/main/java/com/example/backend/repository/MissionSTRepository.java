package com.example.backend.repository;

import com.example.backend.model.Mission;
import com.example.backend.model.MissionST;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionSTRepository extends JpaRepository<MissionST, Long> {
    @Modifying
    @Query("DELETE FROM MissionST mst WHERE mst.mission = :mission")
    void deleteAllByMission(Mission mission);
}