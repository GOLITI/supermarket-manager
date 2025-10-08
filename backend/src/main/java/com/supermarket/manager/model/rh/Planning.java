package com.supermarket.manager.model.rh;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "plannings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Planning {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe_id", nullable = false)
    private Employe employe;
    
    @NotNull(message = "La date est obligatoire")
    @Column(nullable = false)
    private LocalDate date;
    
    @NotNull(message = "L'heure de début est obligatoire")
    @Column(nullable = false)
    private LocalTime heureDebut;
    
    @NotNull(message = "L'heure de fin est obligatoire")
    @Column(nullable = false)
    private LocalTime heureFin;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeShift typeShift;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PosteEmploye posteAssigne;
    
    @Column(length = 500)
    private String notes;
    
    @Builder.Default
    private Boolean valide = false;
    
    // Méthodes utilitaires
    public double getDureeHeures() {
        return java.time.Duration.between(heureDebut, heureFin).toMinutes() / 60.0;
    }
    
    public boolean estAujourdhui() {
        return date.equals(LocalDate.now());
    }
}

