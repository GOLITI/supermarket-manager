package com.supermarket.manager.service;

import com.supermarket.manager.model.dto.PointageDTO;

import java.time.LocalDate;
import java.util.List;

public interface PointageService {
    
    PointageDTO pointer(Long employeId);
    
    PointageDTO obtenirPointage(Long id);
    
    List<PointageDTO> obtenirPointagesEmploye(Long employeId);
    
    List<PointageDTO> obtenirPointagesEmployeParDate(Long employeId, LocalDate date);
    
    List<PointageDTO> obtenirPointagesParPeriode(Long employeId, LocalDate debut, LocalDate fin);
    
    PointageDTO validerPointage(Long id);
    
    Double calculerHeuresTravaillees(Long employeId, LocalDate debut, LocalDate fin);
    
    Double calculerHeuresSupplementaires(Long employeId, LocalDate debut, LocalDate fin);
}

