package com.example.backend.repository;

import com.example.backend.model.SousTraitant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SousTraitantRepository extends JpaRepository<SousTraitant, Long> {
}