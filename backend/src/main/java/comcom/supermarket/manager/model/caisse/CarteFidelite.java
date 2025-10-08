package comcom.supermarket.manager.model.caisse;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cartes_fidelite")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarteFidelite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroCarte;

    @Column(nullable = false)
    private String nomClient;

    @Column(nullable = false)
    private String prenomClient;

    @Column(unique = true)
    private String email;

    private String telephone;

    @Column(nullable = false)
    private LocalDate dateInscription;

    @Column(nullable = false)
    private BigDecimal pointsFidelite = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal totalAchats = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NiveauFidelite niveau = NiveauFidelite.BRONZE;

    @Column(nullable = false)
    private Boolean active = true;

    private LocalDate dateExpiration;

    public void ajouterPoints(BigDecimal points) {
        this.pointsFidelite = this.pointsFidelite.add(points);
        mettreAJourNiveau();
    }

    public void utiliserPoints(BigDecimal points) {
        if (this.pointsFidelite.compareTo(points) >= 0) {
            this.pointsFidelite = this.pointsFidelite.subtract(points);
        } else {
            throw new IllegalArgumentException("Points insuffisants");
        }
    }

    public void ajouterAchat(BigDecimal montant) {
        this.totalAchats = this.totalAchats.add(montant);
        mettreAJourNiveau();
    }

    private void mettreAJourNiveau() {
        if (this.totalAchats.compareTo(new BigDecimal("5000")) >= 0) {
            this.niveau = NiveauFidelite.PLATINE;
        } else if (this.totalAchats.compareTo(new BigDecimal("2000")) >= 0) {
            this.niveau = NiveauFidelite.OR;
        } else if (this.totalAchats.compareTo(new BigDecimal("500")) >= 0) {
            this.niveau = NiveauFidelite.ARGENT;
        } else {
            this.niveau = NiveauFidelite.BRONZE;
        }
    }

    public boolean isValide() {
        return active && (dateExpiration == null || dateExpiration.isAfter(LocalDate.now()));
    }
}

