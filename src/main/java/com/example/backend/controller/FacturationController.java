package com.example.backend.controller;

import com.example.backend.model.Facturation;
import com.example.backend.repository.FacturationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturations")
public class FacturationController {

    @Autowired
    private FacturationRepository facturationRepository;

    @GetMapping
    public List<Facturation> getAllFacturations() {
        return facturationRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facturation> getFacturationById(@PathVariable Long id) {
        return facturationRepository.findById(id)
                .map(facturation -> ResponseEntity.ok().body(facturation))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Facturation createFacturation(@RequestBody Facturation facturation) {
        return facturationRepository.save(facturation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facturation> updateFacturation(@PathVariable Long id, @RequestBody Facturation facturation) {
        return facturationRepository.findById(id)
                .map(existingFacturation -> {
                    existingFacturation.setMontantFacture(facturation.getMontantFacture());
                    existingFacturation.setDocumentFacture(facturation.getDocumentFacture());
                    existingFacturation.setDateFacturation(facturation.getDateFacturation());
                    existingFacturation.setMission(facturation.getMission());
                    return ResponseEntity.ok().body(facturationRepository.save(existingFacturation));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFacturation(@PathVariable Long id) {
        return facturationRepository.findById(id)
                .map(facturation -> {
                    facturationRepository.delete(facturation);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
