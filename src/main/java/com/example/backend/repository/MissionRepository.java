package com.example.backend.repository;

import com.example.backend.DTO.MissionDTO;
import com.example.backend.model.Mission;
import com.example.backend.model.MissionDivision;
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

//    @Query("SELECT a.libelle_affaire, a.dateDebut, a.dateFin, a.partCID, " +
//            "m.libelle_mission, m.principalDivision.nom_division, m.partDivPrincipale, " +
//            "sd.division.nom_division AS divisionSecondaireNom, sd.partMission AS partDivisionSecondaire, " +
//            "st.sousTraitant.nom_soustrait AS sousTraitantNom, st.partMission AS partSousTraitant, " +
//            "p.partenaire.nom_partenaire AS partenaireNom, p.partMission AS partPartenaire " +
//            "FROM Affaire a " +
//            "INNER JOIN Mission m ON m.affaire.idAffaire = a.idAffaire " +
//            "LEFT JOIN MissionDivision sd ON sd.mission.id_mission = m.id_mission " +
//            "LEFT JOIN MissionST st ON st.mission.id_mission = m.id_mission " +
//            "LEFT JOIN MissionPartenaire p ON p.mission.id_mission = m.id_mission " +
//            "WHERE (m.partDivPrincipale + " +
//            "COALESCE(sd.partMission, 0) + " +
//            "COALESCE(st.partMission, 0) + " +
//            "COALESCE(p.partMission, 0)) <= a.partCID")
//    List<Object[]> findAffairesWithMissionsAndDivisions();
@Query("SELECT a.libelle_affaire, a.dateDebut, a.dateFin, a.partCID, " +
        "m.libelle_mission, m.principalDivision.nom_division, m.partDivPrincipale, " +
        "sd.division.nom_division AS divisionSecondaireNom, sd.partMission AS partDivisionSecondaire, " +
        "st.sousTraitant.nom_soustrait AS sousTraitantNom, st.partMission AS partSousTraitant, " +
        "p.partenaire.nom_partenaire AS partenaireNom, p.partMission AS partPartenaire " +
        "FROM Affaire a " +
        "INNER JOIN Mission m ON m.affaire.idAffaire = a.idAffaire " +
        "LEFT JOIN MissionDivision sd ON sd.mission.id_mission = m.id_mission " +
        "LEFT JOIN MissionST st ON st.mission.id_mission = m.id_mission " +
        "LEFT JOIN MissionPartenaire p ON p.mission.id_mission = m.id_mission " +
        "WHERE m.principalDivision.id_division = (SELECT d.id_division FROM Utilisateur cd JOIN cd.division d WHERE cd.id_utilisateur = :idChefDivision)" +
        "AND (m.partDivPrincipale + " +
        "COALESCE(sd.partMission, 0) + " +
        "COALESCE(st.partMission, 0) + " +
        "COALESCE(p.partMission, 0)) <= a.partCID")
List<Object[]> findAffairesWithMissionsAndDivisionsByChefDivision(@Param("idChefDivision") Long idChefDivision);


}
