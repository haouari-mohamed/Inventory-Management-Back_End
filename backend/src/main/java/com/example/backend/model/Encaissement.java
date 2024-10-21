package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Encaissement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Encaissement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_encaissement;

    @Column(name = "montant_encaisse", nullable = false)
    private Double montantEncaisse;

    @Column(name = "document_facture", nullable = false)
    private String documentFacture;

    @Column(name = "date_encaissement")
    @Temporal(TemporalType.DATE)
    private Date dateEncaissement;

    @ManyToOne
    @JoinColumn(name = "id_facture")
    private Facturation facturation;
}
