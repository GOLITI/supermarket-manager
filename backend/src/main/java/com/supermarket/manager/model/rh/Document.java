package com.supermarket.manager.model.rh;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe_id", nullable = false)
    private Employe employe;
    
    @NotBlank(message = "Le titre du document est obligatoire")
    @Column(nullable = false)
    private String titre;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeDocument type;
    
    @NotBlank(message = "Le chemin du fichier est obligatoire")
    @Column(nullable = false)
    private String cheminFichier;
    
    private String nomFichier;
    
    private Long tailleFichier;
    
    private String typeMime;
    
    @Column(length = 1000)
    private String description;
    
    @NotNull
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime dateAjout = LocalDateTime.now();
    
    @Builder.Default
    private Boolean confidentiel = false;
    
    private LocalDateTime dateExpiration;
    
    // Méthode utilitaire
    public boolean estExpire() {
        return dateExpiration != null && LocalDateTime.now().isAfter(dateExpiration);
    }
}

