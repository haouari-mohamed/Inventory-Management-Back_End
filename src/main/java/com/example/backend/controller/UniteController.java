package com.example.backend.controller;

import com.example.backend.model.Unite;
import com.example.backend.service.UniteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unites")
public class UniteController {

    @Autowired
    private UniteService uniteService;

    @GetMapping
    public List<Unite> getAllUnites() {
        return uniteService.getAllUnites();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Unite> getUniteById(@PathVariable Long id) {
        return uniteService.getUniteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Unite> createUnite(@RequestBody Unite unite) {
        Unite createdUnite = uniteService.createUnite(unite);
        return ResponseEntity.ok(createdUnite);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Unite> updateUnite(@PathVariable Long id, @RequestBody Unite uniteDetails) {
        return uniteService.updateUnite(id, uniteDetails)
                .map(updatedUnite -> ResponseEntity.ok().body(updatedUnite))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")

    public ResponseEntity<?> deleteUnite(@PathVariable Long id) {
        boolean isDeleted = uniteService.deleteUnite(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
