package com.example.backend.service;

import com.example.backend.model.Affaire;
import com.example.backend.model.StatusAffaire;
import com.example.backend.repository.AffaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AffaireService {
    @Autowired
    private AffaireRepository affaireRepository;
    public List<Affaire> getAllAffaire(){
        return affaireRepository.findAll();
    }
    public Affaire getById(Long id){
        return affaireRepository.findById(id).orElseThrow();
    }
    public Affaire createAffaire(Affaire affaire){
        return affaireRepository.save(affaire);
    }
    public void deleteAffaire(Long id){
        affaireRepository.deleteById(id);
    }
    public Map<String,Long> getAffaireByStatus(){
        long total = affaireRepository.count();
        long enCours = affaireRepository.countByStatusAffaire(StatusAffaire.EN_PRODUCTION);
        long terminees = affaireRepository.countByStatusAffaire(StatusAffaire.TERMINE);
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("enCours", enCours);
        stats.put("terminees", terminees);
        return stats;
    }

    public Affaire updateAffaire(Long id ,Affaire affaire) {
        Affaire affaire1 = getById(id);
        affaire1.setStatusAffaire(affaire.getStatusAffaire());
        affaire1.setPrixGlobal(affaire.getPrixGlobal());
        affaire1.setMarche(affaire.getMarche());
        affaire1.setDateDebut(affaire.getDateDebut());
        affaire1.setDateArret(affaire.getDateArret());
        affaire1.setDateRecommencement(affaire.getDateRecommencement());
        affaire1.setDateFin(affaire.getDateFin());
        affaire1.setClient(affaire.getClient());
        affaire1.setPartCID(affaire.getPartCID());
        affaire1.setPolePrincipale(affaire.getPolePrincipale());
        affaire1.setDivisionPrincipale(affaire.getDivisionPrincipale());
        return affaireRepository.save(affaire1);
    }
}
