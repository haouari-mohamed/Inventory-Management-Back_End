package com.example.backend.controller;

import com.example.backend.model.Division;
import com.example.backend.repository.DivisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/divisions")
public class DivisionController {

    @Autowired
    private DivisionRepository divisionRepository;

    @GetMapping
    public List<Division> getAllDivisions() {
        return divisionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Division> getDivisionById(@PathVariable Long id) {
        return divisionRepository.findById(id)
                .map(division -> ResponseEntity.ok().body(division))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Division createDivision(@RequestBody Division division) {
        return divisionRepository.save(division);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Division> updateDivision(@PathVariable Long id, @RequestBody Division division) {
        return divisionRepository.findById(id)
                .map(existingDivision -> {
                    existingDivision.setNom_division(division.getNom_division());
                    existingDivision.setPole(division.getPole());
                    return ResponseEntity.ok().body(divisionRepository.save(existingDivision));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDivision(@PathVariable Long id) {
        return divisionRepository.findById(id)
                .map(division -> {
                    divisionRepository.delete(division);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
