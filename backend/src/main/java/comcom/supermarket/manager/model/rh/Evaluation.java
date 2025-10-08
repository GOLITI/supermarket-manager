package comcom.supermarket.manager.model.rh;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "evaluations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe_id", nullable = false)
    private Employe employe;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluateur_id", nullable = false)
    private Employe evaluateur;
    
    @NotNull(message = "La date d'évaluation est obligatoire")
    @Column(nullable = false)
    private LocalDate dateEvaluation;
    
    @NotNull(message = "La période évaluée est obligatoire")
    private String periodeEvaluee; // Ex: "Q1 2025"
    
    @Min(value = 1, message = "La note doit être entre 1 et 5")
    @Max(value = 5, message = "La note doit être entre 1 et 5")
    @Column(nullable = false)
    private Integer noteGlobale;
    
    @Min(value = 1)
    @Max(value = 5)
    private Integer notePonctualite;
    
    @Min(value = 1)
    @Max(value = 5)
    private Integer noteQualiteTravail;
    
    @Min(value = 1)
    @Max(value = 5)
    private Integer noteProductivite;
    
    @Min(value = 1)
    @Max(value = 5)
    private Integer noteRelationClient;
    
    @Min(value = 1)
    @Max(value = 5)
    private Integer noteEspritEquipe;
    
    @Column(length = 2000)
    private String pointsForts;
    
    @Column(length = 2000)
    private String pointsAmeliorer;
    
    @Column(length = 2000)
    private String objectifs;
    
    @Column(length = 1000)
    private String commentaires;
    
    @Builder.Default
    private Boolean valide = false;
    
    // Méthode utilitaire
    public double getNoteMoyenne() {
        int somme = 0;
        int count = 0;
        
        if (notePonctualite != null) { somme += notePonctualite; count++; }
        if (noteQualiteTravail != null) { somme += noteQualiteTravail; count++; }
        if (noteProductivite != null) { somme += noteProductivite; count++; }
        if (noteRelationClient != null) { somme += noteRelationClient; count++; }
        if (noteEspritEquipe != null) { somme += noteEspritEquipe; count++; }
        
        return count > 0 ? (double) somme / count : 0.0;
    }
}

