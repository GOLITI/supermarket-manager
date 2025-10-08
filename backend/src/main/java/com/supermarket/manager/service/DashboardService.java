package com.supermarket.manager.service;

import com.supermarket.manager.model.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface DashboardService {

    /**
     * Récupère le tableau de bord complet pour une période donnée
     */
    DashboardDTO getDashboard(LocalDate debut, LocalDate fin);

    /**
     * Récupère les ventes par produit pour une période
     */
    List<VenteProduitDTO> getVentesProduits(LocalDate debut, LocalDate fin);

    /**
     * Récupère les produits en baisse de ventes
     */
    List<VenteProduitDTO> getProduitsEnBaisse(LocalDate debut, LocalDate fin, Double seuilPourcentage);

    /**
     * Récupère les heures de pointe
     */
    List<HeurePointeDTO> getHeuresPointe(LocalDate debut, LocalDate fin);

    /**
     * Récupère les marges bénéficiaires par catégorie
     */
    List<MargeBeneficiaireDTO> getMargesParCategorie(LocalDate debut, LocalDate fin);

    /**
     * Récupère l'évolution des ventes d'un produit
     */
    List<VenteProduitDTO> getEvolutionVentesProduit(Long produitId, LocalDate debut, LocalDate fin);

    /**
     * Récupère les statistiques de fréquentation
     */
    FrequentationDTO getStatistiquesFrequentation(LocalDate debut, LocalDate fin);
}

