package com.example.backend.controller;

import com.example.backend.model.Pays;
import com.example.backend.repository.PaysRepository;
import com.example.backend.service.PaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pays")
public class PaysController {

    @Autowired
    private PaysService paysService;


    @GetMapping
    public List<Pays> getAllPays() {
        return paysService.getAllPays();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Pays> getPaysById(@PathVariable Long id) {
        return paysService.getPaysById(id)
                .map(pays -> ResponseEntity.ok().body(pays))
                .orElse(ResponseEntity.notFound().build());
    }



   @PostMapping
   public Pays CreatePays(@RequestBody Pays pays) {
        return paysService.CreatePays(pays);
   }


    @PutMapping("/{id}")
    public ResponseEntity<Pays> updatePays(@PathVariable Long id, @RequestBody Pays pays) {
        return paysService.updatePays(id, pays)
                .map(updatedPays -> ResponseEntity.ok().body(updatedPays))
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePays(@PathVariable Long id) {
        boolean deleted = paysService.deletePays(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
