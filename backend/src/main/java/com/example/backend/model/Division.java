package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Division")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Division {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_division;

    @Column(nullable = false)
    private String nom_division;

    @ManyToOne
    @JoinColumn(name = "id_pole", nullable = false)
    private Pole pole;
}
