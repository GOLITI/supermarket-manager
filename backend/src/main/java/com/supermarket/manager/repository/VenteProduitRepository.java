package com.supermarket.manager.repository;

import com.supermarket.manager.model.reporting.VenteProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VenteProduitRepository extends JpaRepository<VenteProduit, Long> {

    // Ventes par période
    List<VenteProduit> findByDateVenteBetween(LocalDateTime debut, LocalDateTime fin);

    // Top produits par quantité vendue
    @Query("SELECT v FROM VenteProduit v WHERE v.dateVente BETWEEN :debut AND :fin " +
           "ORDER BY v.quantiteVendue DESC")
    List<VenteProduit> findTopProduitsByQuantite(@Param("debut") LocalDateTime debut,
                                                   @Param("fin") LocalDateTime fin);

    // Ventes par produit et période
    @Query("SELECT v FROM VenteProduit v WHERE v.produit.id = :produitId " +
           "AND v.dateVente BETWEEN :debut AND :fin")
    List<VenteProduit> findByProduitAndPeriode(@Param("produitId") Long produitId,
                                                 @Param("debut") LocalDateTime debut,
                                                 @Param("fin") LocalDateTime fin);

    // Statistiques par catégorie
    @Query("SELECT v.produit.categorie.nom, SUM(v.montantVente), SUM(v.coutAchat), " +
           "SUM(v.margeBeneficiaire), COUNT(DISTINCT v.produit.id) " +
           "FROM VenteProduit v WHERE v.dateVente BETWEEN :debut AND :fin " +
           "GROUP BY v.produit.categorie.nom")
    List<Object[]> findStatistiquesByCategorie(@Param("debut") LocalDateTime debut,
                                                @Param("fin") LocalDateTime fin);

    // Ventes par jour de la semaine
    @Query("SELECT v.jourSemaine, SUM(v.quantiteVendue), SUM(v.montantVente) " +
           "FROM VenteProduit v WHERE v.dateVente BETWEEN :debut AND :fin " +
           "GROUP BY v.jourSemaine ORDER BY SUM(v.montantVente) DESC")
    List<Object[]> findVentesByJourSemaine(@Param("debut") LocalDateTime debut,
                                            @Param("fin") LocalDateTime fin);

    // Ventes par heure
    @Query("SELECT v.heureVente, COUNT(v), SUM(v.montantVente) " +
           "FROM VenteProduit v WHERE v.dateVente BETWEEN :debut AND :fin " +
           "GROUP BY v.heureVente ORDER BY v.heureVente")
    List<Object[]> findVentesByHeure(@Param("debut") LocalDateTime debut,
                                       @Param("fin") LocalDateTime fin);

    // Evolution des ventes d'un produit
    @Query("SELECT DATE(v.dateVente), SUM(v.quantiteVendue), SUM(v.montantVente) " +
           "FROM VenteProduit v WHERE v.produit.id = :produitId " +
           "AND v.dateVente BETWEEN :debut AND :fin " +
           "GROUP BY DATE(v.dateVente) ORDER BY DATE(v.dateVente)")
    List<Object[]> findEvolutionVentesProduit(@Param("produitId") Long produitId,
                                                @Param("debut") LocalDateTime debut,
                                                @Param("fin") LocalDateTime fin);
}
