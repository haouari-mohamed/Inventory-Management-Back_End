package com.example.backend.repository;

import com.example.backend.model.Affaire;
import com.example.backend.model.Division;
import com.example.backend.model.Pole;
import com.example.backend.model.StatusAffaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AffaireRepository extends JpaRepository<Affaire, Long> {
    Optional<Affaire> findTopByOrderByIdAffaireDesc();
    long countByStatusAffaire(StatusAffaire statusAffaire);
    long countByStatusAffaireAndDateFinBetween(StatusAffaire statusAffaire, Date startDate, Date endDate);
    long countByPolePrincipale(Pole pole);
    long countByPolePrincipaleAndStatusAffaire(Pole pole, StatusAffaire statusAffaire);
    
    @Query("SELECT COUNT(a) FROM Affaire a WHERE a.polePrincipale = :pole AND a.statusAffaire = :status AND MONTH(a.dateFin) = :month")
    long countByPolePrincipaleAndStatusAffaireAndMonth(Pole pole, StatusAffaire status, int month);
    
    @Query("SELECT MONTH(a.dateFin) as month, a.statusAffaire as status, COUNT(a) as count " +
           "FROM Affaire a " +
           "WHERE a.polePrincipale = :pole " +
           "AND YEAR(a.dateFin) = :year " +
           "GROUP BY MONTH(a.dateFin), a.statusAffaire")
    List<Object[]> countByPolePrincipaleAndStatusAffaireGroupByMonth(Pole pole, int year);
    
    long countByDivisionPrincipale(Division division);
    long countByDivisionPrincipaleAndStatusAffaire(Division division, StatusAffaire statusAffaire);

    @Query("SELECT MONTH(a.dateFin) as month, a.statusAffaire as status, COUNT(a) as count " +
           "FROM Affaire a " +
           "WHERE a.divisionPrincipale = :division " +
           "AND YEAR(a.dateFin) = :year " +
           "GROUP BY MONTH(a.dateFin), a.statusAffaire")
    List<Object[]> countByDivisionPrincipaleAndStatusAffaireGroupByMonth(Division division, int year);
    
    List<Affaire> findByDivisionPrincipale(Division divisionPrincipale);
}
