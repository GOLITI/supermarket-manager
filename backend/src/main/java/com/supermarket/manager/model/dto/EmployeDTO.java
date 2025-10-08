package com.supermarket.manager.model.dto;

import com.supermarket.manager.model.rh.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeDTO {
    private Long id;
    private String matricule;
    private String nom;
    private String prenom;
    private String nomComplet;
    private String email;
    private String telephone;
    private LocalDate dateNaissance;
    private String adresse;
    private LocalDate dateEmbauche;
    private PosteEmploye poste;
    private String posteLibelle;
    private StatutEmploye statut;
    private TypeContrat typeContrat;
    private String typeContratLibelle;
    private Double salaireBase;
    private Integer nombreAbsences;
    private Integer nombreJoursConge;
}

