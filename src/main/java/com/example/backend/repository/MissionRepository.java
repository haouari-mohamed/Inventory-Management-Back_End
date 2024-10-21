package com.example.backend.repository;

import com.example.backend.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
    List<Mission> findByAffaireIdAffaire(Long affaireId);

    @Query("SELECT DISTINCT m FROM Mission m " +
           "LEFT JOIN FETCH m.secondaryDivisions " +
           "LEFT JOIN FETCH m.sousTraitants " +
           "LEFT JOIN FETCH m.partenaires " +
           "WHERE m.id_mission = :id")
    Optional<Mission> findByIdWithDetails(@Param("id") Long id);
}
