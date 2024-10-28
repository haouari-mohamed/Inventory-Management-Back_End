package com.example.backend.controller;

import com.example.backend.DTO.*;
import com.example.backend.mapper.MissionMapper;
import com.example.backend.model.*;
import com.example.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/missions")
@CrossOrigin(origins = "http://localhost:3000")
public class MissionController {

    private static final Logger logger = LoggerFactory.getLogger(MissionController.class);

    @Autowired
    private DivisionRepository divisionRepository;
    @Autowired
    private MissionMapper missionMapper;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private AffaireRepository affaireRepository;

    @Autowired
    private UniteRepository uniteRepository;

    @Autowired
    private MissionDivisionRepository missionDivisionRepository;

    @Autowired
    private MissionSTRepository missionSTRepository;

    @Autowired
    private MissionPartenaireRepository missionPartenaireRepository;

    @Autowired
    private PartenaireRepository partenaireRepository;

    @Autowired
    private SousTraitantRepository sousTraitantRepository;



    @PostMapping
    public ResponseEntity<?> createMission(@RequestBody Mission mission) {
        try {
            // Validate Unite
            if (mission.getUnite() == null || mission.getUnite().getId_unite() == null) {
                return ResponseEntity.badRequest().body("Unite is required");
            }
            Unite unite = uniteRepository.findById(mission.getUnite().getId_unite())
                    .orElseThrow(() -> new RuntimeException("Unite not found"));
            mission.setUnite(unite);

            // Validate Affaire
            if (mission.getAffaire() == null || mission.getAffaire().getIdAffaire() == null) {
                return ResponseEntity.badRequest().body("Affaire is required");
            }
            Affaire affaire = affaireRepository.findById(mission.getAffaire().getIdAffaire())
                    .orElseThrow(() -> new RuntimeException("Affaire not found"));
            mission.setAffaire(affaire);

            // Validate Principal Division
            if (mission.getPrincipalDivision() == null || mission.getPrincipalDivision().getId_division() == null) {
                return ResponseEntity.badRequest().body("Principal Division is required");
            }
            Division principalDivision = divisionRepository.findById(mission.getPrincipalDivision().getId_division())
                    .orElseThrow(() -> new RuntimeException("Principal Division not found"));
            mission.setPrincipalDivision(principalDivision);

            Mission savedMission = missionRepository.save(mission);
            return ResponseEntity.ok(savedMission);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error creating mission: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMission(@PathVariable Long id, @RequestBody Mission mission) {
        try {
            return missionRepository.findById(id)
                .map(existingMission -> {
                    // Update basic fields
                    existingMission.setLibelle_mission(mission.getLibelle_mission());
                    existingMission.setPrixMissionTotal(mission.getPrixMissionTotal());
                    existingMission.setPartMissionCID(mission.getPartMissionCID());
                    
                    // Handle Unite relationship
                    if (mission.getUnite() != null && mission.getUnite().getId_unite() != null) {
                        Unite unite = uniteRepository.findById(mission.getUnite().getId_unite())
                                .orElseThrow(() -> new RuntimeException("Unite not found"));
                        existingMission.setUnite(unite);
                    }

                    // Handle quantite and prixMissionUnitaire
                    if (mission.getUnite().getId_unite() != 10) { // Assuming 10 is the ID for "forfait"
                        existingMission.setQuantite(mission.getQuantite());
                        existingMission.setPrixMissionUnitaire(mission.getPrixMissionUnitaire());
                    } else {
                        existingMission.setQuantite(0);
                        existingMission.setPrixMissionUnitaire(null);
                    }

                    // Handle Principal Division
                    if (mission.getPrincipalDivision() != null && mission.getPrincipalDivision().getId_division() != null) {
                        Division principalDivision = divisionRepository.findById(mission.getPrincipalDivision().getId_division())
                                .orElseThrow(() -> new RuntimeException("Principal Division not found"));
                        existingMission.setPrincipalDivision(principalDivision);
                    }

                    // Handle Affaire
                    if (mission.getAffaire() != null && mission.getAffaire().getIdAffaire() != null) {
                        Affaire affaire = affaireRepository.findById(mission.getAffaire().getIdAffaire())
                                .orElseThrow(() -> new RuntimeException("Affaire not found"));
                        existingMission.setAffaire(affaire);
                    }

                    Mission updatedMission = missionRepository.save(existingMission);
                    return ResponseEntity.ok().body(updatedMission);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error updating mission: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMission(@PathVariable Long id) {
        return missionRepository.findById(id)
                .map(mission -> {
                    missionRepository.delete(mission);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/affaire/{affaireId}")
    public List<Mission> getMissionsByAffaireId(@PathVariable Long affaireId) {
        return missionRepository.findByAffaireIdAffaire(affaireId);
    }

//    @PostMapping("/{id}/repartition")
//    @Transactional
//    public ResponseEntity<?> repartitionTasks(@PathVariable Long id,@RequestBody RepartitionRequest repartitionRequest) {
//        double partDivisionPrincipal = repartitionRequest.getPrincipalDivisionPart();
//        double partDivisionsSecondaires = 0;
//        double partPartenaires = 0;
//        double partSousTraitants = 0;
//
//        for (MissionDivisionDTO division : repartitionRequest.getSecondaryDivisions()) {
//            partDivisionsSecondaires += division.getPartMission();
//        }
//        for (MissionPartenaireDTO partenaire : repartitionRequest.getPartenaires()) {
//            partPartenaires += partenaire.getPartMission();
//        }
//        for (MissionSTDTO sTraitants : repartitionRequest.getSousTraitants()) {
//            partSousTraitants += sTraitants.getPartMission();
//        }
//        double sommeParts = partDivisionPrincipal + partDivisionsSecondaires + partPartenaires + partSousTraitants;
//
//        Mission mission = missionRepository.findById(id).orElseThrow();
//
//        if (mission.getPartMissionCID() > sommeParts) {
//            //add part principal
//            mission.setPartDivPrincipale(partDivisionPrincipal);
//            //add division secondaire
//            Set<MissionDivision> updatedSecondaryDivisions = new HashSet<>();
//            for (MissionDivisionDTO divisionDTO : repartitionRequest.getSecondaryDivisions()) {
//                Division division = divisionRepository.findById(divisionDTO.getDivisionId())
//                        .orElseThrow(() -> new RuntimeException("Division not found: " + divisionDTO.getDivisionId()));
//                MissionDivision missionDivision = mission.getSecondaryDivisions().stream()
//                        .filter(sd -> sd.getDivision().getId_division().equals(division.getId_division()))
//                        .findFirst()
//                        .orElse(new MissionDivision());
//                    missionDivision.setMission(mission);
//                    missionDivision.setDivision(division);
//                   missionDivision.setPartMission(divisionDTO.getPartMission());
//                   updatedSecondaryDivisions.add(missionDivisionRepository.save(missionDivision));
//            }
//            //add sous traitance
//             Set<MissionST> updatedSousTraitants = new HashSet<>();
//            for (MissionSTDTO stDTO : repartitionRequest.getSousTraitants()) {
//                SousTraitant sousTraitant = sousTraitantRepository.findById(stDTO.getSousTraitantId())
//                        .orElseThrow(() -> new RuntimeException("Sous-traitant not found: " + stDTO.getSousTraitantId()));
//                MissionST missionST = mission.getSousTraitants().stream()
//                        .filter(st -> st.getSousTraitant().getId_soustrait().equals(sousTraitant.getId_soustrait()))
//                        .findFirst()
//                        .orElse(new MissionST());
//                        missionST.setMission(mission);
//                        missionST.setSousTraitant(sousTraitant);
//                        missionST.setPartMission(stDTO.getPartMission());
//                        updatedSousTraitants.add(missionSTRepository.save(missionST));
//            }
//            //add partenaire
//                Set<MissionPartenaire> updatedPartenaires = new HashSet<>();
//                for (MissionPartenaireDTO partenaireDTO : repartitionRequest.getPartenaires()) {
//                    Partenaire partenaire = partenaireRepository.findById(partenaireDTO.getPartenaireId())
//                            .orElseThrow(() -> new RuntimeException("Partenaire not found: " + partenaireDTO.getPartenaireId()));
//
//                    MissionPartenaire missionPartenaire = mission.getPartenaires().stream()
//                            .filter(mp -> mp.getPartenaire().getId_partenaire().equals(partenaire.getId_partenaire()))
//                            .findFirst()
//                            .orElse(new MissionPartenaire());
//
//                    missionPartenaire.setMission(mission);
//                    missionPartenaire.setPartenaire(partenaire);
//                    missionPartenaire.setPartMission(partenaireDTO.getPartMission());
//                    updatedPartenaires.add(missionPartenaireRepository.save(missionPartenaire));
//
//                }
//                //add mission
//               Mission updatedMission = missionRepository.save(mission);
//               return ResponseEntity.ok().body(updatedMission);
//
//
//
//
//        }
//        return null;
//    }

    @PostMapping("/{id}/repartition")
    @Transactional
    public ResponseEntity<?> repartitionTasks(@PathVariable Long id, @RequestBody RepartitionRequest repartitionRequest) {
        logger.info("Received repartition request for mission id: {}", id);
        logger.info("Request body: {}", repartitionRequest);

        try {
            Mission existingMission = missionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Mission not found"));

            logger.info("Found mission: {}", existingMission);

            // Calculate total part mission
            double totalPartMission = repartitionRequest.getPrincipalDivisionPart();
            totalPartMission += repartitionRequest.getSecondaryDivisions().stream()
                    .mapToDouble(MissionDivisionDTO::getPartMission)
                    .sum();
            totalPartMission += repartitionRequest.getPartenaires().stream()
                    .mapToDouble(MissionPartenaireDTO::getPartMission)
                    .sum();
            totalPartMission += repartitionRequest.getSousTraitants().stream()
                    .mapToDouble(MissionSTDTO::getPartMission)
                    .sum();

            // Validate total part mission
            if (totalPartMission > existingMission.getPartMissionCID()) {
                return ResponseEntity.badRequest().body("Total part mission (" + totalPartMission +
                    ") exceeds partMissionCID (" + existingMission.getPartMissionCID() + ")");
            }

            // Update the principal division's part
            existingMission.setPartDivPrincipale(repartitionRequest.getPrincipalDivisionPart());
            missionRepository.save(existingMission);
            logger.info("Updated principal division part to: {}", repartitionRequest.getPrincipalDivisionPart());

            // Update or create secondary divisions
            Set<MissionDivision> updatedSecondaryDivisions = new HashSet<>();
            for (MissionDivisionDTO divisionDTO : repartitionRequest.getSecondaryDivisions()) {
                Division division = divisionRepository.findById(divisionDTO.getDivisionId())
                        .orElseThrow(() -> new RuntimeException("Division not found: " + divisionDTO.getDivisionId()));

                MissionDivision missionDivision = existingMission.getSecondaryDivisions().stream()
                        .filter(sd -> sd.getDivision().getId_division().equals(division.getId_division()))
                        .findFirst()
                        .orElse(new MissionDivision());

                missionDivision.setMission(existingMission);
                missionDivision.setDivision(division);
                missionDivision.setPartMission(divisionDTO.getPartMission());
                updatedSecondaryDivisions.add(missionDivisionRepository.save(missionDivision));
                logger.info("Updated/Added secondary division: {}", missionDivision);
            }
            existingMission.getSecondaryDivisions().retainAll(updatedSecondaryDivisions);

            // Update or create partners
            Set<MissionPartenaire> updatedPartenaires = new HashSet<>();
            for (MissionPartenaireDTO partenaireDTO : repartitionRequest.getPartenaires()) {
                Partenaire partenaire = partenaireRepository.findById(partenaireDTO.getPartenaireId())
                        .orElseThrow(() -> new RuntimeException("Partenaire not found: " + partenaireDTO.getPartenaireId()));

                MissionPartenaire missionPartenaire = existingMission.getPartenaires().stream()
                        .filter(mp -> mp.getPartenaire().getId_partenaire().equals(partenaire.getId_partenaire()))
                        .findFirst()
                        .orElse(new MissionPartenaire());

                missionPartenaire.setMission(existingMission);
                missionPartenaire.setPartenaire(partenaire);
                missionPartenaire.setPartMission(partenaireDTO.getPartMission());
                updatedPartenaires.add(missionPartenaireRepository.save(missionPartenaire));
                logger.info("Updated/Added partner: {}", missionPartenaire);
            }
            existingMission.getPartenaires().retainAll(updatedPartenaires);

            // Update or create subcontractors
            Set<MissionST> updatedSousTraitants = new HashSet<>();
            for (MissionSTDTO stDTO : repartitionRequest.getSousTraitants()) {
                SousTraitant sousTraitant = sousTraitantRepository.findById(stDTO.getSousTraitantId())
                        .orElseThrow(() -> new RuntimeException("Sous-traitant not found: " + stDTO.getSousTraitantId()));

                MissionST missionST = existingMission.getSousTraitants().stream()
                        .filter(st -> st.getSousTraitant().getId_soustrait().equals(sousTraitant.getId_soustrait()))
                        .findFirst()
                        .orElse(new MissionST());

                missionST.setMission(existingMission);
                missionST.setSousTraitant(sousTraitant);
                missionST.setPartMission(stDTO.getPartMission());
                updatedSousTraitants.add(missionSTRepository.save(missionST));
                logger.info("Updated/Added subcontractor: {}", missionST);
            }
            existingMission.getSousTraitants().retainAll(updatedSousTraitants);

            Mission updatedMission = missionRepository.save(existingMission);
            logger.info("Repartition completed successfully");
            return ResponseEntity.ok().body(updatedMission);
        } catch (Exception e) {
            logger.error("Error during repartition", e);
            return ResponseEntity.badRequest().body("Error repartitioning tasks: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/part-div-principale")
    public ResponseEntity<?> updateMissionPartDivPrincipale(@PathVariable Long id, @RequestBody PartDivPrincipaleDTO partDivPrincipaleDTO) {
        try {
            return missionRepository.findById(id)
                .map(existingMission -> {
                    Double newPartDivPrincipale = partDivPrincipaleDTO.getPartDivPrincipale();
                    
                    // Validate the newPartDivPrincipale
                    if (newPartDivPrincipale == null || newPartDivPrincipale < 0) {
                        return ResponseEntity.badRequest().body("La valeur de partDivPrincipale est invalide. Elle doit être un montant non négatif.");
                    }

                    // Check if partDivPrincipale exceeds partMissionCID
                    if (newPartDivPrincipale > existingMission.getPartMissionCID()) {
                        return ResponseEntity.badRequest().body("partDivPrincipale ne peut pas être supérieur à partMissionCID.");
                    }

                    existingMission.setPartDivPrincipale(newPartDivPrincipale);
                    Mission updatedMission = missionRepository.save(existingMission);
                    return ResponseEntity.ok().body(updatedMission);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace(); // Consider using a logger instead
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour de la part div principale de la mission : " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mission> getMissionDetails(@PathVariable Long id) {
            try {
                Mission mission = missionRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Mission not found"));

                return ResponseEntity.ok(mission);
            } catch (Exception e) {
                logger.error("Error fetching mission details", e);
                return ResponseEntity.badRequest().build();
            }
    }
    //tableaux qui affiche partenaire sous traitants division secondaires avec leurs parts
    @GetMapping("/missions-divisions/{id}")
    public ResponseEntity<List<AffaireMissionDTO>> getAffairesWithMissionsAndDivisions(@PathVariable Long id) {
        List<Object[]> result = missionRepository.findAffairesWithMissionsAndDivisionsByChefDivision(id);
        List<AffaireMissionDTO> dtoList = result.stream().map(record -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            return new AffaireMissionDTO(
                    (String) record[0],
                    dateFormat.format((Date) record[1]),
                    dateFormat.format((Date) record[2]),
                    (Double) record[3],
                    (String) record[4],
                    (String) record[5],
                    (Double) record[6],
                    (String) record[7],
                    (Double) record[8],
                    (String) record[9],
                    (Double) record[10],
                    (String) record[11],
                    (Double) record[12]
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("missionbydivisionsc/{id}/{idf}")
    public List<MissionDivision2DTO> findMissionByDivissionsc(@PathVariable Long id,@PathVariable Long idf){
        return missionDivisionRepository.findMissionDivisionByUtilisateurId(id,idf);
    }
    @GetMapping("missionbydivisionpr/{id}/{idf}")
    public List<Mission> findMissionByDivissionpr(@PathVariable Long id,@PathVariable Long idf){
        return missionRepository.findMissionsByAffaireId(id,idf);
    }

    public static class PartDivPrincipaleDTO {
        private Double partDivPrincipale;

        public Double getPartDivPrincipale() {
            return partDivPrincipale;
        }

        public void setPartDivPrincipale(Double partDivPrincipale) {
            this.partDivPrincipale = partDivPrincipale;
        }
    }


}