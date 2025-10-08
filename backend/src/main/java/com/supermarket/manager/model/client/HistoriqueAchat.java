package com.supermarket.manager.model.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "historique_achats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriqueAchat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false, unique = true)
    private String numeroTicket;

    @Column(nullable = false)
    private LocalDateTime dateAchat;

    @Column(nullable = false)
    private BigDecimal montantTotal;

    @Column(nullable = false)
    private BigDecimal montantRemise = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal montantPaye;

    @Column(nullable = false)
    private Integer pointsGagnes = 0;

    @Column(nullable = false)
    private Integer pointsUtilises = 0;

    @ElementCollection
    @CollectionTable(name = "achat_produits", 
                     joinColumns = @JoinColumn(name = "historique_achat_id"))
    private List<ProduitAchete> produitsAchetes = new ArrayList<>();

    @Column(name = "caisse_numero")
    private String numeroCaisse;

    @Column(name = "caissier_nom")
    private String nomCaissier;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProduitAchete {
        private Long produitId;
        private String nomProduit;
        private String categorie;
        private Integer quantite;
        private BigDecimal prixUnitaire;
        private BigDecimal montantLigne;
    }
}

