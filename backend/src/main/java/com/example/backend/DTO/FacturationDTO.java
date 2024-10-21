package com.example.backend.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturationDTO {
    private Long id_facture;
    private Double montantFacture;
    private String documentFacture;
    private Date dateFacturation;
    private Long missionId;
}