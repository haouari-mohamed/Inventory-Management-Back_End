package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "Avancement")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Avancement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_avancement;

    @ManyToOne
    @JoinColumn(name = "mission_fk", referencedColumnName = "id_mission", nullable = false)
    private Mission mission;

    @Column(name = "date_mise_a_jour", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime dateMiseAJour;


    @Column(name = "montant_avancement", nullable = false)
    private Double montantAvancement;

    @Column(name = "commentaire", length = 1000)
    private String commentaire;

    @ManyToOne
    @JoinColumn(name = "chef_projet_id", nullable = false)
    private Utilisateur chefProjet;
}