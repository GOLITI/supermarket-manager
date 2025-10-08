package com.supermarket.manager.model.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduitDTO {

    private Long id;
    private String code;
    private String nom;
    private String description;
    private Long categorieId;
    private String nomCategorie;
    private Long fournisseurId;
    private String nomFournisseur;
    private BigDecimal prixAchat;
    private BigDecimal prixVente;
    private String unite;
    private String codeBarres;
    private Boolean actif;
    private Boolean datePeremptionRequis;
    private String imageUrl;
    private Integer stockTotal;
}

