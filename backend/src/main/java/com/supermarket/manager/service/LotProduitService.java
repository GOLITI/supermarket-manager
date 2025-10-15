package com.supermarket.manager.service;

import com.supermarket.manager.model.dto.LotProduitDTO;
import com.supermarket.manager.model.produit.LotProduit;
import com.supermarket.manager.model.produit.StatutLot;

import java.time.LocalDate;
import java.util.List;

public interface LotProduitService {

    LotProduit creerLot(LotProduit lot);

    LotProduit getLotById(Long id);

    LotProduit getLotByNumero(String numeroLot);

    List<LotProduit> getLotsByProduit(Long produitId);

    List<LotProduit> getLotsByEntrepot(Long entrepotId);

    List<LotProduit> getLotsActifsAvecStock();

    List<LotProduit> getLotsProchesPeremption(int joursAvant);

    List<LotProduit> getLotsPerimes();

    List<LotProduit> getLotsDisponiblesFIFO(Long produitId);

    LotProduit updateLot(Long id, LotProduit lot);

    LotProduit retirerQuantiteLot(Long lotId, Integer quantite);

    LotProduit ajouterQuantiteLot(Long lotId, Integer quantite);

    LotProduit changerStatutLot(Long lotId, StatutLot nouveauStatut);

    void deleteLot(Long id);

    Integer getStockTotalProduit(Long produitId);

    LotProduitDTO toDTO(LotProduit lot);

    List<LotProduitDTO> toDTOList(List<LotProduit> lots);
}

