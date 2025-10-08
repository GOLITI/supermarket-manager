package com.supermarket.manager.model.reporting;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "statistiques_frequentation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatistiqueFrequentation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "date_stat", nullable = false)
    private LocalDate dateStat;
    
    @Column(name = "heure_debut", nullable = false)
    private LocalTime heureDebut;
    
    @Column(name = "heure_fin", nullable = false)
    private LocalTime heureFin;
    
    @Column(name = "nombre_clients", nullable = false)
    private Integer nombreClients;
    
    @Column(name = "nombre_transactions", nullable = false)
    private Integer nombreTransactions;
    
    @Column(name = "jour_semaine")
    private String jourSemaine;
    
    @Column(name = "est_heure_pointe")
    private Boolean estHeurePointe = false;
    
    @PrePersist
    public void prePersist() {
        if (dateStat != null) {
            this.jourSemaine = dateStat.getDayOfWeek().toString();
        }
    }
}

