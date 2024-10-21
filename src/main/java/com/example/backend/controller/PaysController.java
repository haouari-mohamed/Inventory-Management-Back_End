package com.example.backend.controller;

import com.example.backend.model.Pays;
import com.example.backend.repository.PaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pays")
public class PaysController {

    @Autowired
    private PaysRepository paysRepository;

    @GetMapping
    public List<Pays> getAllPays() {
        return paysRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pays> getPaysById(@PathVariable Long id) {
        return paysRepository.findById(id)
                .map(pays -> ResponseEntity.ok().body(pays))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Pays createPays(@RequestBody Pays pays) {
        return paysRepository.save(pays);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pays> updatePays(@PathVariable Long id, @RequestBody Pays pays) {
        return paysRepository.findById(id)
                .map(existingPays -> {
                    existingPays.setLibelle_pays(pays.getLibelle_pays());
                    return ResponseEntity.ok().body(paysRepository.save(existingPays));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePays(@PathVariable Long id) {
        return paysRepository.findById(id)
                .map(pays -> {
                    paysRepository.delete(pays);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
