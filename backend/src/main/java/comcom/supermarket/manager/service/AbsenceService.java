package comcom.supermarket.manager.service;

import comcom.supermarket.manager.model.dto.AbsenceDTO;
import comcom.supermarket.manager.model.dto.DemandeAbsenceRequest;
import comcom.supermarket.manager.model.dto.ValidationAbsenceRequest;
import comcom.supermarket.manager.model.rh.StatutDemande;

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

