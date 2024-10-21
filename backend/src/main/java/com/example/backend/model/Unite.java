package com.example.backend.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Unite")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Unite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_unite;

    @Column(nullable = false)
    private String nom_unite;
}
