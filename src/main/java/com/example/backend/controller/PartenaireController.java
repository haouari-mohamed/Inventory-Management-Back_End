package com.example.backend.controller;

import com.example.backend.model.Partenaire;
import com.example.backend.repository.PartenaireRepository;
import com.example.backend.service.PartenaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partenaires")
public class PartenaireController {

    @Autowired
    private PartenaireService partenaireService;

    @GetMapping
    public List<Partenaire> getAllPartenaires() {
        return partenaireService.getAllPartenaire();
    }

    @GetMapping("/{id}")
    public Partenaire getPartenaireById(@PathVariable Long id) {
        return partenaireService.getPartenaireById(id);
    }

    @PostMapping
    public Partenaire createPartenaire(@RequestBody Partenaire partenaire) {
        return partenaireService.createPartenaire(partenaire);
    }

    @PutMapping("/{id}")
    public Partenaire updatePartenaire(@PathVariable Long id, @RequestBody Partenaire partenaireDetails) {
        return partenaireService.updatePartenaire(id,partenaireDetails);
    }

    @DeleteMapping("/{id}")
    public void deletePartenaire(@PathVariable Long id) {
        partenaireService.deletePartenaire(id);
    }
}