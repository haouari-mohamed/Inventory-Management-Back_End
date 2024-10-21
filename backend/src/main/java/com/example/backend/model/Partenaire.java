package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Partenaire")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Partenaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_partenaire;

    @Column(nullable = false)
    private String nom_partenaire;
}