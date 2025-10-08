package com.supermarket.manager.repository;

import com.supermarket.manager.model.rh.Planning;
import com.supermarket.manager.model.rh.PosteEmploye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlanningRepository extends JpaRepository<Planning, Long> {
    
    List<Planning> findByEmployeId(Long employeId);
    
    List<Planning> findByDate(LocalDate date);
    
    List<Planning> findByEmployeIdAndDate(Long employeId, LocalDate date);
    
    @Query("SELECT p FROM Planning p WHERE p.employe.id = :employeId " +
           "AND p.date >= :debut AND p.date <= :fin")
    List<Planning> findByEmployeAndPeriode(@Param("employeId") Long employeId,
                                           @Param("debut") LocalDate debut,
                                           @Param("fin") LocalDate fin);
    
    @Query("SELECT p FROM Planning p WHERE p.date >= :debut AND p.date <= :fin")
    List<Planning> findByPeriode(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);
    
    List<Planning> findByDateAndPosteAssigne(LocalDate date, PosteEmploye poste);
    
    @Query("SELECT p FROM Planning p WHERE p.date = :date AND p.valide = true")
    List<Planning> findPlanningsValidesParDate(@Param("date") LocalDate date);
    
    @Query("SELECT COUNT(p) FROM Planning p WHERE p.date = :date AND p.posteAssigne = :poste")
    long countByDateAndPoste(@Param("date") LocalDate date, @Param("poste") PosteEmploye poste);
}

