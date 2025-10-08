package comcom.supermarket.manager.model.caisse;

import comcom.supermarket.manager.model.produit.Produit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "lignes_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @Column(nullable = false)
    private Integer quantite;

    @Column(nullable = false)
    private BigDecimal prixUnitaire;

    private BigDecimal remiseLigne = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotionAppliquee;

    public BigDecimal getMontantTotal() {
        BigDecimal montantBrut = prixUnitaire.multiply(new BigDecimal(quantite));
        return montantBrut.subtract(remiseLigne);
    }

    public void appliquerPromotion(Promotion promotion) {
        if (promotion.getProduit() != null && !promotion.getProduit().equals(produit)) {
            return;
        }
        this.promotionAppliquee = promotion;
        BigDecimal montantBrut = prixUnitaire.multiply(new BigDecimal(quantite));
        this.remiseLigne = promotion.calculerRemise(montantBrut);
    }
}
