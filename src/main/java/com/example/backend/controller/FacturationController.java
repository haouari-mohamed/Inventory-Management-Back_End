package com.example.backend.controller;

import com.example.backend.model.Facturation;
import com.example.backend.service.FacturationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/facturations")
public class FacturationController {

    @Autowired
    private FacturationService facturationService;

    @GetMapping
    public List<Facturation> getAllFacturations() {
        return facturationService.getAllFacturations();
    }

    @GetMapping("/{id}")
    public Facturation getFacturationById(@PathVariable Long id) {
        return facturationService.getFacturationById(id);
    }

    @PostMapping
    public Facturation createFacturation(@RequestBody Facturation facturation) {
        return facturationService.createFacturation(facturation);
    }

    @PutMapping("/{id}")
    public Facturation updateFacturation(@PathVariable Long id, @RequestBody Facturation facturation) {
        return facturationService.updateFacturation(id,facturation);
    }

    @DeleteMapping("/{id}")
    public void deleteFacturation(@PathVariable Long id) {
         facturationService.deleteFacturationById(id);
    }

    }


