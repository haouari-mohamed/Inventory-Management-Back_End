package com.example.backend.repository;

import com.example.backend.model.Encaissement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncaissementRepository extends JpaRepository<Encaissement, Long> {
}
