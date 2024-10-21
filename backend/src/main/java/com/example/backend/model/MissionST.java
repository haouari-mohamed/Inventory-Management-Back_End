package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Mission_Sous_Traitant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissionST {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sous_traitant_id", nullable = false)
    private SousTraitant sousTraitant;

    @Column(name = "part_mission", nullable = true)
    private Double partMission;

}