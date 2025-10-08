package com.supermarket.manager.model.dto;

import com.supermarket.manager.model.rh.TypeAbsence;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandeAbsenceRequest {
    
    @NotNull(message = "L'ID de l'employé est obligatoire")
    private Long employeId;
    
    @NotNull(message = "Le type d'absence est obligatoire")
    private TypeAbsence type;
    
    @NotNull(message = "La date de début est obligatoire")
    @FutureOrPresent(message = "La date de début doit être aujourd'hui ou dans le futur")
    private LocalDate dateDebut;
    
    @NotNull(message = "La date de fin est obligatoire")
    @FutureOrPresent(message = "La date de fin doit être aujourd'hui ou dans le futur")
    private LocalDate dateFin;
    
    @NotBlank(message = "Le motif est obligatoire")
    @Size(max = 1000, message = "Le motif ne peut pas dépasser 1000 caractères")
    private String motif;
    
    private String documentJustificatif;
}

