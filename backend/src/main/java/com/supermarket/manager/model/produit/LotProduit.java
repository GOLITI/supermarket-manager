package com.supermarket.manager.model.produit;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lots_produits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LotProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String numeroLot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrepot_id")
    private Entrepot entrepot;

    @Column(nullable = false)
    private Integer quantiteInitiale;

    @Column(nullable = false)
    private Integer quantiteActuelle;

    @Column(name = "date_production")
    private LocalDate dateProduction;

    @Column(name = "date_peremption")
    private LocalDate datePeremption;

    @Column(name = "date_reception", nullable = false)
    private LocalDate dateReception;

    @Column(name = "numero_commande", length = 50)
    private String numeroCommande;

    @Column(name = "fournisseur_lot", length = 200)
    private String fournisseurLot;

    @Column(name = "statut", length = 50)
    @Enumerated(EnumType.STRING)
    private StatutLot statut;

    @Column(name = "emplacement_stockage", length = 100)
    private String emplacementStockage;

    @Column(name = "temperature_stockage")
    private Double temperatureStockage;

    @Column(name = "observations", length = 1000)
    private String observations;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
        if (statut == null) {
            statut = StatutLot.ACTIF;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }

    public boolean isPerime() {
        return datePeremption != null && datePeremption.isBefore(LocalDate.now());
    }

    public boolean isProcheDeLaPeremption(int joursAvant) {
        return datePeremption != null &&
               datePeremption.isBefore(LocalDate.now().plusDays(joursAvant));
    }

    public void retirerQuantite(int quantite) {
        if (quantite > quantiteActuelle) {
            throw new IllegalArgumentException(
                "Quantité insuffisante dans le lot " + numeroLot +
                ". Disponible: " + quantiteActuelle + ", Demandé: " + quantite);
        }
        this.quantiteActuelle -= quantite;
        if (this.quantiteActuelle == 0) {
            this.statut = StatutLot.EPUISE;
        }
    }

    public void ajouterQuantite(int quantite) {
        this.quantiteActuelle += quantite;
        if (this.statut == StatutLot.EPUISE) {
            this.statut = StatutLot.ACTIF;
        }
    }
}

