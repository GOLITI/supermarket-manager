package com.supermarket.manager.repository;

import com.supermarket.manager.model.produit.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    Optional<Produit> findByCode(String code);

    Optional<Produit> findByCodeBarres(String codeBarres);

    List<Produit> findByActifTrue();

    List<Produit> findByCategorieId(Long categorieId);

    List<Produit> findByFournisseurId(Long fournisseurId);

    @Query("SELECT p FROM Produit p WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.code) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Produit> searchProduits(@Param("search") String search);

    // Recherche par type de marque
    List<Produit> findByTypeMarque(com.supermarket.manager.model.produit.TypeMarque typeMarque);

    // Recherche par marque
    List<Produit> findByMarqueIgnoreCase(String marque);

    // Produits de marque propre actifs
    @Query("SELECT p FROM Produit p WHERE p.typeMarque = 'MARQUE_PROPRE' AND p.actif = true")
    List<Produit> findMarquePropresActifs();

    // Produits grandes marques actifs
    @Query("SELECT p FROM Produit p WHERE p.typeMarque = 'GRANDE_MARQUE' AND p.actif = true")
    List<Produit> findGrandesMarquesActifs();
}
