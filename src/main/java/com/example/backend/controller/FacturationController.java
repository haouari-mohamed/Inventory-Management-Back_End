package com.example.backend.controller;

import com.example.backend.model.Facturation;
import com.example.backend.service.FacturationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/facturations")
public class FacturationController {

    @Autowired
    private FacturationService facturationService;

    @GetMapping
    public List<Facturation> getAllFacturations() {
        return facturationService.getAllFacturations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facturation> getFacturationById(@PathVariable Long id) {
        Facturation facturation = facturationService.getFacturationById(id);
        return ResponseEntity.ok(facturation);
    }

    @PostMapping
    public ResponseEntity<?> createFacturation(
            @RequestParam("montantFacture") Double montantFacture,
            @RequestParam("dateFacturation") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFacturation,
            @RequestParam("id_mission") Long id_mission,
            @RequestParam("file") MultipartFile file) {
        try {
            Facturation facturation = facturationService.createFacturation(montantFacture, dateFacturation, id_mission, file);
            return new ResponseEntity<>(facturation, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFacturation(
            @PathVariable Long id,
            @RequestParam("montantFacture") Double montantFacture,
            @RequestParam("dateFacturation") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFacturation,
            @RequestParam("id_mission") Long id_mission,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            Facturation updatedFacturation = facturationService.updateFacturation(id, montantFacture, dateFacturation, id_mission, file);
            return ResponseEntity.ok(updatedFacturation);
        } catch (IOException e) {
            return new ResponseEntity<>("File update failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacturation(@PathVariable Long id) {
        facturationService.deleteFacturationById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}