package com.supermarket.manager.repository;

import com.supermarket.manager.model.produit.*;
import com.supermarket.manager.model.fournisseur.Fournisseur;
import com.supermarket.manager.model.fournisseur.TypeFournisseur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    private Produit produit1;
    private Produit produit2;
    private Entrepot entrepot;

    @BeforeEach
    void setUp() {
        // Créer une catégorie
        Categorie categorie = Categorie.builder()
                .nom("Farines")
                .description("Produits de boulangerie")
                .type(TypeProduit.ALIMENTAIRE_SEC)
                .build();
        categorie = categorieRepository.save(categorie);

        // Créer un fournisseur
        Fournisseur fournisseur = Fournisseur.builder()
                .nom("Meunerie Test")
                .code("FOUR-TEST-001")
                .type(TypeFournisseur.PRODUCTEUR_LOCAL)
                .delaiLivraisonJours(3)
                .actif(true)
                .build();
        fournisseur = fournisseurRepository.save(fournisseur);

        // Créer un entrepôt
        entrepot = Entrepot.builder()
                .code("ENT-TEST-001")
                .nom("Entrepôt Test")
                .actif(true)
                .build();

        // Créer des produits
        produit1 = Produit.builder()
                .code("PROD-TEST-001")
                .nom("Farine T45 Test")
                .categorie(categorie)
                .fournisseur(fournisseur)
                .prixAchat(new BigDecimal("1500.00"))
                .prixVente(new BigDecimal("2000.00"))
                .actif(true)
                .build();
        produit1 = produitRepository.save(produit1);

        produit2 = Produit.builder()
                .code("PROD-TEST-002")
                .nom("Farine T55 Test")
                .categorie(categorie)
                .fournisseur(fournisseur)
                .prixAchat(new BigDecimal("1400.00"))
                .prixVente(new BigDecimal("1800.00"))
                .actif(true)
                .build();
        produit2 = produitRepository.save(produit2);
    }

    @Test
    void testSaveStock() {
        // Given
        Stock stock = Stock.builder()
                .produit(produit1)
                .entrepot(entrepot)
                .quantite(50)
                .seuilReapprovisionnement(100)
                .quantiteRecommandeeCommande(500)
                .build();

        // When
        Stock savedStock = stockRepository.save(stock);

        // Then
        assertThat(savedStock).isNotNull();
        assertThat(savedStock.getId()).isNotNull();
        assertThat(savedStock.getQuantite()).isEqualTo(50);
        assertThat(savedStock.getAlerteActive()).isTrue(); // 50 <= 100
    }

    @Test
    void testFindByProduitIdAndEntrepotId() {
        // Given
        Stock stock = Stock.builder()
                .produit(produit1)
                .entrepot(entrepot)
                .quantite(150)
                .seuilReapprovisionnement(100)
                .build();
        stockRepository.save(stock);

        // When
        Optional<Stock> found = stockRepository.findByProduitIdAndEntrepotId(
                produit1.getId(), entrepot.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getProduit().getId()).isEqualTo(produit1.getId());
        assertThat(found.get().getQuantite()).isEqualTo(150);
    }

    @Test
    void testFindStocksEnAlerte() {
        // Given - Stock en alerte
        Stock stockAlerte = Stock.builder()
                .produit(produit1)
                .entrepot(entrepot)
                .quantite(50)
                .seuilReapprovisionnement(100)
                .build();
        stockRepository.save(stockAlerte);

        // Stock normal
        Stock stockNormal = Stock.builder()
                .produit(produit2)
                .entrepot(entrepot)
                .quantite(150)
                .seuilReapprovisionnement(100)
                .build();
        stockRepository.save(stockNormal);

        // When
        List<Stock> stocksEnAlerte = stockRepository.findStocksEnAlerte();

        // Then
        assertThat(stocksEnAlerte).hasSize(1);
        assertThat(stocksEnAlerte.get(0).getProduit().getId()).isEqualTo(produit1.getId());
        assertThat(stocksEnAlerte.get(0).getQuantite()).isLessThanOrEqualTo(
                stocksEnAlerte.get(0).getSeuilReapprovisionnement());
    }

    @Test
    void testFindStocksProchesPeremption() {
        // Given
        LocalDate dateLimite = LocalDate.now().plusDays(30);

        Stock stockPeremption = Stock.builder()
                .produit(produit1)
                .entrepot(entrepot)
                .quantite(100)
                .seuilReapprovisionnement(50)
                .datePeremption(LocalDate.now().plusDays(20))
                .build();
        stockRepository.save(stockPeremption);

        Stock stockNormal = Stock.builder()
                .produit(produit2)
                .entrepot(entrepot)
                .quantite(100)
                .seuilReapprovisionnement(50)
                .datePeremption(LocalDate.now().plusDays(60))
                .build();
        stockRepository.save(stockNormal);

        // When
        List<Stock> stocksProches = stockRepository.findStocksProchesPeremption(dateLimite);

        // Then
        assertThat(stocksProches).hasSize(1);
        assertThat(stocksProches.get(0).getDatePeremption()).isBefore(dateLimite);
    }

    @Test
    void testGetTotalStockProduit() {
        // Given
        Stock stock1 = Stock.builder()
                .produit(produit1)
                .entrepot(entrepot)
                .quantite(50)
                .seuilReapprovisionnement(100)
                .build();
        stockRepository.save(stock1);

        // When
        Integer total = stockRepository.getTotalStockProduit(produit1.getId());

        // Then
        assertThat(total).isEqualTo(50);
    }

    @Test
    void testAlerteActiveAutoUpdate() {
        // Given - Stock au-dessus du seuil
        Stock stock = Stock.builder()
                .produit(produit1)
                .entrepot(entrepot)
                .quantite(150)
                .seuilReapprovisionnement(100)
                .build();
        stock = stockRepository.save(stock);

        // Then - Pas d'alerte
        assertThat(stock.getAlerteActive()).isFalse();

        // When - Réduire la quantité sous le seuil
        stock.setQuantite(50);
        stock = stockRepository.save(stock);

        // Then - Alerte activée
        assertThat(stock.getAlerteActive()).isTrue();
    }
}

