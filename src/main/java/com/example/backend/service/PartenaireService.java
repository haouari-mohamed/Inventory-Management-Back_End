package com.example.backend.service;

import com.example.backend.model.Partenaire;
import com.example.backend.repository.PartenaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartenaireService {
    @Autowired
    private PartenaireRepository partenaireRepository;

    public List<Partenaire> getAllPartenaire(){
        return partenaireRepository.findAll();
    }
    public Partenaire getPartenaireById(Long id){
        return partenaireRepository.findById(id).orElseThrow();
    }
    public Partenaire createPartenaire(Partenaire partenaire){
        return partenaireRepository.save(partenaire);
    }
    public void deletePartenaire(Long id){
        partenaireRepository.deleteById(id);
    }
    public Partenaire updatePartenaire(Long id,Partenaire partenaire){
        Partenaire partenaire1=getPartenaireById(id);
        partenaire1.setNom_partenaire(partenaire.getNom_partenaire());
        partenaire1.setId_partenaire(partenaire.getId_partenaire());
        return partenaireRepository.save(partenaire1);
    }

}
