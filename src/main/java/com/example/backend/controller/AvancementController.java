package com.example.backend.controller;

import com.example.backend.model.Avancement;
import com.example.backend.model.Mission;
import com.example.backend.repository.MissionRepository;
import com.example.backend.service.AvancementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/avancements")
public class  AvancementController {

    @Autowired
    private AvancementService avancementService;
    @Autowired
    private MissionRepository missionRepository;



    @GetMapping("/mission/{id_mission}")
    public ResponseEntity<List<Avancement>> getAvancementsByMission(@PathVariable Long id_mission) {
        List<Avancement> avancements = avancementService.getAvancementsByMission(id_mission);
        return ResponseEntity.ok(avancements);
    }

    @PostMapping
    public Avancement createAvancement(@RequestBody Avancement avancement) {
        return avancementService.saveAvancement(avancement);
    }

//    @PostMapping
//    public ResponseEntity<Avancement> createAvancement(@RequestBody Avancement avancement) {
//        if (avancement.getMission() == null || avancement.getMission().getId_mission() == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//
//        // Set mission to avancement if needed or fetch mission if necessary
//        Avancement savedAvancement = avancementService.saveAvancement(avancement);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedAvancement);
//    }

    @PutMapping("/{id_avancement}")
    public ResponseEntity<Avancement> updateAvancement(@PathVariable Long id_avancement, @RequestBody Avancement avancement) {
        Avancement updatedAvancement = avancementService.updateAvancement(id_avancement, avancement);
        return ResponseEntity.ok(updatedAvancement);
    }

    @DeleteMapping("/{id_avancement}")
    public ResponseEntity<Void> deleteAvancement(@PathVariable Long id_avancement) {
        avancementService.deleteAvancement(id_avancement);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mission/{id_mission}/percentage")
    public ResponseEntity<Double> calculateAvancementPercentage(@PathVariable Long id_mission) {
        Double avancementPercentage = avancementService.calculateAvancementPercentage(id_mission);
        return ResponseEntity.ok(avancementPercentage);
    }
}