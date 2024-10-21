package com.example.backend.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncaissementDTO {
    private Long id_encaissement;
    private Double montantEncaisse;
    private String documentFacture;
    private Date dateEncaissement;
    private Long facturationId;
}