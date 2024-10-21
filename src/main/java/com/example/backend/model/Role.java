package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_role;

    @Column(nullable = false)
    private String nom_role;

    @Column(nullable = false)
    private boolean requiresDivision;

    @Column(nullable = false)
    private boolean requiresPole;

    @Column(nullable = false)
    private String redirectionLink;
}