package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Affaire")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Affaire {
    @Id
    @Column(name = "id_affaire")
    private Long idAffaire;

    @Column(nullable = false)
    private String libelle_affaire;

    @Column(name = "prix_global", nullable = false)
    private Double prixGlobal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_affaire", nullable = false)
    private StatusAffaire statusAffaire = StatusAffaire.DEFAULT;

    @Column(name = "num_marche", nullable = false)
    private String marche;

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
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "pole_principale_id", nullable = false)
    private Pole polePrincipale;

    @ManyToOne
    @JoinColumn(name = "division_principale_id", nullable = false)
    private Division divisionPrincipale;

    @Column(name = "part_cid", nullable = false)
    private Double partCID;

    @ManyToOne
    @JoinColumn(name = "chef_projet_id")
    private Utilisateur chefProjet;

    @PrePersist
    public void prePersist() {
        if (this.statusAffaire == null) {
            this.statusAffaire = StatusAffaire.DEFAULT;
        }
    }
}
