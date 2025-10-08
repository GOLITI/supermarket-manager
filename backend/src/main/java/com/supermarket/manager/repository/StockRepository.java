package com.supermarket.manager.repository;

import com.supermarket.manager.model.produit.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByProduitIdAndEntrepotId(Long produitId, Long entrepotId);

    List<Stock> findByProduitId(Long produitId);

    List<Stock> findByEntrepotId(Long entrepotId);

    // Stocks en alerte (quantité <= seuil)
    @Query("SELECT s FROM Stock s WHERE s.quantiteActuelle <= s.seuilAlerte")
    List<Stock> findStocksEnDessousDuSeuil();

    // Stocks en alerte (méthode alternative)
    @Query("SELECT s FROM Stock s WHERE s.quantiteActuelle <= s.seuilAlerte")
    List<Stock> findStocksEnAlerte();

    // Stocks en alerte par entrepôt
    @Query("SELECT s FROM Stock s WHERE s.quantiteActuelle <= s.seuilAlerte AND s.entrepot.id = :entrepotId")
    List<Stock> findStocksEnAlerteByEntrepot(@Param("entrepotId") Long entrepotId);

    // Stocks proches de la péremption
    @Query("SELECT s FROM Stock s WHERE s.datePeremption IS NOT NULL AND s.datePeremption <= :dateLimite")
    List<Stock> findStocksProchesPeremption(@Param("dateLimite") LocalDate dateLimite);

    // Stock total d'un produit (tous entrepôts)
    @Query("SELECT COALESCE(SUM(s.quantite), 0) FROM Stock s WHERE s.produit.id = :produitId")
    Integer getTotalStockProduit(@Param("produitId") Long produitId);
}
