package com.supermarket.manager.repository;

import com.supermarket.manager.model.rh.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    
    List<Evaluation> findByEmployeId(Long employeId);
    
    List<Evaluation> findByEvaluateurId(Long evaluateurId);
    
    @Query("SELECT e FROM Evaluation e WHERE e.employe.id = :employeId " +
           "ORDER BY e.dateEvaluation DESC")
    List<Evaluation> findByEmployeIdOrderByDateDesc(@Param("employeId") Long employeId);
    
    @Query("SELECT e FROM Evaluation e WHERE e.dateEvaluation >= :debut " +
           "AND e.dateEvaluation <= :fin")
    List<Evaluation> findByPeriode(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);
    
    @Query("SELECT AVG(e.noteGlobale) FROM Evaluation e WHERE e.employe.id = :employeId")
    Double getNoteMoyenneEmploye(@Param("employeId") Long employeId);
}

