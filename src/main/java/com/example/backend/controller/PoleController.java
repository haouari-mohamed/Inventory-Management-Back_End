package com.example.backend.controller;

import com.example.backend.model.Pole;

import com.example.backend.service.PoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/poles")
@CrossOrigin(origins = "http://localhost:3000")
public class PoleController {


    @Autowired
    private PoleService poleService;


    @GetMapping
    public List<Pole> getAllPoles() {
        return poleService.getAllPoles();
    }

    @PostMapping
    public Pole createPole(@RequestBody Pole pole) {
        return poleService.createPole(pole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pole> updatePole(@PathVariable Long id, @RequestBody Pole poleDetails) {
        return poleService.updatePole(id, poleDetails)
                .map(updatedPole -> ResponseEntity.ok(updatedPole))
                .orElseThrow(() -> new RuntimeException("Pole not found with id " + id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePole(@PathVariable Long id) {
        boolean deleted = poleService
                .deletePole(id);
        if (!deleted) {
            return ResponseEntity.status(409)
                    .body("Ce pôle est associé à d'autres données. Le supprimer entraînera la suppression de toutes les données liées.");
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/cascade")
    public ResponseEntity<?> deletePoleWithCascade(@PathVariable Long id) {
        boolean deleted = poleService.deletePoleWithCascade(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
