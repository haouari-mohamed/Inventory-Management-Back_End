package com.example.backend.controller;

import com.example.backend.model.Encaissement;
import com.example.backend.repository.EncaissementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encaissements")
public class EncaissementController {

    @Autowired
    private EncaissementRepository encaissementRepository;

    @GetMapping
    public List<Encaissement> getAllEncaissements() {
        return encaissementRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Encaissement> getEncaissementById(@PathVariable Long id) {
        return encaissementRepository.findById(id)
                .map(encaissement -> ResponseEntity.ok().body(encaissement))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Encaissement createEncaissement(@RequestBody Encaissement encaissement) {
        return encaissementRepository.save(encaissement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Encaissement> updateEncaissement(@PathVariable Long id, @RequestBody Encaissement encaissement) {
        return encaissementRepository.findById(id)
                .map(existingEncaissement -> {
                    existingEncaissement.setMontantEncaisse(encaissement.getMontantEncaisse());
                    existingEncaissement.setDocumentFacture(encaissement.getDocumentFacture());
                    existingEncaissement.setDateEncaissement(encaissement.getDateEncaissement());
                    existingEncaissement.setFacturation(encaissement.getFacturation());
                    return ResponseEntity.ok().body(encaissementRepository.save(existingEncaissement));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEncaissement(@PathVariable Long id) {
        return encaissementRepository.findById(id)
                .map(encaissement -> {
                    encaissementRepository.delete(encaissement);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
