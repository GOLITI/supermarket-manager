package com.supermarket.manager.model.rh;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Table(name = "pointages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pointage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe_id", nullable = false)
    private Employe employe;
    
    @NotNull(message = "L'heure d'entrée est obligatoire")
    @Column(nullable = false)
    private LocalDateTime heureEntree;
    
    private LocalDateTime heureSortie;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypePointage type;
    
    @Column(length = 500)
    private String notes;
    
    private Double heuresTravaillees;
    
    private Double heuresSupplementaires;
    
    @Builder.Default
    private Boolean valide = false;
    
    // Méthodes utilitaires
    public void calculerHeuresTravaillees() {
        if (heureSortie != null) {
            Duration duree = Duration.between(heureEntree, heureSortie);
            this.heuresTravaillees = duree.toMinutes() / 60.0;
            
            // Calculer les heures sup (au-delà de 8h)
            if (this.heuresTravaillees > 8.0) {
                this.heuresSupplementaires = this.heuresTravaillees - 8.0;
            } else {
                this.heuresSupplementaires = 0.0;
            }
        }
    }
    
    public boolean estEnCours() {
        return heureSortie == null;
    }
}

