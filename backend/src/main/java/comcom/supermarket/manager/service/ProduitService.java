package comcom.supermarket.manager.service;

import comcom.supermarket.manager.model.dto.ProduitDTO;
import comcom.supermarket.manager.model.produit.Produit;

import java.util.List;

public interface ProduitService {

    Produit creerProduit(Produit produit);

    Produit getProduitById(Long id);

    Produit getProduitByCode(String code);

    Produit getProduitByCodeBarres(String codeBarres);

    List<Produit> getAllProduits();

    List<Produit> getProduitsActifs();

    List<Produit> getProduitsByCategorie(Long categorieId);

    List<Produit> getProduitsByFournisseur(Long fournisseurId);

    List<Produit> searchProduits(String search);

    Produit updateProduit(Long id, Produit produit);

    void deleteProduit(Long id);

    ProduitDTO toDTO(Produit produit);

    List<ProduitDTO> toDTOList(List<Produit> produits);
}

