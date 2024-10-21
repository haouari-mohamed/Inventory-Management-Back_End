package com.example.backend.repository;

import com.example.backend.model.Pole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoleRepository extends JpaRepository<Pole, Long> {
    // Additional custom methods if needed
}
