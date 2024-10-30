package com.example.backend.service;

import com.example.backend.model.Facturation;
import com.example.backend.model.Mission;
import com.example.backend.repository.FacturationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacturationService {
    @Autowired
    private FacturationRepository facturationRepository;

    public List<Facturation> getAllFacturations(){
        return facturationRepository.findAll();
    }
    public Facturation getFacturationById(Long id){
        return facturationRepository.findById(id).orElseThrow();
    }
    public Facturation createFacturation(Facturation facturation){
        return facturationRepository.save(facturation);
    }
    public void deleteFacturationById(Long id){
        facturationRepository.findById(id);
    }
    public Facturation updateFacturation(Long id,Facturation facturation){
        Facturation facturation1=getFacturationById(id);
        facturation1.setMontantFacture(facturation.getMontantFacture());
        facturation1.setDocumentFacture(facturation.getDocumentFacture());
        facturation1.setDateFacturation(facturation.getDateFacturation());
        facturation1.setMission(facturation.getMission());
        return facturationRepository.save(facturation1);
    }
    public List<Facturation> getFacturationByIdMission(Long id){
        return facturationRepository.getFacturationMissionId(id);
    }

}
