package com.example.backend.repository;

import com.example.backend.model.Facturation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturationRepository extends JpaRepository<Facturation, Long> {
}
