package com.example.backend.repository;

import com.example.backend.model.Division;
import com.example.backend.model.Pole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Long> {
    boolean existsByPole(Pole pole);
    void deleteByPole(Pole pole);
}
