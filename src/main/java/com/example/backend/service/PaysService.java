package com.example.backend.service;

import com.example.backend.model.Pays;
import com.example.backend.repository.PaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaysService {
    @Autowired
    private PaysRepository paysRepository;

    public List<Pays> getAllPays() {
        return paysRepository.findAll();
    }

    public Optional<Pays> getPaysById(Long id) {
        return paysRepository.findById(id);

    }

    public Pays CreatePays(Pays pays) {
        return paysRepository.save(pays);
    }

    public Optional<Pays> updatePays(Long id, Pays pays) {
        return paysRepository.findById(id)
                .map(existingPays -> {
                    existingPays.setLibelle_pays(pays.getLibelle_pays());
                    return paysRepository.save(existingPays);
                });
    }

    public boolean deletePays(Long id) {
        return paysRepository.findById(id)
                .map(pays -> {
                    paysRepository.delete(pays);
                    return true;
                }).orElse(false);
    }




}
