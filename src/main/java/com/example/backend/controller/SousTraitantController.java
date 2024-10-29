package com.example.backend.controller;

import com.example.backend.model.SousTraitant;
import com.example.backend.service.SousTraitantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sous-traitants")
//@CrossOrigin(origins = "http://localhost:3000")
public class SousTraitantController {

    @Autowired

    private SousTraitantService sousTraitantService;


    @GetMapping
    public List<SousTraitant> getAllSousTraitants() {
        return sousTraitantService.getAllSousTraitants();

    }

    @GetMapping("/{id}")
    public ResponseEntity<SousTraitant> getSousTraitantById(@PathVariable Long id) {

        return sousTraitantService.getSousTraitantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping

    public ResponseEntity<SousTraitant> createSousTraitant(@RequestBody SousTraitant sousTraitant) {
        SousTraitant createdSousTraitant = sousTraitantService.createSousTraitant(sousTraitant);
        return ResponseEntity.ok(createdSousTraitant);

    }

    @PutMapping("/{id}")
    public ResponseEntity<SousTraitant> updateSousTraitant(@PathVariable Long id, @RequestBody SousTraitant sousTraitantDetails) {
        return sousTraitantService.updateSousTraitant(id, sousTraitantDetails)
                .map(updatedSousTraitant -> ResponseEntity.ok().body(updatedSousTraitant))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSousTraitant(@PathVariable Long id) {

        boolean isDeleted = sousTraitantService.deleteSousTraitant(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

