package com.supermarket.manager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {
    private PeriodeDTO periode;
    private VentesGlobalesDTO ventesGlobales;
    private List<VenteProduitDTO> topProduits;
    private List<VenteProduitDTO> produitsEnBaisse;
    private List<StockAlertDTO> alertesStock;
    private List<HeurePointeDTO> heuresPointe;
    private List<MargeBeneficiaireDTO> margesParCategorie;
    private FrequentationDTO frequentation;
}
