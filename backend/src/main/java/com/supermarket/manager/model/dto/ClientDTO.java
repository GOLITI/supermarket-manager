package com.supermarket.manager.model.dto;
import com.supermarket.manager.model.client.NiveauFidelite;
import com.supermarket.manager.model.client.SegmentClient;
import com.supermarket.manager.model.client.StatutClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDTO {
    private Long id;
    private String numeroClient;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String ville;
    private String codePostal;
    private LocalDate dateNaissance;
    private StatutClient statut;
    private SegmentClient segment;
    private String numeroCarteFidelite;
    private Integer pointsFidelite;
    private NiveauFidelite niveauFidelite;
    private BigDecimal totalAchats;
    private Integer nombreAchats;
    private BigDecimal panierMoyen;
    private LocalDate dernierAchat;
    private Boolean accepteEmail;
    private Boolean accepteSMS;
    private Boolean accepteNotifications;
    private List<String> categoriesPreferees;
}
