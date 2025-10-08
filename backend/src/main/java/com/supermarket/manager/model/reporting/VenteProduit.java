package com.supermarket.manager.model.reporting;

import com.supermarket.manager.model.produit.Produit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventes_produit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenteProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @Column(name = "quantite_vendue", nullable = false)
    private Integer quantiteVendue;

    @Column(name = "montant_vente", nullable = false, precision = 12, scale = 2)
    private BigDecimal montantVente;

    @Column(name = "cout_achat", precision = 12, scale = 2)
    private BigDecimal coutAchat;

    @Column(name = "marge_beneficiaire", precision = 12, scale = 2)
    private BigDecimal margeBeneficiaire;

    @Column(name = "date_vente", nullable = false)
    private LocalDateTime dateVente;

    @Column(name = "heure_vente")
    private Integer heureVente; // 0-23

    @Column(name = "jour_semaine")
    private String jourSemaine; // Lundi, Mardi, etc.

    @PrePersist
    public void prePersist() {
        if (dateVente != null) {
            this.heureVente = dateVente.getHour();
            this.jourSemaine = dateVente.getDayOfWeek().toString();
        }

        if (montantVente != null && coutAchat != null) {
            this.margeBeneficiaire = montantVente.subtract(coutAchat);
        }
    }
}

