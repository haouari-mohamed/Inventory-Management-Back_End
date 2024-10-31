package com.example.backend.repository;

import com.example.backend.model.Encaissement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EncaissementRepository extends JpaRepository<Encaissement, Long> {
    @Query("select e from Encaissement e where e.facturation.id_facture =:id")
    List<Encaissement> getEncaissementFactureId(@Param("id")Long id);
}
