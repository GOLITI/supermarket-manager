package com.supermarket.manager.controller;

import com.supermarket.manager.model.dto.LotProduitDTO;
import com.supermarket.manager.model.produit.LotProduit;
import com.supermarket.manager.model.produit.StatutLot;
import com.supermarket.manager.service.LotProduitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lots")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Gestion des Lots", description = "APIs pour gérer les lots de produits et leur traçabilité")
public class LotProduitController {

    private final LotProduitService lotProduitService;

    @PostMapping
    @Operation(summary = "Créer un nouveau lot", description = "Crée un nouveau lot de produit avec traçabilité complète")
    public ResponseEntity<LotProduitDTO> creerLot(@RequestBody LotProduit lot) {
        LotProduit nouveauLot = lotProduitService.creerLot(lot);
        return ResponseEntity.status(HttpStatus.CREATED).body(lotProduitService.toDTO(nouveauLot));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un lot par ID", description = "Récupère les détails d'un lot par son identifiant")
    public ResponseEntity<LotProduitDTO> getLotById(@PathVariable Long id) {
        LotProduit lot = lotProduitService.getLotById(id);
        return ResponseEntity.ok(lotProduitService.toDTO(lot));
    }

    @GetMapping("/numero/{numeroLot}")
    @Operation(summary = "Obtenir un lot par numéro", description = "Récupère un lot par son numéro unique")
    public ResponseEntity<LotProduitDTO> getLotByNumero(@PathVariable String numeroLot) {
        LotProduit lot = lotProduitService.getLotByNumero(numeroLot);
        return ResponseEntity.ok(lotProduitService.toDTO(lot));
    }

    @GetMapping("/produit/{produitId}")
    @Operation(summary = "Lots d'un produit", description = "Récupère tous les lots d'un produit spécifique")
    public ResponseEntity<List<LotProduitDTO>> getLotsByProduit(@PathVariable Long produitId) {
        List<LotProduit> lots = lotProduitService.getLotsByProduit(produitId);
        return ResponseEntity.ok(lotProduitService.toDTOList(lots));
    }

    @GetMapping("/entrepot/{entrepotId}")
    @Operation(summary = "Lots d'un entrepôt", description = "Récupère tous les lots d'un entrepôt")
    public ResponseEntity<List<LotProduitDTO>> getLotsByEntrepot(@PathVariable Long entrepotId) {
        List<LotProduit> lots = lotProduitService.getLotsByEntrepot(entrepotId);
        return ResponseEntity.ok(lotProduitService.toDTOList(lots));
    }

    @GetMapping("/actifs")
    @Operation(summary = "Lots actifs avec stock", description = "Récupère tous les lots actifs ayant du stock disponible")
    public ResponseEntity<List<LotProduitDTO>> getLotsActifsAvecStock() {
        List<LotProduit> lots = lotProduitService.getLotsActifsAvecStock();
        return ResponseEntity.ok(lotProduitService.toDTOList(lots));
    }

    @GetMapping("/peremption-proche")
    @Operation(summary = "Lots proches de la péremption", description = "Récupère les lots qui arrivent bientôt à péremption")
    public ResponseEntity<List<LotProduitDTO>> getLotsProchesPeremption(
            @RequestParam(defaultValue = "7") int joursAvant) {
        List<LotProduit> lots = lotProduitService.getLotsProchesPeremption(joursAvant);
        return ResponseEntity.ok(lotProduitService.toDTOList(lots));
    }

    @GetMapping("/perimes")
    @Operation(summary = "Lots périmés", description = "Récupère tous les lots périmés")
    public ResponseEntity<List<LotProduitDTO>> getLotsPerimes() {
        List<LotProduit> lots = lotProduitService.getLotsPerimes();
        return ResponseEntity.ok(lotProduitService.toDTOList(lots));
    }

    @GetMapping("/produit/{produitId}/fifo")
    @Operation(summary = "Lots disponibles FIFO", description = "Récupère les lots disponibles d'un produit triés par FIFO (First In First Out)")
    public ResponseEntity<List<LotProduitDTO>> getLotsDisponiblesFIFO(@PathVariable Long produitId) {
        List<LotProduit> lots = lotProduitService.getLotsDisponiblesFIFO(produitId);
        return ResponseEntity.ok(lotProduitService.toDTOList(lots));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un lot", description = "Met à jour les informations d'un lot")
    public ResponseEntity<LotProduitDTO> updateLot(@PathVariable Long id, @RequestBody LotProduit lot) {
        LotProduit updatedLot = lotProduitService.updateLot(id, lot);
        return ResponseEntity.ok(lotProduitService.toDTO(updatedLot));
    }

    @PostMapping("/{id}/retirer")
    @Operation(summary = "Retirer du stock d'un lot", description = "Retire une quantité du stock d'un lot spécifique")
    public ResponseEntity<LotProduitDTO> retirerQuantite(
            @PathVariable Long id,
            @RequestParam Integer quantite) {
        LotProduit lot = lotProduitService.retirerQuantiteLot(id, quantite);
        return ResponseEntity.ok(lotProduitService.toDTO(lot));
    }

    @PostMapping("/{id}/ajouter")
    @Operation(summary = "Ajouter du stock à un lot", description = "Ajoute une quantité au stock d'un lot")
    public ResponseEntity<LotProduitDTO> ajouterQuantite(
            @PathVariable Long id,
            @RequestParam Integer quantite) {
        LotProduit lot = lotProduitService.ajouterQuantiteLot(id, quantite);
        return ResponseEntity.ok(lotProduitService.toDTO(lot));
    }

    @PatchMapping("/{id}/statut")
    @Operation(summary = "Changer le statut d'un lot", description = "Change le statut d'un lot (ACTIF, PERIME, RAPPELE, etc.)")
    public ResponseEntity<LotProduitDTO> changerStatut(
            @PathVariable Long id,
            @RequestParam StatutLot statut) {
        LotProduit lot = lotProduitService.changerStatutLot(id, statut);
        return ResponseEntity.ok(lotProduitService.toDTO(lot));
    }

    @GetMapping("/produit/{produitId}/stock-total")
    @Operation(summary = "Stock total d'un produit", description = "Calcule le stock total d'un produit (tous lots actifs confondus)")
    public ResponseEntity<Integer> getStockTotalProduit(@PathVariable Long produitId) {
        Integer stockTotal = lotProduitService.getStockTotalProduit(produitId);
        return ResponseEntity.ok(stockTotal);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un lot", description = "Supprime un lot (uniquement si vide)")
    public ResponseEntity<Void> deleteLot(@PathVariable Long id) {
        lotProduitService.deleteLot(id);
        return ResponseEntity.noContent().build();
    }
}

