package com.example.backend.service;

import com.example.backend.model.Unite;
import com.example.backend.repository.UniteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UniteService {

    @Autowired
    private UniteRepository uniteRepository;

    public List<Unite> getAllUnites() {
        return uniteRepository.findAll();
    }

    public Optional<Unite> getUniteById(Long id) {
        return uniteRepository.findById(id);
    }

    public Unite createUnite(Unite unite) {
        return uniteRepository.save(unite);
    }

    public Optional<Unite> updateUnite(Long id, Unite uniteDetails) {
        return uniteRepository.findById(id)
                .map(unite -> {
                    unite.setNom_unite(uniteDetails.getNom_unite());
                    return uniteRepository.save(unite);
                });
    }

    public boolean deleteUnite(Long id) {
        return uniteRepository.findById(id)
                .map(unite -> {
                    uniteRepository.delete(unite);
                    return true;
                }).orElse(false);
    }
}
