package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Pays")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pays {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pays;

    @Column(nullable = false, length = 100)
    private String libelle_pays;
}
