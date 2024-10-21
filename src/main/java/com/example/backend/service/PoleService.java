package com.example.backend.service;

import com.example.backend.model.Pole;
import com.example.backend.repository.DivisionRepository;
import com.example.backend.repository.PoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class PoleService {
    @Autowired
    private PoleRepository poleRepository;
    @Autowired
    private DivisionRepository divisionRepository;

    public List<Pole> getAllPoles() {
        return poleRepository.findAll();
    }
    public Optional<Pole> getPoleById(Long id) {
        return poleRepository.findById(id);
    }

    public Pole createPole(Pole pole) {
        return poleRepository.save(pole);
    }
    public Optional<Pole> updatePole(Long id, Pole poleDetails) {
        return poleRepository.findById(id)
                .map(pole -> {
                    pole.setLibelle_pole(poleDetails.getLibelle_pole());
                    return poleRepository.save(pole);
                });
    }

    public boolean deletePole(Long id) {
        return poleRepository.findById(id)
                .map(pole -> {
                    if (divisionRepository.existsByPole(pole)) {
                        return false;
                    }
                    poleRepository.delete(pole);
                    return true;
                }).orElse(false);
    }

    @Transactional
    public boolean deletePoleWithCascade(Long id) {
        return poleRepository.findById(id)
                .map(pole -> {
                    divisionRepository.deleteByPole(pole);
                    poleRepository.delete(pole);
                    return true;
                }).orElse(false);
    }

}
