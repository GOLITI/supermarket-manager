package com.supermarket.manager.model.rh;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "absences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Absence {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe_id", nullable = false)
    private Employe employe;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeAbsence type;
    
    @NotNull(message = "La date de début est obligatoire")
    @Column(nullable = false)
    private LocalDate dateDebut;
    
    @NotNull(message = "La date de fin est obligatoire")
    @Column(nullable = false)
    private LocalDate dateFin;
    
    @Column(length = 1000)
    private String motif;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutDemande statut = StatutDemande.EN_ATTENTE;
    
    private LocalDate dateValidation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validateur_id")
    private Employe validateur;
    
    @Column(length = 500)
    private String commentaireValidation;
    
    private String documentJustificatif;
    
    // Méthodes utilitaires
    public long getNombreJours() {
        return java.time.temporal.ChronoUnit.DAYS.between(dateDebut, dateFin) + 1;
    }
    
    public boolean estEnCours() {
        LocalDate now = LocalDate.now();
        return !now.isBefore(dateDebut) && !now.isAfter(dateFin);
    }
}

