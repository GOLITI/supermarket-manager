package comcom.supermarket.manager.service.impl;

import comcom.supermarket.manager.exception.ResourceNotFoundException;
import comcom.supermarket.manager.model.dto.ProduitDTO;
import comcom.supermarket.manager.model.produit.Produit;
import comcom.supermarket.manager.repository.ProduitRepository;
import comcom.supermarket.manager.service.ProduitService;
import comcom.supermarket.manager.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;
    private final StockService stockService;

    @Override
    public Produit creerProduit(Produit produit) {
        log.info("Création d'un nouveau produit: {}", produit.getNom());
        return produitRepository.save(produit);
    }

    @Override
    @Transactional(readOnly = true)
    public Produit getProduitById(Long id) {
        return produitRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produit", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Produit getProduitByCode(String code) {
        return produitRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Produit", "code", code));
    }

    @Override
    @Transactional(readOnly = true)
    public Produit getProduitByCodeBarres(String codeBarres) {
        return produitRepository.findByCodeBarres(codeBarres)
            .orElseThrow(() -> new ResourceNotFoundException("Produit", "code-barres", codeBarres));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produit> getProduitsActifs() {
        return produitRepository.findByActifTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produit> getProduitsByCategorie(Long categorieId) {
        return produitRepository.findByCategorieId(categorieId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produit> getProduitsByFournisseur(Long fournisseurId) {
        return produitRepository.findByFournisseurId(fournisseurId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produit> searchProduits(String search) {
        return produitRepository.searchProduits(search);
    }

    @Override
    public Produit updateProduit(Long id, Produit produit) {
        Produit existingProduit = getProduitById(id);

        existingProduit.setNom(produit.getNom());
        existingProduit.setDescription(produit.getDescription());
        existingProduit.setCategorie(produit.getCategorie());
        existingProduit.setFournisseur(produit.getFournisseur());
        existingProduit.setPrixAchat(produit.getPrixAchat());
        existingProduit.setPrixVente(produit.getPrixVente());
        existingProduit.setUnite(produit.getUnite());
        existingProduit.setCodeBarres(produit.getCodeBarres());
        existingProduit.setActif(produit.getActif());
        existingProduit.setDatePeremptionRequis(produit.getDatePeremptionRequis());
        existingProduit.setImageUrl(produit.getImageUrl());

        return produitRepository.save(existingProduit);
    }

    @Override
    public void deleteProduit(Long id) {
        Produit produit = getProduitById(id);
        produitRepository.delete(produit);
        log.info("Produit supprimé: {}", id);
    }

    @Override
    public ProduitDTO toDTO(Produit produit) {
        Integer stockTotal = stockService.getTotalStockProduit(produit.getId());

        return ProduitDTO.builder()
            .id(produit.getId())
            .code(produit.getCode())
            .nom(produit.getNom())
            .description(produit.getDescription())
            .categorieId(produit.getCategorie().getId())
            .nomCategorie(produit.getCategorie().getNom())
            .fournisseurId(produit.getFournisseur() != null ? produit.getFournisseur().getId() : null)
            .nomFournisseur(produit.getFournisseur() != null ? produit.getFournisseur().getNom() : null)
            .prixAchat(produit.getPrixAchat())
            .prixVente(produit.getPrixVente())
            .unite(produit.getUnite())
            .codeBarres(produit.getCodeBarres())
            .actif(produit.getActif())
            .datePeremptionRequis(produit.getDatePeremptionRequis())
            .imageUrl(produit.getImageUrl())
            .stockTotal(stockTotal)
            .build();
    }

    @Override
    public List<ProduitDTO> toDTOList(List<Produit> produits) {
        return produits.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
}

