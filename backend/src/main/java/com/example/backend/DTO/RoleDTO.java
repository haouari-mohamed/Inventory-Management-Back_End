package com.example.backend.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long id_role;
    private String nom_role;
    private boolean requiresDivision;
    private boolean requiresPole;
    private String redirectionLink;
}