package com.example.backend.controller;

import com.example.backend.model.Partenaire;
import com.example.backend.repository.PartenaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partenaires")
public class PartenaireController {

    @Autowired
    private PartenaireRepository partenaireRepository;

    @GetMapping
    public List<Partenaire> getAllPartenaires() {
        return partenaireRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partenaire> getPartenaireById(@PathVariable Long id) {
        return partenaireRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Partenaire createPartenaire(@RequestBody Partenaire partenaire) {
        return partenaireRepository.save(partenaire);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partenaire> updatePartenaire(@PathVariable Long id, @RequestBody Partenaire partenaireDetails) {
        return partenaireRepository.findById(id)
                .map(partenaire -> {
                    partenaire.setNom_partenaire(partenaireDetails.getNom_partenaire());
                    Partenaire updatedPartenaire = partenaireRepository.save(partenaire);
                    return ResponseEntity.ok(updatedPartenaire);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePartenaire(@PathVariable Long id) {
        return partenaireRepository.findById(id)
                .map(partenaire -> {
                    partenaireRepository.delete(partenaire);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}