package com.example.backend.service;

import com.example.backend.model.Facturation;
import com.example.backend.model.Mission;
import com.example.backend.repository.FacturationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FacturationService {
    @Autowired
    private FacturationRepository facturationRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public List<Facturation> getAllFacturations() {
        return facturationRepository.findAll();
    }

    public Facturation getFacturationById(Long id) {
        return facturationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facturation not found"));
    }
    public List<Facturation> getFacturationByIdMission(Long id){
        return facturationRepository.getFacturationMissionId(id);
    }

    public Facturation createFacturation(Double montantFacture, Date dateFacturation, Long id_mission, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        // Save file
        Path targetLocation = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Create Facturation object
        Facturation facturation = new Facturation();
        facturation.setMontantFacture(montantFacture);
        facturation.setDocumentFacture(uniqueFileName);
        facturation.setDateFacturation(dateFacturation);

        // You might want to set the mission here
        // facturation.setMission(missionRepository.findById(id_mission).orElseThrow());

        return facturationRepository.save(facturation);
    }

    public Facturation updateFacturation(Long id, Double montantFacture, Date dateFacturation, Long id_mission, MultipartFile file) throws IOException {
        Facturation existingFacturation = getFacturationById(id);

        existingFacturation.setMontantFacture(montantFacture);
        existingFacturation.setDateFacturation(dateFacturation);

        // Update file if provided
        if (file != null && !file.isEmpty()) {
            // Delete old file if exists
            deleteOldFile(existingFacturation.getDocumentFacture());

            // Generate and save new file
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            Path targetLocation = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            existingFacturation.setDocumentFacture(uniqueFileName);
        }

        return facturationRepository.save(existingFacturation);
    }

    private void deleteOldFile(String fileName) throws IOException {
        if (fileName != null && !fileName.isEmpty()) {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            Files.deleteIfExists(filePath);
        }
    }

    public void deleteFacturationById(Long id) {
        Facturation facturation = getFacturationById(id);

        // Delete associated file
        try {
            deleteOldFile(facturation.getDocumentFacture());
        } catch (IOException e) {
            // Log the error, but don't prevent deletion of the database record
            System.err.println("Could not delete file: " + e.getMessage());
        }

        // Delete database record
        facturationRepository.deleteById(id);
    }
}