package com.supermarket.manager.service;

import com.supermarket.manager.model.dto.AbsenceDTO;
import com.supermarket.manager.model.dto.DemandeAbsenceRequest;
import com.supermarket.manager.model.dto.ValidationAbsenceRequest;
import com.supermarket.manager.model.rh.StatutDemande;

import java.time.LocalDate;
import java.util.List;

public interface AbsenceService {
    
    AbsenceDTO creerDemandeAbsence(DemandeAbsenceRequest request);
    
    AbsenceDTO obtenirAbsence(Long id);
    
    List<AbsenceDTO> obtenirAbsencesEmploye(Long employeId);
    
    List<AbsenceDTO> obtenirAbsencesEnAttente();
    
    List<AbsenceDTO> obtenirAbsencesParPeriode(LocalDate debut, LocalDate fin);
    
    AbsenceDTO validerAbsence(Long absenceId, ValidationAbsenceRequest request);
    
    AbsenceDTO annulerAbsence(Long absenceId);
    
    List<AbsenceDTO> obtenirAbsencesEnCoursEmploye(Long employeId);
}

