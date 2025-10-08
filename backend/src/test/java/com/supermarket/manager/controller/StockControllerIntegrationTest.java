package com.supermarket.manager.controller;

import com.supermarket.manager.model.dto.StockAlertDTO;
import com.supermarket.manager.model.dto.StockDTO;
import com.supermarket.manager.model.fournisseur.Fournisseur;
import com.supermarket.manager.model.fournisseur.TypeFournisseur;
import com.supermarket.manager.model.produit.*;
import com.supermarket.manager.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class StockControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    private Produit produit;
    private Entrepot entrepot;

    @BeforeEach
    void setUp() {
        // Nettoyer les données
        stockRepository.deleteAll();
        produitRepository.deleteAll();
        categorieRepository.deleteAll();
        fournisseurRepository.deleteAll();

        // Créer une catégorie
        Categorie categorie = Categorie.builder()
                .nom("Farines Test")
                .description("Produits de boulangerie")
                .type(TypeProduit.ALIMENTAIRE_SEC)
                .build();
        categorie = categorieRepository.save(categorie);

        // Créer un fournisseur
        Fournisseur fournisseur = Fournisseur.builder()
                .nom("Meunerie Integration Test")
                .code("FOUR-INT-001")
                .type(TypeFournisseur.PRODUCTEUR_LOCAL)
                .delaiLivraisonJours(3)
                .actif(true)
                .build();
        fournisseur = fournisseurRepository.save(fournisseur);

        // Créer un entrepôt
        entrepot = Entrepot.builder()
                .code("ENT-INT-001")
                .nom("Entrepôt Integration Test")
                .actif(true)
                .build();

        // Créer un produit
        produit = Produit.builder()
                .code("PROD-INT-001")
                .nom("Farine T45 Integration")
                .categorie(categorie)
                .fournisseur(fournisseur)
                .prixAchat(new BigDecimal("1500.00"))
                .prixVente(new BigDecimal("2000.00"))
                .actif(true)
                .build();
        produit = produitRepository.save(produit);
    }

    @Test
    void testGetAllStocks() throws Exception {
        // Given
        Stock stock = Stock.builder()
                .produit(produit)
                .entrepot(entrepot)
                .quantite(100)
                .seuilReapprovisionnement(50)
                .build();
        stockRepository.save(stock);

        // When & Then
        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].nomProduit").value("Farine T45 Integration"))
                .andExpect(jsonPath("$[0].quantite").value(100));
    }

    @Test
    void testGetStockById() throws Exception {
        // Given
        Stock stock = Stock.builder()
                .produit(produit)
                .entrepot(entrepot)
                .quantite(150)
                .seuilReapprovisionnement(100)
                .build();
        stock = stockRepository.save(stock);

        // When & Then
        mockMvc.perform(get("/api/stocks/" + stock.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(stock.getId()))
                .andExpect(jsonPath("$.quantite").value(150))
                .andExpect(jsonPath("$.nomProduit").value("Farine T45 Integration"));
    }

    @Test
    void testGetStocksEnAlerte() throws Exception {
        // Given - Stock en alerte
        Stock stockAlerte = Stock.builder()
                .produit(produit)
                .entrepot(entrepot)
                .quantite(30)
                .seuilReapprovisionnement(100)
                .quantiteRecommandeeCommande(500)
                .build();
        stockRepository.save(stockAlerte);

        // When & Then
        mockMvc.perform(get("/api/stocks/alertes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nomProduit").value("Farine T45 Integration"))
                .andExpect(jsonPath("$[0].quantiteActuelle").value(30))
                .andExpect(jsonPath("$[0].seuilReapprovisionnement").value(100))
                .andExpect(jsonPath("$[0].niveauAlerte").isNotEmpty());
    }

    @Test
    void testAjouterQuantite() throws Exception {
        // Given
        Stock stock = Stock.builder()
                .produit(produit)
                .entrepot(entrepot)
                .quantite(50)
                .seuilReapprovisionnement(100)
                .build();
        stock = stockRepository.save(stock);

        // When & Then
        mockMvc.perform(post("/api/stocks/" + stock.getId() + "/ajouter")
                        .param("quantite", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantite").value(150));
    }

    @Test
    void testRetirerQuantite() throws Exception {
        // Given
        Stock stock = Stock.builder()
                .produit(produit)
                .entrepot(entrepot)
                .quantite(200)
                .seuilReapprovisionnement(100)
                .build();
        stock = stockRepository.save(stock);

        // When & Then
        mockMvc.perform(post("/api/stocks/" + stock.getId() + "/retirer")
                        .param("quantite", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantite").value(150));
    }

    @Test
    void testRetirerQuantite_StockInsuffisant() throws Exception {
        // Given
        Stock stock = Stock.builder()
                .produit(produit)
                .entrepot(entrepot)
                .quantite(30)
                .seuilReapprovisionnement(100)
                .build();
        stock = stockRepository.save(stock);

        // When & Then
        mockMvc.perform(post("/api/stocks/" + stock.getId() + "/retirer")
                        .param("quantite", "50"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Stock insuffisant")));
    }

    @Test
    void testGetStockById_NotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/stocks/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("Stock")));
    }

    @Test
    void testGetTotalStockProduit() throws Exception {
        // Given
        Stock stock1 = Stock.builder()
                .produit(produit)
                .entrepot(entrepot)
                .quantite(100)
                .seuilReapprovisionnement(50)
                .build();
        stockRepository.save(stock1);

        // When & Then
        mockMvc.perform(get("/api/stocks/produit/" + produit.getId() + "/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));
    }

    @Test
    void testGetStocksProchesPeremption() throws Exception {
        // Given
        Stock stock = Stock.builder()
                .produit(produit)
                .entrepot(entrepot)
                .quantite(100)
                .seuilReapprovisionnement(50)
                .datePeremption(java.time.LocalDate.now().plusDays(15))
                .build();
        stockRepository.save(stock);

        // When & Then
        mockMvc.perform(get("/api/stocks/peremption")
                        .param("joursAvantPeremption", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nomProduit").value("Farine T45 Integration"));
    }
}

