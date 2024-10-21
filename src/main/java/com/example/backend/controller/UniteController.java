package com.example.backend.controller;

import com.example.backend.model.Unite;
import com.example.backend.repository.UniteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unites")
public class UniteController {

    @Autowired
    private UniteRepository uniteRepository;

    @GetMapping
    public List<Unite> getAllUnites() {
        return uniteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Unite> getUniteById(@PathVariable Long id) {
        return uniteRepository.findById(id)
                .map(unite -> ResponseEntity.ok().body(unite))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Unite createUnite(@RequestBody Unite unite) {
        return uniteRepository.save(unite);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Unite> updateUnite(@PathVariable Long id, @RequestBody Unite unite) {
        return uniteRepository.findById(id)
                .map(existingUnite -> {
                    existingUnite.setNom_unite(unite.getNom_unite());
                    return ResponseEntity.ok().body(uniteRepository.save(existingUnite));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUnite(@PathVariable Long id) {
        return uniteRepository.findById(id)
                .map(unite -> {
                    uniteRepository.delete(unite);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
