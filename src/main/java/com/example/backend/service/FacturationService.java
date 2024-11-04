package com.example.backend.service;

import com.example.backend.repository.MissionRepository;
import com.example.backend.model.Facturation;
import com.example.backend.repository.FacturationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FacturationService {
    @Autowired
    private FacturationRepository facturationRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;


    private Path getUploadPath() {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
        return uploadPath;
    }

    // Download a file by its ID
    public ResponseEntity<Resource> downloadFile(Long id_facture) {
        try {
            Optional<Facturation> facturationOpt = facturationRepository.findById(id_facture);
            if (facturationOpt.isPresent()) {
                String fileName = facturationOpt.get().getDocumentFacture();
                Path filePath = getUploadPath().resolve(fileName).normalize();
                Resource resource = new UrlResource(filePath.toUri());

                if (resource.exists()) {
                    String contentType = Files.probeContentType(filePath);
                    if (contentType == null) {
                        contentType = "application/octet-stream"; 
                    }

                    // Create response headers
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
                    headers.add(HttpHeaders.CONTENT_TYPE, contentType);
                    headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
                    headers.add(HttpHeaders.PRAGMA, "no-cache");
                    headers.add(HttpHeaders.EXPIRES, "0");

                    return ResponseEntity.ok()
                            .headers(headers)
                            .contentType(MediaType.parseMediaType(contentType))
                            .body(resource);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Retrieve all Facturation records
    public List<Facturation> getAllFacturations() {
        return facturationRepository.findAll();
    }

    // Retrieve a Facturation by its ID
    public Facturation getFacturationById(Long id) {
        return facturationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facturation not found"));
    }

    // Retrieve Facturation records by Mission ID
    public List<Facturation> getFacturationByIdMission(Long id) {
        return facturationRepository.getFacturationMissionId(id);
    }

    // Create a new Facturation
    public Facturation createFacturation(Double montantFacture, Date dateFacturation, Long id_mission, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName.contains("..")) {
            throw new RuntimeException("Invalid file path sequence in filename: " + fileName);
        }

        // Save file to disk
        Path targetLocation = getUploadPath().resolve(fileName);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename(), e);
        }

        // Create and save Facturation entity
        Facturation facturation = new Facturation();
        facturation.setMontantFacture(montantFacture);
        facturation.setDocumentFacture(fileName);
        facturation.setDateFacturation(dateFacturation);
        facturation.setMission(missionRepository.findById(id_mission)
                .orElseThrow(() -> new RuntimeException("Mission not found")));

        return facturationRepository.save(facturation);
    }

    // Update an existing Facturation
    public Facturation updateFacturation(Long id, Double montantFacture, Date dateFacturation, Long id_mission, MultipartFile file) {
        Facturation existingFacturation = getFacturationById(id);
        existingFacturation.setMontantFacture(montantFacture);
        existingFacturation.setDateFacturation(dateFacturation);

        // Update file if provided
        if (file != null && !file.isEmpty()) {
            // Delete old file if it exists
            String oldFilename = existingFacturation.getDocumentFacture();
            if (oldFilename != null) {
                Path oldFilePath = getUploadPath().resolve(oldFilename);
                try {
                    Files.deleteIfExists(oldFilePath);
                } catch (IOException e) {
                    throw new RuntimeException("Could not delete old file", e);
                }
            }

            // Save new file with original filename
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileName.contains("..")) {
                throw new RuntimeException("Invalid file path sequence in filename: " + fileName);
            }

            Path targetLocation = getUploadPath().resolve(fileName);
            try {
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                existingFacturation.setDocumentFacture(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Could not store file", e);
            }
        }

        return facturationRepository.save(existingFacturation);
    }

    // Delete a Facturation by its ID
    public void deleteFacturationById(Long id) {
        Facturation facturation = getFacturationById(id);
        String filename = facturation.getDocumentFacture();

        // Delete file from disk
        if (filename != null) {
            Path filePath = getUploadPath().resolve(filename);
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Could not delete file", e);
            }
        }

        // Delete database record
        facturationRepository.deleteById(id);
    }
}
