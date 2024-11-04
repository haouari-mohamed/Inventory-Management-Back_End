package com.example.backend.repository;

import com.example.backend.model.Facturation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FacturationRepository extends JpaRepository<Facturation, Long> {
//    List<Facturation> findByMission_Id_mission(Long id);
    @Query("select f from Facturation f where f.mission.id_mission =:id")
    List<Facturation> getFacturationMissionId(@Param("id")Long id);

    @Query("select f from Facturation f where f.documentFacture = :documentName")
    Facturation findByDocumentFacture(@Param("documentName") String documentName);

    Optional<Facturation> findById(Long id_facture);

}
