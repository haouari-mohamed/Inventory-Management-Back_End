package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

@Entity
@Table(name = "Mission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_mission;

    @Column(nullable = false)
    private String libelle_mission;

    @Column(nullable = true)
    private int quantite;

    @ManyToOne
    @JoinColumn(name = "unite_id", nullable = false)
    private Unite unite;

    @Column(name = "prix_mission_total", nullable = false)
    private Double prixMissionTotal;

    @Column(name = "prix_mission_unitaire", nullable = true)
    private Double prixMissionUnitaire;

    @Column(name = "part_mission_CID", nullable = false)
    private Double partMissionCID;

    @Column(name = "compte_client", nullable = false)
    private Double compteClient = 0.0; // Set a default value

    @Column(name = "date_debut", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Column(name = "date_fin", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateFin;

    @Column(name = "date_arret")
    @Temporal(TemporalType.DATE)
    private Date dateArret;

    @Column(name = "date_recommencement")
    @Temporal(TemporalType.DATE)
    private Date dateRecommencement;

    @ManyToOne
    @JoinColumn(name = "affaire_id", nullable = false)
    private Affaire affaire;

    @ManyToOne
    @JoinColumn(name = "principal_division_id", nullable = false)
    private Division principalDivision;

    @Column(name = "part_div_principale")
    private Double partDivPrincipale;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MissionDivision> secondaryDivisions = new HashSet<>();

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MissionST> sousTraitants = new HashSet<>();

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MissionPartenaire> partenaires = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mission)) return false;
        Mission mission = (Mission) o;
        return Objects.equals(getId_mission(), mission.getId_mission());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId_mission());
    }
}
