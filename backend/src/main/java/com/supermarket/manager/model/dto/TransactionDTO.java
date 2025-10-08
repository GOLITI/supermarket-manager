package com.supermarket.manager.model.dto;

import com.supermarket.manager.model.caisse.MethodePaiement;
import com.supermarket.manager.model.caisse.StatutTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private String numeroTransaction;
    private LocalDateTime dateHeure;
    private List<LigneTransactionDTO> lignes = new ArrayList<>();
    private Long carteFideliteId;
    private String nomClient;
    private BigDecimal montantBrut;
    private BigDecimal montantRemises;
    private BigDecimal montantNet;
    private BigDecimal montantPaye;
    private BigDecimal montantRendu;
    private MethodePaiement methodePaiement;
    private StatutTransaction statut;
    private List<Long> promotionIds = new ArrayList<>();
    private BigDecimal pointsGagnes;
    private BigDecimal pointsUtilises;
    private String caissierId;
    private String remarques;
}

