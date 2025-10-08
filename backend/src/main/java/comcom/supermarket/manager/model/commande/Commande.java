package comcom.supermarket.manager.model.commande;

import comcom.supermarket.manager.model.fournisseur.Fournisseur;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commandes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String numeroCommande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private Fournisseur fournisseur;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCommande statut = StatutCommande.BROUILLON;

    @Column(name = "date_commande", nullable = false)
    private LocalDate dateCommande;

    @Column(name = "date_livraison_prevue")
    private LocalDate dateLivraisonPrevue;

    @Column(name = "date_livraison_effective")
    private LocalDate dateLivraisonEffective;

    @Column(name = "montant_total", precision = 12, scale = 2)
    private BigDecimal montantTotal = BigDecimal.ZERO;

    @Column(length = 1000)
    private String notes;

    @Column(name = "commande_automatique")
    private Boolean commandeAutomatique = false;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LigneCommande> lignes = new ArrayList<>();

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
        if (dateCommande == null) {
            dateCommande = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }

    public void calculerMontantTotal() {
        this.montantTotal = lignes.stream()
            .map(LigneCommande::getMontantLigne)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void ajouterLigne(LigneCommande ligne) {
        lignes.add(ligne);
        ligne.setCommande(this);
        calculerMontantTotal();
    }

    public void retirerLigne(LigneCommande ligne) {
        lignes.remove(ligne);
        ligne.setCommande(null);
        calculerMontantTotal();
    }
}
