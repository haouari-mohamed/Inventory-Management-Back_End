package com.example.backend.controller;

import com.example.backend.DTO.*;
import com.example.backend.model.*;
import com.example.backend.repository.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/missions")
//@CrossOrigin(origins = "http://localhost:3000")
public class MissionController {

    private static final Logger logger = LoggerFactory.getLogger(MissionController.class);

    @Autowired
    private DivisionRepository divisionRepository;

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
    public ResponseEntity<List<MissionDTO>> getMissionsByAffaireId(@PathVariable Long affaireId) {
        List<Mission> missions = missionRepository.findByAffaireIdAffaire(affaireId);
        if (missions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        List<MissionDTO> missionDTOs = missions.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(missionDTOs);
    }

    private MissionDTO convertToDTO(Mission mission) {
        MissionDTO missionDTO=new MissionDTO();

        if (mission.getAffaire() != null) {
            missionDTO.setAffaireId(mission.getAffaire().getIdAffaire());
        }
        if (mission.getPrincipalDivision() != null) {
            missionDTO.setPrincipalDivisionId(mission.getPrincipalDivision().getId_division());
        }
        if (mission.getUnite() != null) {
            missionDTO.setUniteId(mission.getUnite().getId_unite());
        }
        
        // Map nested objects
        missionDTO.setMissionDivisions(mission.getSecondaryDivisions().stream()
            .map(div -> {
                MissionDivisionDTO missionDivisionDTO = new MissionDivisionDTO();
                missionDivisionDTO.setId(div.getId());
                missionDivisionDTO.setMissionId(div.getMission().getId_mission());
                missionDivisionDTO.setDivisionId(div.getDivision().getId_division());
                missionDivisionDTO.setPartMission(div.getPartMission());
                return missionDivisionDTO;
            })
            .collect(Collectors.toList()));
        
        missionDTO.setMissionSousTraitants(mission.getSousTraitants().stream()
            .map(st -> {
                MissionSTDTO missionSTDTO = new MissionSTDTO();
                missionSTDTO.setId(st.getId());
                missionSTDTO.setMissionId(st.getMission().getId_mission());
                missionSTDTO.setSousTraitantId(st.getSousTraitant().getId_soustrait());
                missionSTDTO.setPartMission(st.getPartMission());
                return missionSTDTO;
            })
            .collect(Collectors.toList()));
        
        missionDTO.setMissionPartenaires(mission.getPartenaires().stream()
            .map(p -> {
                MissionPartenaireDTO missionPartenaireDTO = new MissionPartenaireDTO();
                missionPartenaireDTO.setId(p.getId());
                missionPartenaireDTO.setMissionId(p.getMission().getId_mission());
                missionPartenaireDTO.setPartenaireId(p.getPartenaire().getId_partenaire());
                missionPartenaireDTO.setPartMission(p.getPartMission());
                return missionPartenaireDTO;
            })
            .collect(Collectors.toList()));

        return missionDTO;
    }

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

            // Save the updated mission
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

//    @GetMapping("/{id}")
//    public ResponseEntity<MissionDTO> getMissionDetails(@PathVariable Long id) {
//        try {
//            Mission mission = missionRepository.findById(id)
//                    .orElseThrow(() -> new RuntimeException("Mission not found"));
//
//            MissionDTO missionDTO = new MissionDTO();
//
//            // Map secondary divisions
//            List<MissionDivisionDTO> secondaryDivisions = mission.getSecondaryDivisions().stream()
//                    .map(div -> missionDTO.map(div, MissionDivisionDTO.class))
//                    .collect(Collectors.toList());
//            missionDTO.setMissionDivisions(secondaryDivisions);
//
//            // Map sous-traitants
//            List<MissionSTDTO> sousTraitants = mission.getSousTraitants().stream()
//                    .map(st -> modelMapper.map(st, MissionSTDTO.class))
//                    .collect(Collectors.toList());
//            missionDTO.setMissionSousTraitants(sousTraitants);
//
//            // Map partenaires
//            List<MissionPartenaireDTO> partenaires = mission.getPartenaires().stream()
//                    .map(p -> modelMapper.map(p, MissionPartenaireDTO.class))
//                    .collect(Collectors.toList());
//            missionDTO.setMissionPartenaires(partenaires);
//
//            return ResponseEntity.ok(missionDTO);
//        } catch (Exception e) {
//            logger.error("Error fetching mission details", e);
//            return ResponseEntity.badRequest().build();
//        }
//    }

    public static class PartDivPrincipaleDTO {
        private Double partDivPrincipale;

        public Double getPartDivPrincipale() {
            return partDivPrincipale;
        }

        public void setPartDivPrincipale(Double partDivPrincipale) {
            this.partDivPrincipale = partDivPrincipale;
        }
    }

    public static class RepartitionRequest {
        private Double principalDivisionPart;
        private Set<MissionDivisionDTO> secondaryDivisions;
        private Set<MissionPartenaireDTO> partenaires;
        private Set<MissionSTDTO> sousTraitants;

        public Double getPrincipalDivisionPart() {
            return principalDivisionPart;
        }

        public void setPrincipalDivisionPart(Double principalDivisionPart) {
            this.principalDivisionPart = principalDivisionPart;
        }

        public Set<MissionDivisionDTO> getSecondaryDivisions() {
            return secondaryDivisions;
        }

        public void setSecondaryDivisions(Set<MissionDivisionDTO> secondaryDivisions) {
            this.secondaryDivisions = secondaryDivisions;
        }

        public Set<MissionPartenaireDTO> getPartenaires() {
            return partenaires;
        }

        public void setPartenaires(Set<MissionPartenaireDTO> partenaires) {
            this.partenaires = partenaires;
        }

        public Set<MissionSTDTO> getSousTraitants() {
            return sousTraitants;
        }

        public void setSousTraitants(Set<MissionSTDTO> sousTraitants) {
            this.sousTraitants = sousTraitants;
        }

        @Override
        public String toString() {
            return "RepartitionRequest{" +
                    "principalDivisionPart=" + principalDivisionPart +
                    ", secondaryDivisions=" + secondaryDivisions +
                    ", partenaires=" + partenaires +
                    ", sousTraitants=" + sousTraitants +
                    '}';
        }
    }
}