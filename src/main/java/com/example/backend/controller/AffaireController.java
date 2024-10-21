package com.example.backend.controller;

import com.example.backend.model.*;
import com.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.*;

@RestController
@RequestMapping("/api/affaires")
public class AffaireController {

    @Autowired
    private AffaireRepository affaireRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PoleRepository poleRepository;

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping
    public List<Affaire> getAllAffaires() {
        return affaireRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Affaire> getAffaireById(@PathVariable Long id) {
        return affaireRepository.findById(id)
                .map(affaire -> ResponseEntity.ok().body(affaire))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createAffaire(@RequestBody Affaire affaire) {
        if (affaire.getIdAffaire() == null) {
            return ResponseEntity.badRequest().body("ID Affaire must be provided");
        }
        if (affaireRepository.existsById(affaire.getIdAffaire())) {
            return ResponseEntity.badRequest().body("An affaire with this ID already exists");
        }
        affaire.setStatusAffaire(StatusAffaire.EN_CREATION); // Set initial status
        Affaire savedAffaire = affaireRepository.save(affaire);
        return ResponseEntity.ok(savedAffaire);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Affaire> updateAffaire(@PathVariable Long id, @RequestBody Affaire affaire) {
        return affaireRepository.findById(id)
                .map(existingAffaire -> {
                    existingAffaire.setLibelle_affaire(affaire.getLibelle_affaire());
                    existingAffaire.setPrixGlobal(affaire.getPrixGlobal());
                    existingAffaire.setStatusAffaire(affaire.getStatusAffaire());
                    existingAffaire.setMarche(affaire.getMarche());
                    existingAffaire.setDateDebut(affaire.getDateDebut());
                    existingAffaire.setDateFin(affaire.getDateFin());
                    existingAffaire.setDateArret(affaire.getDateArret());
                    existingAffaire.setDateRecommencement(affaire.getDateRecommencement());
                    
                    // Update client
                    if (affaire.getClient() != null && affaire.getClient().getId_client() != null) {
                        clientRepository.findById(affaire.getClient().getId_client())
                                .ifPresent(existingAffaire::setClient);
                    }
                    
                    // Update pole
                    if (affaire.getPolePrincipale() != null && affaire.getPolePrincipale().getId_pole() != null) {
                        poleRepository.findById(affaire.getPolePrincipale().getId_pole())
                                .ifPresent(existingAffaire::setPolePrincipale);
                    }
                    
                    // Update division
                    if (affaire.getDivisionPrincipale() != null && affaire.getDivisionPrincipale().getId_division() != null) {
                        divisionRepository.findById(affaire.getDivisionPrincipale().getId_division())
                                .ifPresent(existingAffaire::setDivisionPrincipale);
                    }
                    
                    existingAffaire.setPartCID(affaire.getPartCID());
                    return ResponseEntity.ok(affaireRepository.save(existingAffaire));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/statuses")
    public List<StatusAffaire> getAllStatuses() {
        return Arrays.asList(StatusAffaire.values());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAffaire(@PathVariable Long id) {
        return affaireRepository.findById(id)
                .map(affaire -> {
                    affaireRepository.delete(affaire);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/last-id")
    public ResponseEntity<String> getLastAffaireId() {
        return affaireRepository.findTopByOrderByIdAffaireDesc()
                .map(affaire -> {
                    Long lastId = affaire.getIdAffaire();
                    int currentYear = java.time.Year.now().getValue();
                    long nextId;
                    if (lastId / 100000 == currentYear) {
                        nextId = lastId + 1;
                    } else {
                        nextId = (long) currentYear * 100000 + 1;
                    }
                    return ResponseEntity.ok(String.valueOf(nextId));
                })
                .orElse(ResponseEntity.ok(String.valueOf(java.time.Year.now().getValue() * 100000 + 1)));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getAffaireStats() {
        long total = affaireRepository.count();
        long enCours = affaireRepository.countByStatusAffaire(StatusAffaire.EN_PRODUCTION);
        long terminees = affaireRepository.countByStatusAffaire(StatusAffaire.TERMINE);

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("enCours", enCours);
        stats.put("terminees", terminees);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/monthly-stats")
    public ResponseEntity<List<Long>> getMonthlyStats() {
        List<Long> monthlyStats = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 12; i++) {
            cal.set(Calendar.MONTH, i);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date startOfMonth = cal.getTime();
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endOfMonth = cal.getTime();
            long count = affaireRepository.countByStatusAffaireAndDateFinBetween(StatusAffaire.TERMINE, startOfMonth, endOfMonth);
            monthlyStats.add(count);
        }
        return ResponseEntity.ok(monthlyStats);
    }

    @GetMapping("/stats/{userId}")
    public ResponseEntity<Map<String, Long>> getAffaireStatsByPole(@PathVariable Long userId) {
        return utilisateurRepository.findById(userId)
            .map(user -> {
                Pole pole = user.getPole();
                if (pole == null) {
                    return ResponseEntity.badRequest().body(Collections.singletonMap("error", 0L));
                }

                Map<String, Long> stats = new HashMap<>();
                stats.put("total", affaireRepository.countByPolePrincipale(pole));
                stats.put("enCreation", affaireRepository.countByPolePrincipaleAndStatusAffaire(pole, StatusAffaire.EN_CREATION));
                stats.put("cdpDecide", affaireRepository.countByPolePrincipaleAndStatusAffaire(pole, StatusAffaire.CDP_DECIDE));
                stats.put("enProduction", affaireRepository.countByPolePrincipaleAndStatusAffaire(pole, StatusAffaire.EN_PRODUCTION));
                stats.put("interrompu", affaireRepository.countByPolePrincipaleAndStatusAffaire(pole, StatusAffaire.INTERROMPU));
                stats.put("termine", affaireRepository.countByPolePrincipaleAndStatusAffaire(pole, StatusAffaire.TERMINE));
                stats.put("annule", affaireRepository.countByPolePrincipaleAndStatusAffaire(pole, StatusAffaire.ANNULE));

                return ResponseEntity.ok(stats);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/chart-data/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getChartDataByPole(@PathVariable Long userId) {
        return (ResponseEntity<List<Map<String, Object>>>) utilisateurRepository.findById(userId)
            .map(user -> {
                Pole pole = user.getPole();
                if (pole == null) {
                    return ResponseEntity.badRequest().body(null);
                }

                int currentYear = Year.now().getValue();
                List<Object[]> rawData = affaireRepository.countByPolePrincipaleAndStatusAffaireGroupByMonth(pole, currentYear);

                Map<StatusAffaire, long[]> statusData = new EnumMap<>(StatusAffaire.class);
                for (StatusAffaire status : StatusAffaire.values()) {
                    statusData.put(status, new long[12]);
                }

                for (Object[] row : rawData) {
                    int month = ((Number) row[0]).intValue() - 1; // Adjust for 0-based array
                    StatusAffaire status = (StatusAffaire) row[1];
                    long count = ((Number) row[2]).longValue();
                    statusData.get(status)[month] = count;
                }

                List<Map<String, Object>> seriesData = new ArrayList<>();
                for (Map.Entry<StatusAffaire, long[]> entry : statusData.entrySet()) {
                    Map<String, Object> series = new HashMap<>();
                    series.put("name", entry.getKey().name());
                    series.put("data", entry.getValue());
                    seriesData.add(series);
                }

                return ResponseEntity.ok(seriesData);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stats/division/{userId}")
    public ResponseEntity<Map<String, Long>> getAffaireStatsByDivision(@PathVariable Long userId) {
        return utilisateurRepository.findById(userId)
            .map(user -> {
                Division division = user.getDivision();
                if (division == null) {
                    return ResponseEntity.badRequest().body(Collections.singletonMap("error", 0L));
                }

                Map<String, Long> stats = new HashMap<>();
                stats.put("total", affaireRepository.countByDivisionPrincipale(division));
                stats.put("enCreation", affaireRepository.countByDivisionPrincipaleAndStatusAffaire(division, StatusAffaire.EN_CREATION));
                stats.put("cdpDecide", affaireRepository.countByDivisionPrincipaleAndStatusAffaire(division, StatusAffaire.CDP_DECIDE));
                stats.put("enProduction", affaireRepository.countByDivisionPrincipaleAndStatusAffaire(division, StatusAffaire.EN_PRODUCTION));
                stats.put("interrompu", affaireRepository.countByDivisionPrincipaleAndStatusAffaire(division, StatusAffaire.INTERROMPU));
                stats.put("termine", affaireRepository.countByDivisionPrincipaleAndStatusAffaire(division, StatusAffaire.TERMINE));
                stats.put("annule", affaireRepository.countByDivisionPrincipaleAndStatusAffaire(division, StatusAffaire.ANNULE));

                return ResponseEntity.ok(stats);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/chart-data/division/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getChartDataByDivision(@PathVariable Long userId) {
        return (ResponseEntity<List<Map<String, Object>>>) utilisateurRepository.findById(userId)
            .map(user -> {
                Division division = user.getDivision();
                if (division == null) {
                    return ResponseEntity.badRequest().body(null);
                }

                int currentYear = Year.now().getValue();
                List<Object[]> rawData = affaireRepository.countByDivisionPrincipaleAndStatusAffaireGroupByMonth(division, currentYear);

                Map<StatusAffaire, long[]> statusData = new EnumMap<>(StatusAffaire.class);
                for (StatusAffaire status : StatusAffaire.values()) {
                    statusData.put(status, new long[12]);
                }

                for (Object[] row : rawData) {
                    int month = ((Number) row[0]).intValue() - 1; // Adjust for 0-based array
                    StatusAffaire status = (StatusAffaire) row[1];
                    long count = ((Number) row[2]).longValue();
                    statusData.get(status)[month] = count;
                }

                List<Map<String, Object>> seriesData = new ArrayList<>();
                for (Map.Entry<StatusAffaire, long[]> entry : statusData.entrySet()) {
                    Map<String, Object> series = new HashMap<>();
                    series.put("name", entry.getKey().name());
                    series.put("data", entry.getValue());
                    seriesData.add(series);
                }

                return ResponseEntity.ok(seriesData);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-division/{userId}")
    public ResponseEntity<List<Affaire>> getAffairesByUserDivision(@PathVariable Long userId) {
        return (ResponseEntity<List<Affaire>>) utilisateurRepository.findById(userId)
            .map(user -> {
                Division division = user.getDivision();
                if (division == null) {
                    return ResponseEntity.badRequest().body(null);
                }
                List<Affaire> affaires = affaireRepository.findByDivisionPrincipale(division);
                return ResponseEntity.ok(affaires);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/chef-projet")
    public ResponseEntity<Object> assignChefProjet(@PathVariable Long id, @RequestBody Map<String, Long> request) {
        Long chefProjetId = request.get("chefProjetId");
        Optional<Affaire> affaireOpt = affaireRepository.findById(id);
        
        if (!affaireOpt.isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Affaire not found");
            return ResponseEntity.notFound().build();
        }
        
        Affaire affaire = affaireOpt.get();
        Optional<Utilisateur> chefProjetOpt = utilisateurRepository.findById(chefProjetId);
        
        if (!chefProjetOpt.isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Chef de Projet not found");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        Utilisateur chefProjet = chefProjetOpt.get();
        affaire.setChefProjet(chefProjet);
        Affaire updatedAffaire = affaireRepository.save(affaire);
        return ResponseEntity.ok().body(updatedAffaire);
    }
}
