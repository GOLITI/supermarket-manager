package com.supermarket.manager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RapportVentesDTO {
    private LocalDate date;
    private Integer nombreTransactions;
    private BigDecimal montantTotalVentes;
    private BigDecimal montantTotalRemises;
    private BigDecimal montantNetVentes;
    private BigDecimal montantMoyen;
    private Integer nombreArticlesVendus;
    private List<ProduitVenduDTO> produitsLesPlusVendus = new ArrayList<>();
    private Integer nombreCartesUtilisees;
    private BigDecimal tauxRemise;
}

