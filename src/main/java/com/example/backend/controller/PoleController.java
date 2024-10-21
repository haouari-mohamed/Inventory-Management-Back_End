package com.example.backend.controller;

import com.example.backend.model.Pole;
import com.example.backend.repository.DivisionRepository;
import com.example.backend.repository.PoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/poles")
public class PoleController {

    @Autowired
    private PoleRepository poleRepository;

    @Autowired
    private DivisionRepository divisionRepository;

    @GetMapping
    public List<Pole> getAllPoles() {
        return poleRepository.findAll();
    }

    @PostMapping
    public Pole createPole(@RequestBody Pole pole) {
        return poleRepository.save(pole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pole> updatePole(@PathVariable Long id, @RequestBody Pole poleDetails) {
        Pole pole = poleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pole not found with id " + id));
        
        pole.setLibelle_pole(poleDetails.getLibelle_pole());
        final Pole updatedPole = poleRepository.save(pole);
        return ResponseEntity.ok(updatedPole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePole(@PathVariable Long id) {
        return poleRepository.findById(id)
                .map(pole -> {
                    if (divisionRepository.existsByPole(pole)) {
                        return ResponseEntity
                                .status(409)
                                .body("Ce pôle est associé à d'autres données. Le supprimer entraînera la suppression de toutes les données liées.");
                    }
                    poleRepository.delete(pole);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/cascade")
    @Transactional
    public ResponseEntity<?> deletePoleWithCascade(@PathVariable Long id) {
        return poleRepository.findById(id)
                .map(pole -> {
                    divisionRepository.deleteByPole(pole);
                    poleRepository.delete(pole);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
