package com.example.backend.service;

import com.example.backend.model.Encaissement;
import com.example.backend.repository.EncaissementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EncaissementService {
    @Autowired
    private EncaissementRepository encaissementRepository;

    public List<Encaissement> getAllEncaissements(){
        return encaissementRepository.findAll();
    }
    public Encaissement getEncaissementById(Long id){
        return encaissementRepository.findById(id).orElseThrow();
    }
    public Encaissement createEncaissement(Encaissement encaissement){
        return encaissementRepository.save(encaissement);
    }
    public void deleteEncaissement(Long id){
        encaissementRepository.deleteById(id);
    }
    public Encaissement updateEncaissement(Long id,Encaissement encaissement){
        Encaissement encaissement1=getEncaissementById(id);
        encaissement1.setDateEncaissement(encaissement.getDateEncaissement());
        encaissement1.setMontantEncaisse(encaissement.getMontantEncaisse());
        encaissement1.setFacturation(encaissement.getFacturation());
        encaissement1.setDocumentFacture(encaissement.getDocumentFacture());
        return encaissementRepository.save(encaissement1);
    }
    public List<Encaissement> getEncaissementByFacture(Long id){
        return encaissementRepository.getEncaissementFactureId(id);
    }

}
