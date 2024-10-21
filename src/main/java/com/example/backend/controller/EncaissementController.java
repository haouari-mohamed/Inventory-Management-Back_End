package com.example.backend.controller;

import com.example.backend.model.Encaissement;
import com.example.backend.repository.EncaissementRepository;
import com.example.backend.service.EncaissementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encaissements")
public class EncaissementController {

    @Autowired
    private EncaissementService encaissementService;

    @GetMapping
    public List<Encaissement> getAllEncaissements() {
        return encaissementService.getAllEncaissements();
    }

    @GetMapping("/{id}")
    public Encaissement getEncaissementById(@PathVariable Long id) {
        return encaissementService.getEncaissementById(id);
    }

    @PostMapping
    public Encaissement createEncaissement(@RequestBody Encaissement encaissement) {
        return encaissementService.createEncaissement(encaissement);
    }

    @PutMapping("/{id}")
    public Encaissement updateEncaissement(@PathVariable Long id, @RequestBody Encaissement encaissement) {
        return encaissementService.updateEncaissement(id,encaissement);
    }

    @DeleteMapping("/{id}")
    public void deleteEncaissement(@PathVariable Long id) {
        encaissementService.deleteEncaissement(id);
    }
}
