package comcom.supermarket.manager.model.caisse;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroTransaction;

    @Column(nullable = false)
    private LocalDateTime dateHeure = LocalDateTime.now();

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneTransaction> lignes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "carte_fidelite_id")
    private CarteFidelite carteFidelite;

    @Column(nullable = false)
    private BigDecimal montantBrut = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal montantRemises = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal montantNet = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal montantPaye = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal montantRendu = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MethodePaiement methodePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutTransaction statut = StatutTransaction.EN_COURS;

    @ManyToMany
    @JoinTable(
        name = "transaction_promotions",
        joinColumns = @JoinColumn(name = "transaction_id"),
        inverseJoinColumns = @JoinColumn(name = "promotion_id")
    )
    private List<Promotion> promotionsAppliquees = new ArrayList<>();

    private BigDecimal pointsGagnes = BigDecimal.ZERO;
    private BigDecimal pointsUtilises = BigDecimal.ZERO;
    private String caissierId;
    private String remarques;

    public void ajouterLigne(LigneTransaction ligne) {
        lignes.add(ligne);
        ligne.setTransaction(this);
        recalculerMontants();
    }

    public void supprimerLigne(LigneTransaction ligne) {
        lignes.remove(ligne);
        ligne.setTransaction(null);
        recalculerMontants();
    }

    public void recalculerMontants() {
        this.montantBrut = lignes.stream()
            .map(LigneTransaction::getMontantTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.montantNet = montantBrut.subtract(montantRemises);
    }

    public void appliquerPromotion(Promotion promotion) {
        if (!promotionsAppliquees.contains(promotion)) {
            promotionsAppliquees.add(promotion);
            BigDecimal remise = promotion.calculerRemise(montantBrut);
            this.montantRemises = this.montantRemises.add(remise);
            recalculerMontants();
        }
    }

    public void finaliser() {
        if (montantPaye.compareTo(montantNet) < 0) {
            throw new IllegalStateException("Montant payé insuffisant");
        }
        this.montantRendu = montantPaye.subtract(montantNet);
        this.statut = StatutTransaction.COMPLETEE;
        if (carteFidelite != null && carteFidelite.isValide()) {
            this.pointsGagnes = carteFidelite.getNiveau().calculerPoints(montantNet);
            carteFidelite.ajouterPoints(pointsGagnes);
            carteFidelite.ajouterAchat(montantNet);
        }
        promotionsAppliquees.forEach(Promotion::incrementerUtilisations);
    }

    public void annuler() {
        this.statut = StatutTransaction.ANNULEE;
    }
}

