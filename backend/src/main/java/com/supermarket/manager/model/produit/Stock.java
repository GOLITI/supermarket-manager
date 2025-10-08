package com.supermarket.manager.model.produit;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrepot_id", nullable = false)
    private Entrepot entrepot;

    @Column(nullable = false)
    private Integer quantite = 0;

    @Column(name = "seuil_reapprovisionnement", nullable = false)
    private Integer seuilReapprovisionnement;

    @Column(name = "quantite_maximale")
    private Integer quantiteMaximale;

    @Column(name = "quantite_recommandee_commande")
    private Integer quantiteRecommandeeCommande;

    @Column(name = "date_derniere_entree")
    private LocalDateTime dateDerniereEntree;

    @Column(name = "date_derniere_sortie")
    private LocalDateTime dateDerniereSortie;

    @Column(name = "date_peremption")
    private LocalDate datePeremption;

    @Column(name = "alerte_active")
    private Boolean alerteActive = false;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
        verifierAlerte();
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
        verifierAlerte();
    }

    private void verifierAlerte() {
        this.alerteActive = this.quantite <= this.seuilReapprovisionnement;
    }

    public void ajouterQuantite(Integer quantiteAjoutee) {
        this.quantite += quantiteAjoutee;
        this.dateDerniereEntree = LocalDateTime.now();
        verifierAlerte();
    }

    public void augmenterQuantite(Integer quantiteAjoutee) {
        ajouterQuantite(quantiteAjoutee);
    }

    public void retirerQuantite(Integer quantiteRetiree) {
        if (this.quantite >= quantiteRetiree) {
            this.quantite -= quantiteRetiree;
            this.dateDerniereSortie = LocalDateTime.now();
            verifierAlerte();
        } else {
            throw new IllegalArgumentException("Stock insuffisant");
        }
    }

    public void diminuerQuantite(Integer quantiteRetiree) {
        retirerQuantite(quantiteRetiree);
    }
}
