package com.supermarket.manager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitVenduDTO {
    private Long produitId;
    private String nomProduit;
    private String codeBarre;
    private Integer quantiteVendue;
    private BigDecimal montantTotal;
    private Integer nombreTransactions;
}
