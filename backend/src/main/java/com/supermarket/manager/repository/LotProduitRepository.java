package com.supermarket.manager.repository;

import com.supermarket.manager.model.produit.LotProduit;
import com.supermarket.manager.model.produit.StatutLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LotProduitRepository extends JpaRepository<LotProduit, Long> {

    Optional<LotProduit> findByNumeroLot(String numeroLot);

    List<LotProduit> findByProduitId(Long produitId);

    List<LotProduit> findByEntrepotId(Long entrepotId);

    List<LotProduit> findByStatut(StatutLot statut);

    // Lots actifs avec stock disponible
    @Query("SELECT l FROM LotProduit l WHERE l.statut = 'ACTIF' AND l.quantiteActuelle > 0")
    List<LotProduit> findLotsActifsAvecStock();

    // Lots par produit et statut
    @Query("SELECT l FROM LotProduit l WHERE l.produit.id = :produitId AND l.statut = :statut")
    List<LotProduit> findByProduitIdAndStatut(@Param("produitId") Long produitId, @Param("statut") StatutLot statut);

    // Lots proches de la péremption
    @Query("SELECT l FROM LotProduit l WHERE l.datePeremption IS NOT NULL AND l.datePeremption <= :dateLimite AND l.statut = 'ACTIF'")
    List<LotProduit> findLotsProchesPeremption(@Param("dateLimite") LocalDate dateLimite);

    // Lots périmés
    @Query("SELECT l FROM LotProduit l WHERE l.datePeremption IS NOT NULL AND l.datePeremption < :dateActuelle AND l.statut != 'PERIME'")
    List<LotProduit> findLotsPerimes(@Param("dateActuelle") LocalDate dateActuelle);

    // Lots par numéro de commande
    List<LotProduit> findByNumeroCommande(String numeroCommande);

    // Lots par produit avec FIFO (First In, First Out)
    @Query("SELECT l FROM LotProduit l WHERE l.produit.id = :produitId AND l.statut = 'ACTIF' AND l.quantiteActuelle > 0 ORDER BY l.dateReception ASC, l.datePeremption ASC")
    List<LotProduit> findLotsDisponiblesFIFO(@Param("produitId") Long produitId);

    // Stock total par produit (tous lots actifs)
    @Query("SELECT COALESCE(SUM(l.quantiteActuelle), 0) FROM LotProduit l WHERE l.produit.id = :produitId AND l.statut = 'ACTIF'")
    Integer getStockTotalProduit(@Param("produitId") Long produitId);

    // Lots par entrepôt et produit
    @Query("SELECT l FROM LotProduit l WHERE l.entrepot.id = :entrepotId AND l.produit.id = :produitId AND l.statut = 'ACTIF' ORDER BY l.dateReception ASC")
    List<LotProduit> findByEntrepotAndProduit(@Param("entrepotId") Long entrepotId, @Param("produitId") Long produitId);
}

