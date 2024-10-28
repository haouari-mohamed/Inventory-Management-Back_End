package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "MissionChefProjet")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionChefProjet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @ManyToOne
    @JoinColumn(name = "chef_projet_id", nullable = false)
    private Utilisateur chefProjet;

    @Column(name = "date_affectation", nullable = false)
    private LocalDate dateAffectation;

}
