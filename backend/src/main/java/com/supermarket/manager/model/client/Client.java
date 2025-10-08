package com.supermarket.manager.model.client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String numeroClient;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String prenom;
    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String telephone;
    private String adresse;
    private String ville;
    private String codePostal;
    @Column(nullable = false)
    private LocalDate dateNaissance;
    @Column(nullable = false)
    private LocalDateTime dateInscription;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutClient statut = StatutClient.ACTIF;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SegmentClient segment = SegmentClient.OCCASIONNEL;
    @Column(unique = true)
    private String numeroCarteFidelite;
    @Column(nullable = false)
    private Integer pointsFidelite = 0;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NiveauFidelite niveauFidelite = NiveauFidelite.BRONZE;
    @Column(nullable = false)
    private BigDecimal totalAchats = BigDecimal.ZERO;
    @Column(nullable = false)
    private Integer nombreAchats = 0;
    @Column(nullable = false)
    private BigDecimal panierMoyen = BigDecimal.ZERO;
    private LocalDate dernierAchat;
    @Column(nullable = false)
    private Boolean accepteEmail = true;
    @Column(nullable = false)
    private Boolean accepteSMS = false;
    @Column(nullable = false)
    private Boolean accepteNotifications = true;
    @ElementCollection
    @CollectionTable(name = "client_categories_preferees", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "categorie")
    private List<String> categoriesPreferees = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<HistoriqueAchat> historiqueAchats = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<MouvementPoints> mouvementsPoints = new ArrayList<>();
    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();
    private LocalDateTime dateModification;
    @PreUpdate
    public void preUpdate() {
        this.dateModification = LocalDateTime.now();
        calculerSegment();
        calculerPanierMoyen();
    }
    @PrePersist
    public void prePersist() {
        if (this.dateInscription == null) {
            this.dateInscription = LocalDateTime.now();
        }
        if (this.numeroCarteFidelite == null) {
            this.numeroCarteFidelite = "CF" + System.currentTimeMillis();
        }
    }
    public void calculerSegment() {
        if (totalAchats.compareTo(BigDecimal.valueOf(500000)) >= 0) {
            this.segment = SegmentClient.VIP;
        } else if (totalAchats.compareTo(BigDecimal.valueOf(200000)) >= 0) {
            this.segment = SegmentClient.PREMIUM;
        } else if (nombreAchats >= 5) {
            this.segment = SegmentClient.REGULIER;
        } else {
            this.segment = SegmentClient.OCCASIONNEL;
        }
    }
    public void calculerPanierMoyen() {
        if (nombreAchats > 0) {
            this.panierMoyen = totalAchats.divide(BigDecimal.valueOf(nombreAchats), 2, BigDecimal.ROUND_HALF_UP);
        }
    }
    public void ajouterPoints(int points) {
        this.pointsFidelite += points;
        mettreAJourNiveauFidelite();
    }
    public void utiliserPoints(int points) {
        if (this.pointsFidelite >= points) {
            this.pointsFidelite -= points;
            mettreAJourNiveauFidelite();
        } else {
            throw new IllegalArgumentException("Points insuffisants");
        }
    }
    private void mettreAJourNiveauFidelite() {
        if (pointsFidelite >= 5000) {
            this.niveauFidelite = NiveauFidelite.DIAMANT;
        } else if (pointsFidelite >= 2000) {
            this.niveauFidelite = NiveauFidelite.OR;
        } else if (pointsFidelite >= 500) {
            this.niveauFidelite = NiveauFidelite.ARGENT;
        } else {
            this.niveauFidelite = NiveauFidelite.BRONZE;
        }
    }
}
