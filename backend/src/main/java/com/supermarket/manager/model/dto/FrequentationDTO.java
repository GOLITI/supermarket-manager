package com.supermarket.manager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrequentationDTO {
    private Integer clientsTotal;
    private Integer clientsMoyenParJour;
    private String jourPlusFrequente;
    private Integer heurePointeMoyenne;
}

