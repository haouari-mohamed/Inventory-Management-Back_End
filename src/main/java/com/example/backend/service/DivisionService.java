package com.example.backend.service;

import com.example.backend.model.Division;
import com.example.backend.repository.DivisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DivisionService {
    @Autowired
    private DivisionRepository divisionRepository;

    public List<Division> getAllDivision(){
        return divisionRepository.findAll();
    }
    public Division getDivisionById(Long id){
        return divisionRepository.findById(id).orElseThrow();
    }
    public Division createDivision(Division division){
        return divisionRepository.save(division);
    }
    public void deletedivision(Long id){
         divisionRepository.deleteById(id);
    }
    public Division updateDivision(Long id ,Division division){
        Division division1=getDivisionById(id);
        division1.setNom_division(division.getNom_division());
        division1.setId_division(division.getId_division());
        return divisionRepository.save(division1);
    }
}
