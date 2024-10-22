package com.example.backend.controller;

import com.example.backend.model.Division;
import com.example.backend.repository.DivisionRepository;
import com.example.backend.service.DivisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/divisions")
public class DivisionController {

    @Autowired
    private DivisionService divisionService;

    @GetMapping
    public List<Division> getAllDivisions() {
        return divisionService.getAllDivision();
    }

    @GetMapping("/{id}")
    public Division getDivisionById(@PathVariable Long id) {
        return divisionService.getDivisionById(id);
    }

    @PostMapping
    public Division createDivision(@RequestBody Division division) {
        return divisionService.createDivision(division);
    }

    @PutMapping("/{id}")
    public Division updateDivision(@PathVariable Long id, @RequestBody Division division) {
        return divisionService.updateDivision(id,division);
    }

    @DeleteMapping("/{id}")
    public void deleteDivision(@PathVariable Long id) {
        divisionService.deletedivision(id);
    }

    }


