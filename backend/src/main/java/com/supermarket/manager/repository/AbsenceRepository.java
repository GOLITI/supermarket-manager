package com.supermarket.manager.repository;

import com.supermarket.manager.model.rh.Absence;
import com.supermarket.manager.model.rh.StatutDemande;
import com.supermarket.manager.model.rh.TypeAbsence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    
    List<Absence> findByEmployeId(Long employeId);
    
    List<Absence> findByStatut(StatutDemande statut);
    
    List<Absence> findByEmployeIdAndStatut(Long employeId, StatutDemande statut);
    
    @Query("SELECT a FROM Absence a WHERE a.employe.id = :employeId " +
           "AND a.dateDebut <= :date AND a.dateFin >= :date")
    List<Absence> findAbsencesEnCours(@Param("employeId") Long employeId, @Param("date") LocalDate date);
    
    @Query("SELECT a FROM Absence a WHERE a.dateDebut >= :debut AND a.dateFin <= :fin")
    List<Absence> findByPeriode(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);
    
    @Query("SELECT a FROM Absence a WHERE a.employe.id = :employeId " +
           "AND a.dateDebut >= :debut AND a.dateFin <= :fin")
    List<Absence> findByEmployeAndPeriode(@Param("employeId") Long employeId, 
                                          @Param("debut") LocalDate debut, 
                                          @Param("fin") LocalDate fin);
    
    @Query("SELECT a FROM Absence a WHERE a.employe = :employe " +
           "AND ((a.dateDebut BETWEEN :debut AND :fin) OR (a.dateFin BETWEEN :debut AND :fin) " +
           "OR (a.dateDebut <= :debut AND a.dateFin >= :fin))")
    List<Absence> findByEmployeAndPeriodeOverlap(@Param("employe") com.supermarket.manager.model.rh.Employe employe,
                                                   @Param("debut") LocalDate debut,
                                                   @Param("fin") LocalDate fin);

    List<Absence> findByEmploye(com.supermarket.manager.model.rh.Employe employe);

    long countByEmployeIdAndType(Long employeId, TypeAbsence type);
    
    @Query("SELECT COUNT(a) FROM Absence a WHERE a.statut = :statut")
    long countByStatut(@Param("statut") StatutDemande statut);
}
