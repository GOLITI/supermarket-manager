package com.supermarket.manager.controller;

import com.supermarket.manager.model.produit.Categorie;
import com.supermarket.manager.model.produit.Produit;
import com.supermarket.manager.model.reporting.StatistiqueFrequentation;
import com.supermarket.manager.model.reporting.VenteProduit;
import com.supermarket.manager.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class DashboardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VenteProduitRepository venteProduitRepository;

    @Autowired
    private StatistiqueFrequentationRepository frequentationRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    private Produit produitGlace;
    private Categorie categorieSurgelés;

    @BeforeEach
    void setUp() {
        // Nettoyage
        venteProduitRepository.deleteAll();
        frequentationRepository.deleteAll();

        // Création catégorie
        categorieSurgelés = new Categorie();
        categorieSurgelés.setNom("Surgelés");
        categorieSurgelés.setDescription("Produits surgelés");
        categorieSurgelés = categorieRepository.save(categorieSurgelés);

        // Création produit
        produitGlace = new Produit();
        produitGlace.setNom("Glace vanille");
        produitGlace.setDescription("Glace à la vanille");
        produitGlace.setCodeBarres("1234567890123");
        produitGlace.setPrixVente(BigDecimal.valueOf(2500));
        produitGlace.setCategorie(categorieSurgelés);
        produitGlace = produitRepository.save(produitGlace);

        // Création de ventes test
        VenteProduit vente1 = new VenteProduit();
        vente1.setProduit(produitGlace);
        vente1.setQuantiteVendue(10);
        vente1.setMontantVente(BigDecimal.valueOf(25000));
        vente1.setCoutAchat(BigDecimal.valueOf(15000));
        vente1.setMargeBeneficiaire(BigDecimal.valueOf(10000));
        vente1.setDateVente(LocalDateTime.now().minusDays(2));
        venteProduitRepository.save(vente1);

        VenteProduit vente2 = new VenteProduit();
        vente2.setProduit(produitGlace);
        vente2.setQuantiteVendue(5);
        vente2.setMontantVente(BigDecimal.valueOf(12500));
        vente2.setCoutAchat(BigDecimal.valueOf(7500));
        vente2.setMargeBeneficiaire(BigDecimal.valueOf(5000));
        vente2.setDateVente(LocalDateTime.now());
        venteProduitRepository.save(vente2);

        // Création de statistiques de fréquentation
        StatistiqueFrequentation stat1 = new StatistiqueFrequentation();
        stat1.setDateStat(LocalDate.now().minusDays(1));
        stat1.setHeureDebut(LocalTime.of(18, 0));
        stat1.setHeureFin(LocalTime.of(19, 0));
        stat1.setNombreClients(150);
        stat1.setNombreTransactions(80);
        stat1.setJourSemaine("FRIDAY");
        stat1.setEstHeurePointe(true);
        frequentationRepository.save(stat1);

        StatistiqueFrequentation stat2 = new StatistiqueFrequentation();
        stat2.setDateStat(LocalDate.now());
        stat2.setHeureDebut(LocalTime.of(20, 0));
        stat2.setHeureFin(LocalTime.of(21, 0));
        stat2.setNombreClients(180);
        stat2.setNombreTransactions(95);
        stat2.setJourSemaine("SATURDAY");
        stat2.setEstHeurePointe(true);
        frequentationRepository.save(stat2);
    }

    @Test
    void testGetDashboard() throws Exception {
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();

        mockMvc.perform(get("/api/dashboard")
                        .param("debut", debut.toString())
                        .param("fin", fin.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.periode").exists())
                .andExpect(jsonPath("$.ventesGlobales").exists())
                .andExpect(jsonPath("$.ventesGlobales.chiffreAffaires").exists())
                .andExpect(jsonPath("$.topProduits").isArray())
                .andExpect(jsonPath("$.heuresPointe").isArray())
                .andExpect(jsonPath("$.margesParCategorie").isArray())
                .andExpect(jsonPath("$.frequentation").exists());
    }

    @Test
    void testGetDashboardSansParametres() throws Exception {
        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.periode").exists())
                .andExpect(jsonPath("$.ventesGlobales").exists());
    }
    
    @Test
    void testGetVentesProduits() throws Exception {
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        mockMvc.perform(get("/api/dashboard/ventes-produits")
                        .param("debut", debut.toString())
                        .param("fin", fin.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nomProduit").value("Glace vanille"))
                .andExpect(jsonPath("$[0].categorie").value("Surgelés"))
                .andExpect(jsonPath("$[0].quantiteVendue").value(15))
                .andExpect(jsonPath("$[0].montantVente").exists())
                .andExpect(jsonPath("$[0].margeBeneficiaire").exists());
    }
    
    @Test
    void testGetProduitsEnBaisse() throws Exception {
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        mockMvc.perform(get("/api/dashboard/produits-en-baisse")
                        .param("debut", debut.toString())
                        .param("fin", fin.toString())
                        .param("seuil", "5.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
    
    @Test
    void testGetHeuresPointe() throws Exception {
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        mockMvc.perform(get("/api/dashboard/heures-pointe")
                        .param("debut", debut.toString())
                        .param("fin", fin.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].jourSemaine").exists())
                .andExpect(jsonPath("$[0].plageHoraire").exists())
                .andExpect(jsonPath("$[0].nombreClients").isNumber())
                .andExpect(jsonPath("$[0].intensite").isNumber());
    }
    
    @Test
    void testGetMargesParCategorie() throws Exception {
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        mockMvc.perform(get("/api/dashboard/marges-categorie")
                        .param("debut", debut.toString())
                        .param("fin", fin.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].categorie").value("Surgelés"))
                .andExpect(jsonPath("$[0].chiffreAffaires").exists())
                .andExpect(jsonPath("$[0].margeBrute").exists())
                .andExpect(jsonPath("$[0].pourcentageMarge").exists())
                .andExpect(jsonPath("$[0].nombreProduits").value(1));
    }
    
    @Test
    void testGetEvolutionProduit() throws Exception {
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        mockMvc.perform(get("/api/dashboard/evolution-produit/{id}", produitGlace.getId())
                        .param("debut", debut.toString())
                        .param("fin", fin.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
    
    @Test
    void testGetFrequentation() throws Exception {
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        mockMvc.perform(get("/api/dashboard/frequentation")
                        .param("debut", debut.toString())
                        .param("fin", fin.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientsTotal").value(330))
                .andExpect(jsonPath("$.clientsMoyenParJour").exists())
                .andExpect(jsonPath("$.jourPlusFrequente").exists())
                .andExpect(jsonPath("$.heurePointeMoyenne").isNumber());
    }
}

