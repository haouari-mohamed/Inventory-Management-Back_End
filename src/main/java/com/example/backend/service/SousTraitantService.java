package com.example.backend.service;

import com.example.backend.model.SousTraitant;
import com.example.backend.repository.SousTraitantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SousTraitantService {

    @Autowired
    private SousTraitantRepository sousTraitantRepository;

    public List<SousTraitant> getAllSousTraitants() {
        return sousTraitantRepository.findAll();
    }

    public Optional<SousTraitant> getSousTraitantById(Long id) {
        return sousTraitantRepository.findById(id);
    }

    public SousTraitant createSousTraitant(SousTraitant sousTraitant) {
        return sousTraitantRepository.save(sousTraitant);
    }

    @Transactional
    public Optional<SousTraitant> updateSousTraitant(Long id, SousTraitant sousTraitantDetails) {
        return sousTraitantRepository.findById(id)
                .map(sousTraitant -> {
                    sousTraitant.setNom_soustrait(sousTraitantDetails.getNom_soustrait());
                    return sousTraitantRepository.save(sousTraitant);
                });
    }

    @Transactional
    public boolean deleteSousTraitant(Long id) {
        return sousTraitantRepository.findById(id)
                .map(sousTraitant -> {
                    sousTraitantRepository.delete(sousTraitant);
                    return true;
                }).orElse(false);
    }
}

