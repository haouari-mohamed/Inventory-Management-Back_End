package com.example.backend.service;

import com.example.backend.repository.MissionRepository;
import com.example.backend.model.Facturation;
import com.example.backend.repository.FacturationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
public class FacturationService {
    @Autowired
    private FacturationRepository facturationRepository;

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private MissionRepository missionRepository;

    // Method for downloading files if needed
    public ResponseEntity<Resource> downloadFile(String fileName) {
        try {
            // Prepend your files directory path here
            String filePath = "/path/to/your/files/" + fileName;
            Resource resource = resourceLoader.getResource("file:" + filePath);

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    public List<Facturation> getAllFacturations() {
        return facturationRepository.findAll();
    }

    public Facturation getFacturationById(Long id) {
        return facturationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facturation not found"));
    }

    public List<Facturation> getFacturationByIdMission(Long id) {
        return facturationRepository.getFacturationMissionId(id);
    }

    public Facturation createFacturation(Double montantFacture, Date dateFacturation, Long id_mission, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        // original filename
        String originalFilename = file.getOriginalFilename();

        // store origin  filename in the database
        Facturation facturation = new Facturation();
        facturation.setMontantFacture(montantFacture);
        facturation.setDocumentFacture(originalFilename);
        facturation.setDateFacturation(dateFacturation);

        // Associate the mission if exists
        facturation.setMission(missionRepository.findById(id_mission).orElseThrow());

        return facturationRepository.save(facturation);
    }

    public Facturation updateFacturation(Long id, Double montantFacture, Date dateFacturation, Long id_mission, MultipartFile file) {
        Facturation existingFacturation = getFacturationById(id);

        existingFacturation.setMontantFacture(montantFacture);
        existingFacturation.setDateFacturation(dateFacturation);

        // Update filename if a new file is provided
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            existingFacturation.setDocumentFacture(originalFilename);
        }

        return facturationRepository.save(existingFacturation);
    }

    public void deleteFacturationById(Long id) {
        facturationRepository.deleteById(id);
    }
}
