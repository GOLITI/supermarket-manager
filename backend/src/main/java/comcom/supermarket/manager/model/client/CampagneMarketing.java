package comcom.supermarket.manager.model.client;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campagnes_marketing")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampagneMarketing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String nom;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCampagne type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCampagne statut = StatutCampagne.BROUILLON;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    // Ciblage
    @ElementCollection
    @CollectionTable(name = "campagne_segments_cibles", 
                     joinColumns = @JoinColumn(name = "campagne_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "segment")
    private List<SegmentClient> segmentsCibles = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "campagne_niveaux_cibles", 
                     joinColumns = @JoinColumn(name = "campagne_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "niveau")
    private List<NiveauFidelite> niveauxCibles = new ArrayList<>();

    // Contenu
    @Column(length = 500)
    private String sujet;

    @Column(length = 2000)
    private String message;

    private String urlImage;

    // Offre promotionnelle
    @Enumerated(EnumType.STRING)
    private TypeOffre typeOffre;

    private BigDecimal valeurOffre;

    private String codePromo;

    // Statistiques
    @Column(nullable = false)
    private Integer nombreCibles = 0;

    @Column(nullable = false)
    private Integer nombreEnvoyes = 0;

    @Column(nullable = false)
    private Integer nombreOuvertures = 0;

    @Column(nullable = false)
    private Integer nombreClics = 0;

    @Column(nullable = false)
    private Integer nombreConversions = 0;

    private BigDecimal chiffreAffaireGenere = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    private LocalDateTime dateEnvoi;

    private LocalDateTime dateModification;

    @PreUpdate
    public void preUpdate() {
        this.dateModification = LocalDateTime.now();
    }

    public double getTauxOuverture() {
        return nombreEnvoyes > 0 ? (nombreOuvertures * 100.0 / nombreEnvoyes) : 0.0;
    }

    public double getTauxClics() {
        return nombreEnvoyes > 0 ? (nombreClics * 100.0 / nombreEnvoyes) : 0.0;
    }

    public double getTauxConversion() {
        return nombreEnvoyes > 0 ? (nombreConversions * 100.0 / nombreEnvoyes) : 0.0;
    }
}

