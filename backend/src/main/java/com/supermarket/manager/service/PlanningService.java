package com.supermarket.manager.service;

import com.supermarket.manager.model.dto.PlanningDTO;

import java.time.LocalDate;
import java.util.List;

public interface PlanningService {

    PlanningDTO creerPlanning(PlanningDTO planningDTO);

    PlanningDTO obtenirPlanning(Long id);

    List<PlanningDTO> obtenirPlanningsEmploye(Long employeId);

    List<PlanningDTO> obtenirPlanningsParDate(LocalDate date);

    List<PlanningDTO> obtenirPlanningsParPeriode(LocalDate debut, LocalDate fin);

    List<PlanningDTO> obtenirPlanningsEmployePeriode(Long employeId, LocalDate debut, LocalDate fin);

    PlanningDTO modifierPlanning(Long id, PlanningDTO planningDTO);

    PlanningDTO validerPlanning(Long id);

    void supprimerPlanning(Long id);

    List<PlanningDTO> genererPlanningHebdomadaire(LocalDate dateDebut);
}

