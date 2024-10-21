package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Sous_Traitant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SousTraitant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_soustrait;

    @Column(nullable = false)
    private String nom_soustrait;
}