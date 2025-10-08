package comcom.supermarket.manager.model.caisse;

import comcom.supermarket.manager.model.produit.Produit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypePromotion type;

    @Column(nullable = false)
    private BigDecimal valeur;

    private BigDecimal montantMinimum;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @Column(nullable = false)
    private Boolean active = true;

    private Integer nombreUtilisations = 0;

    private Integer utilisationsMax;

    private Integer utilisationsActuelles = 0;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }

    public BigDecimal calculerRemise(BigDecimal montant) {
        if (!isValide() || (montantMinimum != null && montant.compareTo(montantMinimum) < 0)) {
            return BigDecimal.ZERO;
        }

        return switch (type) {
            case POURCENTAGE -> montant.multiply(valeur).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            case MONTANT_FIXE -> valeur;
            case PRIX_FIXE -> montant.subtract(valeur).max(BigDecimal.ZERO);
        };
    }

    public boolean isValide() {
        LocalDate now = LocalDate.now();
        return active && !now.isBefore(dateDebut) && !now.isAfter(dateFin);
    }

    public void incrementerUtilisations() {
        this.nombreUtilisations++;
        this.utilisationsActuelles++;
    }
}
