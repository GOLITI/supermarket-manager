package com.supermarket.manager.service;

import com.supermarket.manager.model.dto.EmployeDTO;
import com.supermarket.manager.model.rh.PosteEmploye;
import com.supermarket.manager.model.rh.StatutEmploye;

import java.util.List;

public interface EmployeService {

    EmployeDTO creerEmploye(EmployeDTO employeDTO);

    EmployeDTO obtenirEmploye(Long id);

    EmployeDTO obtenirParMatricule(String matricule);

    List<EmployeDTO> obtenirTousLesEmployes();

    List<EmployeDTO> obtenirEmployesActifs();

    List<EmployeDTO> obtenirEmployesParPoste(PosteEmploye poste);

    List<EmployeDTO> rechercherEmployes(String search);

    EmployeDTO modifierEmploye(Long id, EmployeDTO employeDTO);

    EmployeDTO changerStatut(Long id, StatutEmploye statut);

    void supprimerEmploye(Long id);
}

