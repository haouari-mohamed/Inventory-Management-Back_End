package com.example.backend.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoleDTO {
    private Long id_pole;
    private String libelle_pole;
}