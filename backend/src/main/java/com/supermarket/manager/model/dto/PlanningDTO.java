package com.supermarket.manager.model.dto;

import com.supermarket.manager.model.rh.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanningDTO {
    private Long id;
    private Long employeId;
    private String employeNom;
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Double dureeHeures;
    private TypeShift typeShift;
    private String typeShiftLibelle;
    private PosteEmploye posteAssigne;
    private String posteAssigneLibelle;
    private String notes;
    private Boolean valide;
}

