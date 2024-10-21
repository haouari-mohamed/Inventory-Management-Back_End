package com.example.backend.controller;

import com.example.backend.model.SousTraitant;
import com.example.backend.repository.SousTraitantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sous-traitants")
public class SousTraitantController {

    @Autowired
    private SousTraitantRepository sousTraitantRepository;

    @GetMapping
    public List<SousTraitant> getAllSousTraitants() {
        return sousTraitantRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SousTraitant> getSousTraitantById(@PathVariable Long id) {
        return sousTraitantRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SousTraitant createSousTraitant(@RequestBody SousTraitant sousTraitant) {
        return sousTraitantRepository.save(sousTraitant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SousTraitant> updateSousTraitant(@PathVariable Long id, @RequestBody SousTraitant sousTraitantDetails) {
        return sousTraitantRepository.findById(id)
                .map(sousTraitant -> {
                    sousTraitant.setNom_soustrait(sousTraitantDetails.getNom_soustrait());
                    SousTraitant updatedSousTraitant = sousTraitantRepository.save(sousTraitant);
                    return ResponseEntity.ok(updatedSousTraitant);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSousTraitant(@PathVariable Long id) {
        return sousTraitantRepository.findById(id)
                .map(sousTraitant -> {
                    sousTraitantRepository.delete(sousTraitant);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}