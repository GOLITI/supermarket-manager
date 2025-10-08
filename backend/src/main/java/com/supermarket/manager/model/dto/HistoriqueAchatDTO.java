package com.supermarket.manager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriqueAchatDTO {
    private Long id;
    private String numeroTicket;
    private LocalDateTime dateAchat;
    private BigDecimal montantTotal;
    private BigDecimal montantRemise;
    private BigDecimal montantPaye;
    private Integer pointsGagnes;
    private Integer pointsUtilises;
    private List<ProduitAcheteDTO> produitsAchetes;
    private String numeroCaisse;
    private String nomCaissier;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProduitAcheteDTO {
        private Long produitId;
        private String nomProduit;
        private String categorie;
        private Integer quantite;
        private BigDecimal prixUnitaire;
        private BigDecimal montantLigne;
    }
}

