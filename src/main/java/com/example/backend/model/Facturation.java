package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Facturation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facturation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_facture;

    @Column(name = "montant_facture", nullable = false)
    private Double montantFacture;

    @Column(name = "document_facture", nullable = false)
    private String documentFacture;

//    @Lob
//    private byte[] fileContent;

    @Column(name = "date_facturation")
    @Temporal(TemporalType.DATE)
    private Date dateFacturation;

    @ManyToOne
    @JoinColumn(name = "id_mission")
    private Mission mission;
}
