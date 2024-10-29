package com.example.backend.repository;

import com.example.backend.DTO.MissionDivision2DTO;
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
//    @Query("SELECT md FROM MissionDivision md inner join Utilisateur u on u.division=md.division where u.id_utilisateur =:id and where md.mission.")
//    List<MissionDivision> findMissionDivisionByUtilisateurId(@Param("id") Long id,@Param("idf") Long idf);
//@Query("SELECT new com.example.backend.DTO.MissionDivision2DTO(md.mission.id_mission, md.mission.libelle_mission, md.partMission) " +
//        "FROM MissionDivision md INNER JOIN Utilisateur u ON u.division = md.division " +
//        "WHERE u.id_utilisateur = :id AND md.mission.affaire.idAffaire = :idf")
//List<MissionDivision2DTO> findMissionDivisionByUtilisateurId(@Param("id") Long id, @Param("idf") Long idf);

@Query("SELECT new com.example.backend.DTO.MissionDivision2DTO(md.mission.id_mission, md.mission.libelle_mission, md.partMission) " +
        "FROM MissionDivision md INNER JOIN Utilisateur u ON u.division = md.division " +
        "WHERE u.id_utilisateur = :id AND md.mission.affaire.idAffaire = :idf")
List<MissionDivision2DTO> findMissionDivisionByUtilisateurId(@Param("id") Long id, @Param("idf") Long idf);


}