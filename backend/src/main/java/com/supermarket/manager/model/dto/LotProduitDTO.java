package com.supermarket.manager.model.dto;

import com.supermarket.manager.model.produit.StatutLot;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LotProduitDTO {
    
    private Long id;
    private String numeroLot;
    private Long produitId;
    private String nomProduit;
    private String codeProduit;
    private Long entrepotId;
    private String nomEntrepot;
    private Integer quantiteInitiale;
    private Integer quantiteActuelle;
    private LocalDate dateProduction;
    private LocalDate datePeremption;
    private LocalDate dateReception;
    private String numeroCommande;
    private String fournisseurLot;
    private StatutLot statut;
    private String emplacementStockage;
    private Double temperatureStockage;
    private String observations;
    private Boolean perime;
    private Boolean prochePeremption;
    private Integer joursAvantPeremption;
}
