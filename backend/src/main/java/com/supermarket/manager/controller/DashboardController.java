package com.supermarket.manager.controller;

import com.supermarket.manager.model.dto.*;
import com.supermarket.manager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class DashboardController {
    
    private final DashboardService dashboardService;
    
    /**
     * GET /api/dashboard - Récupère le tableau de bord complet
     */
    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        
        log.info("Récupération du tableau de bord pour la période {} - {}", debut, fin);
        
        // Par défaut, période du mois en cours
        if (debut == null) {
            debut = LocalDate.now().withDayOfMonth(1);
        }
        if (fin == null) {
            fin = LocalDate.now();
        }
        
        DashboardDTO dashboard = dashboardService.getDashboard(debut, fin);
        return ResponseEntity.ok(dashboard);
    }
    
    /**
     * GET /api/dashboard/ventes-produits - Récupère les ventes par produit
     */
    @GetMapping("/ventes-produits")
    public ResponseEntity<List<VenteProduitDTO>> getVentesProduits(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        
        log.info("Récupération des ventes produits du {} au {}", debut, fin);
        List<VenteProduitDTO> ventes = dashboardService.getVentesProduits(debut, fin);
        return ResponseEntity.ok(ventes);
    }
    
    /**
     * GET /api/dashboard/produits-en-baisse - Récupère les produits en baisse
     */
    @GetMapping("/produits-en-baisse")
    public ResponseEntity<List<VenteProduitDTO>> getProduitsEnBaisse(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @RequestParam(defaultValue = "10.0") Double seuil) {
        
        log.info("Recherche des produits en baisse de plus de {}%", seuil);
        List<VenteProduitDTO> produits = dashboardService.getProduitsEnBaisse(debut, fin, seuil);
        return ResponseEntity.ok(produits);
    }
    
    /**
     * GET /api/dashboard/heures-pointe - Récupère les heures de pointe
     */
    @GetMapping("/heures-pointe")
    public ResponseEntity<List<HeurePointeDTO>> getHeuresPointe(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        
        log.info("Récupération des heures de pointe du {} au {}", debut, fin);
        List<HeurePointeDTO> heuresPointe = dashboardService.getHeuresPointe(debut, fin);
        return ResponseEntity.ok(heuresPointe);
    }
    
    /**
     * GET /api/dashboard/marges-categorie - Récupère les marges par catégorie
     */
    @GetMapping("/marges-categorie")
    public ResponseEntity<List<MargeBeneficiaireDTO>> getMargesParCategorie(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        
        log.info("Récupération des marges par catégorie du {} au {}", debut, fin);
        List<MargeBeneficiaireDTO> marges = dashboardService.getMargesParCategorie(debut, fin);
        return ResponseEntity.ok(marges);
    }
    
    /**
     * GET /api/dashboard/evolution-produit/{id} - Récupère l'évolution d'un produit
     */
    @GetMapping("/evolution-produit/{id}")
    public ResponseEntity<List<VenteProduitDTO>> getEvolutionProduit(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        
        log.info("Récupération de l'évolution du produit {} du {} au {}", id, debut, fin);
        List<VenteProduitDTO> evolution = dashboardService.getEvolutionVentesProduit(id, debut, fin);
        return ResponseEntity.ok(evolution);
    }
    
    /**
     * GET /api/dashboard/frequentation - Récupère les statistiques de fréquentation
     */
    @GetMapping("/frequentation")
    public ResponseEntity<FrequentationDTO> getFrequentation(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        
        log.info("Récupération des statistiques de fréquentation du {} au {}", debut, fin);
        FrequentationDTO frequentation = dashboardService.getStatistiquesFrequentation(debut, fin);
        return ResponseEntity.ok(frequentation);
    }
}

