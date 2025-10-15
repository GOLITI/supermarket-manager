package com.supermarket.manager.service.impl;

import com.supermarket.manager.exception.ResourceNotFoundException;
import com.supermarket.manager.model.dto.LotProduitDTO;
import com.supermarket.manager.model.produit.LotProduit;
import com.supermarket.manager.model.produit.StatutLot;
import com.supermarket.manager.repository.LotProduitRepository;
import com.supermarket.manager.service.LotProduitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LotProduitServiceImpl implements LotProduitService {

    private final LotProduitRepository lotProduitRepository;

    @Override
    public LotProduit creerLot(LotProduit lot) {
        log.info("Création d'un nouveau lot: {}", lot.getNumeroLot());
        return lotProduitRepository.save(lot);
    }

    @Override
    @Transactional(readOnly = true)
    public LotProduit getLotById(Long id) {
        return lotProduitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lot non trouvé avec l'ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public LotProduit getLotByNumero(String numeroLot) {
        return lotProduitRepository.findByNumeroLot(numeroLot)
                .orElseThrow(() -> new ResourceNotFoundException("Lot non trouvé avec le numéro: " + numeroLot));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LotProduit> getLotsByProduit(Long produitId) {
        return lotProduitRepository.findByProduitId(produitId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LotProduit> getLotsByEntrepot(Long entrepotId) {
        return lotProduitRepository.findByEntrepotId(entrepotId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LotProduit> getLotsActifsAvecStock() {
        return lotProduitRepository.findLotsActifsAvecStock();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LotProduit> getLotsProchesPeremption(int joursAvant) {
        LocalDate dateLimite = LocalDate.now().plusDays(joursAvant);
        return lotProduitRepository.findLotsProchesPeremption(dateLimite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LotProduit> getLotsPerimes() {
        return lotProduitRepository.findLotsPerimes(LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LotProduit> getLotsDisponiblesFIFO(Long produitId) {
        return lotProduitRepository.findLotsDisponiblesFIFO(produitId);
    }

    @Override
    public LotProduit updateLot(Long id, LotProduit lot) {
        LotProduit existingLot = getLotById(id);
        
        existingLot.setQuantiteActuelle(lot.getQuantiteActuelle());
        existingLot.setStatut(lot.getStatut());
        existingLot.setEmplacementStockage(lot.getEmplacementStockage());
        existingLot.setTemperatureStockage(lot.getTemperatureStockage());
        existingLot.setObservations(lot.getObservations());
        
        return lotProduitRepository.save(existingLot);
    }

    @Override
    public LotProduit retirerQuantiteLot(Long lotId, Integer quantite) {
        LotProduit lot = getLotById(lotId);
        lot.retirerQuantite(quantite);
        log.info("Retrait de {} unités du lot {}", quantite, lot.getNumeroLot());
        return lotProduitRepository.save(lot);
    }

    @Override
    public LotProduit ajouterQuantiteLot(Long lotId, Integer quantite) {
        LotProduit lot = getLotById(lotId);
        lot.ajouterQuantite(quantite);
        log.info("Ajout de {} unités au lot {}", quantite, lot.getNumeroLot());
        return lotProduitRepository.save(lot);
    }

    @Override
    public LotProduit changerStatutLot(Long lotId, StatutLot nouveauStatut) {
        LotProduit lot = getLotById(lotId);
        lot.setStatut(nouveauStatut);
        log.info("Changement de statut du lot {} vers {}", lot.getNumeroLot(), nouveauStatut);
        return lotProduitRepository.save(lot);
    }

    @Override
    public void deleteLot(Long id) {
        LotProduit lot = getLotById(id);
        lotProduitRepository.delete(lot);
        log.info("Lot supprimé: {}", lot.getNumeroLot());
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getStockTotalProduit(Long produitId) {
        return lotProduitRepository.getStockTotalProduit(produitId);
    }

    @Override
    public LotProduitDTO toDTO(LotProduit lot) {
        LocalDate now = LocalDate.now();
        Long joursAvantPeremption = lot.getDatePeremption() != null 
                ? ChronoUnit.DAYS.between(now, lot.getDatePeremption()) 
                : null;

        return LotProduitDTO.builder()
                .id(lot.getId())
                .numeroLot(lot.getNumeroLot())
                .produitId(lot.getProduit().getId())
                .nomProduit(lot.getProduit().getNom())
                .codeProduit(lot.getProduit().getCode())
                .entrepotId(lot.getEntrepot() != null ? lot.getEntrepot().getId() : null)
                .nomEntrepot(lot.getEntrepot() != null ? lot.getEntrepot().getNom() : null)
                .quantiteInitiale(lot.getQuantiteInitiale())
                .quantiteActuelle(lot.getQuantiteActuelle())
                .dateProduction(lot.getDateProduction())
                .datePeremption(lot.getDatePeremption())
                .dateReception(lot.getDateReception())
                .numeroCommande(lot.getNumeroCommande())
                .fournisseurLot(lot.getFournisseurLot())
                .statut(lot.getStatut())
                .emplacementStockage(lot.getEmplacementStockage())
                .temperatureStockage(lot.getTemperatureStockage())
                .observations(lot.getObservations())
                .perime(lot.isPerime())
                .prochePeremption(lot.isProcheDeLaPeremption(7))
                .joursAvantPeremption(joursAvantPeremption != null ? joursAvantPeremption.intValue() : null)
                .build();
    }

    @Override
    public List<LotProduitDTO> toDTOList(List<LotProduit> lots) {
        return lots.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}

