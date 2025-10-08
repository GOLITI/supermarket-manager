package com.supermarket.manager.model.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneCommandeDTO {

    private Long id;
    private Long produitId;
    private String nomProduit;
    private Integer quantite;
    private Integer quantiteRecue;
    private BigDecimal prixUnitaire;
    private BigDecimal montantLigne;
    private String notes;
}

