package com.supermarket.manager.model.dto;

import com.supermarket.manager.model.commande.StatutCommande;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeDTO {

    private Long id;
    private String numeroCommande;
    private Long fournisseurId;
    private String nomFournisseur;
    private StatutCommande statut;
    private LocalDate dateCommande;
    private LocalDate dateLivraisonPrevue;
    private LocalDate dateLivraisonEffective;
    private BigDecimal montantTotal;
    private String notes;
    private Boolean commandeAutomatique;

    @Builder.Default
    private List<LigneCommandeDTO> lignes = new ArrayList<>();
}

