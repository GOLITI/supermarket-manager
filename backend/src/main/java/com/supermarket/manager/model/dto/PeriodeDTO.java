package com.supermarket.manager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeriodeDTO {
    private String debut;
    private String fin;
    private String type; // JOUR, SEMAINE, MOIS, ANNEE
}

