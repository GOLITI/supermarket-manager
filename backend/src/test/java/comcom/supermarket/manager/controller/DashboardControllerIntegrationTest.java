package comcom.supermarket.manager.controller;

import comcom.supermarket.manager.model.produit.Categorie;
import comcom.supermarket.manager.model.produit.Produit;
import comcom.supermarket.manager.model.produit.TypeProduit;
import comcom.supermarket.manager.model.reporting.StatistiqueFrequentation;
import comcom.supermarket.manager.model.reporting.VenteProduit;
import comcom.supermarket.manager.repository.*;
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
        produitGlace.setCodeBarre("1234567890123");
        produitGlace.setPrixVente(BigDecimal.valueOf(2500));
        produitGlace.setCategorie(categorieSurgelés);
        produitGlace.setTypeProduit(TypeProduit.SURGELE);
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
package comcom.supermarket.manager.service;

import comcom.supermarket.manager.model.dto.*;
import comcom.supermarket.manager.model.produit.Categorie;
import comcom.supermarket.manager.model.produit.Produit;
import comcom.supermarket.manager.model.produit.TypeProduit;
import comcom.supermarket.manager.model.reporting.StatistiqueFrequentation;
import comcom.supermarket.manager.model.reporting.VenteProduit;
import comcom.supermarket.manager.repository.StatistiqueFrequentationRepository;
import comcom.supermarket.manager.repository.StockRepository;
import comcom.supermarket.manager.repository.VenteProduitRepository;
import comcom.supermarket.manager.service.impl.DashboardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {
    
    @Mock
    private VenteProduitRepository venteProduitRepository;
    
    @Mock
    private StatistiqueFrequentationRepository frequentationRepository;
    
    @Mock
    private StockRepository stockRepository;
    
    @InjectMocks
    private DashboardServiceImpl dashboardService;
    
    private Produit produitGlace;
    private Produit produitEau;
    private Categorie categorieSurgelés;
    private List<VenteProduit> ventesTest;
    
    @BeforeEach
    void setUp() {
        categorieSurgelés = new Categorie();
        categorieSurgelés.setId(1L);
        categorieSurgelés.setNom("Surgelés");
        
        produitGlace = new Produit();
        produitGlace.setId(1L);
        produitGlace.setNom("Glace vanille");
        produitGlace.setCategorie(categorieSurgelés);
        produitGlace.setTypeProduit(TypeProduit.SURGELE);
        produitGlace.setPrixVente(BigDecimal.valueOf(2500));
        
        produitEau = new Produit();
        produitEau.setId(2L);
        produitEau.setNom("Eau minérale");
        produitEau.setCategorie(categorieSurgelés);
        produitEau.setTypeProduit(TypeProduit.ALIMENTAIRE_SEC);
        produitEau.setPrixVente(BigDecimal.valueOf(500));
        
        ventesTest = new ArrayList<>();
        
        // Vente 1
        VenteProduit vente1 = new VenteProduit();
        vente1.setId(1L);
        vente1.setProduit(produitGlace);
        vente1.setQuantiteVendue(10);
        vente1.setMontantVente(BigDecimal.valueOf(25000));
        vente1.setCoutAchat(BigDecimal.valueOf(15000));
        vente1.setMargeBeneficiaire(BigDecimal.valueOf(10000));
        vente1.setDateVente(LocalDateTime.now());
        vente1.setHeureVente(18);
        vente1.setJourSemaine("FRIDAY");
        ventesTest.add(vente1);
        
        // Vente 2
        VenteProduit vente2 = new VenteProduit();
        vente2.setId(2L);
        vente2.setProduit(produitEau);
        vente2.setQuantiteVendue(50);
        vente2.setMontantVente(BigDecimal.valueOf(25000));
        vente2.setCoutAchat(BigDecimal.valueOf(20000));
        vente2.setMargeBeneficiaire(BigDecimal.valueOf(5000));
        vente2.setDateVente(LocalDateTime.now());
        vente2.setHeureVente(20);
        vente2.setJourSemaine("SATURDAY");
        ventesTest.add(vente2);
    }
    
    @Test
    void testGetDashboard() {
        // Arrange
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        when(venteProduitRepository.findByDateVenteBetween(any(), any())).thenReturn(ventesTest);
        when(frequentationRepository.findByDateStatBetween(any(), any())).thenReturn(new ArrayList<>());
        when(frequentationRepository.findHeuresPointe(any(), any())).thenReturn(new ArrayList<>());
        when(venteProduitRepository.findStatistiquesByCategorie(any(), any())).thenReturn(new ArrayList<>());
        when(stockRepository.findStocksEnDessousDuSeuil()).thenReturn(new ArrayList<>());
        
        // Act
        DashboardDTO dashboard = dashboardService.getDashboard(debut, fin);
        
        // Assert
        assertNotNull(dashboard);
        assertNotNull(dashboard.getPeriode());
        assertNotNull(dashboard.getVentesGlobales());
        assertNotNull(dashboard.getTopProduits());
        
        verify(venteProduitRepository, atLeastOnce()).findByDateVenteBetween(any(), any());
    }
    
    @Test
    void testGetVentesProduits() {
        // Arrange
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        when(venteProduitRepository.findByDateVenteBetween(any(), any())).thenReturn(ventesTest);
        
        // Act
        List<VenteProduitDTO> ventes = dashboardService.getVentesProduits(debut, fin);
        
        // Assert
        assertNotNull(ventes);
        assertFalse(ventes.isEmpty());
        assertEquals(2, ventes.size());
        
        VenteProduitDTO premierProduit = ventes.get(0);
        assertNotNull(premierProduit.getNomProduit());
        assertNotNull(premierProduit.getCategorie());
        assertTrue(premierProduit.getQuantiteVendue() > 0);
    }
    
    @Test
    void testGetProduitsEnBaisse() {
        // Arrange
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        Double seuil = 10.0;
        
        // Ventes période actuelle (faible)
        List<VenteProduit> ventesActuelles = Arrays.asList(ventesTest.get(0));
        
        // Ventes période précédente (forte)
        VenteProduit ventePrecedente = new VenteProduit();
        ventePrecedente.setId(3L);
        ventePrecedente.setProduit(produitGlace);
        ventePrecedente.setQuantiteVendue(100); // Beaucoup plus que les 10 actuels
        ventePrecedente.setMontantVente(BigDecimal.valueOf(250000));
        ventePrecedente.setCoutAchat(BigDecimal.valueOf(150000));
        ventePrecedente.setMargeBeneficiaire(BigDecimal.valueOf(100000));
        ventePrecedente.setDateVente(LocalDateTime.now().minusDays(10));
        
        when(venteProduitRepository.findByDateVenteBetween(any(), any()))
                .thenReturn(ventesActuelles) // Pour la période actuelle
                .thenReturn(Arrays.asList(ventePrecedente)); // Pour la période précédente
        
        // Act
        List<VenteProduitDTO> produitsEnBaisse = dashboardService.getProduitsEnBaisse(debut, fin, seuil);
        
        // Assert
        assertNotNull(produitsEnBaisse);
        // La glace devrait être en baisse de 90% (10 vs 100)
    }
    
    @Test
    void testGetHeuresPointe() {
        // Arrange
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        StatistiqueFrequentation stat1 = new StatistiqueFrequentation();
        stat1.setId(1L);
        stat1.setDateStat(LocalDate.now());
        stat1.setHeureDebut(LocalTime.of(18, 0));
        stat1.setHeureFin(LocalTime.of(19, 0));
        stat1.setNombreClients(150);
        stat1.setNombreTransactions(80);
        stat1.setJourSemaine("FRIDAY");
        stat1.setEstHeurePointe(true);
        
        StatistiqueFrequentation stat2 = new StatistiqueFrequentation();
        stat2.setId(2L);
        stat2.setDateStat(LocalDate.now());
        stat2.setHeureDebut(LocalTime.of(20, 0));
        stat2.setHeureFin(LocalTime.of(21, 0));
        stat2.setNombreClients(180);
        stat2.setNombreTransactions(95);
        stat2.setJourSemaine("SATURDAY");
        stat2.setEstHeurePointe(true);
        
        when(frequentationRepository.findHeuresPointe(debut, fin))
                .thenReturn(Arrays.asList(stat1, stat2));
        
        // Act
        List<HeurePointeDTO> heuresPointe = dashboardService.getHeuresPointe(debut, fin);
        
        // Assert
        assertNotNull(heuresPointe);
        assertEquals(2, heuresPointe.size());
        
        HeurePointeDTO premierHeure = heuresPointe.get(0);
        assertNotNull(premierHeure.getJourSemaine());
        assertNotNull(premierHeure.getPlageHoraire());
        assertTrue(premierHeure.getNombreClients() > 0);
        assertTrue(premierHeure.getIntensite() > 0);
    }
    
    @Test
    void testGetMargesParCategorie() {
        // Arrange
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        Object[] categorieStat = new Object[]{
                "Surgelés",
                BigDecimal.valueOf(50000),
                BigDecimal.valueOf(35000),
                BigDecimal.valueOf(15000),
                2L
        };
        
        when(venteProduitRepository.findStatistiquesByCategorie(any(), any()))
                .thenReturn(Arrays.asList(categorieStat));
        
        // Act
        List<MargeBeneficiaireDTO> marges = dashboardService.getMargesParCategorie(debut, fin);
        
        // Assert
        assertNotNull(marges);
        assertEquals(1, marges.size());
        
        MargeBeneficiaireDTO marge = marges.get(0);
        assertEquals("Surgelés", marge.getCategorie());
        assertEquals(BigDecimal.valueOf(50000), marge.getChiffreAffaires());
        assertEquals(BigDecimal.valueOf(15000), marge.getMargeBrute());
        assertEquals(2, marge.getNombreProduits());
        assertTrue(marge.getPourcentageMarge().doubleValue() > 0);
    }
    
    @Test
    void testGetEvolutionVentesProduit() {
        // Arrange
        Long produitId = 1L;
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        Object[] evolution1 = new Object[]{
                java.sql.Date.valueOf(debut),
                10L,
                BigDecimal.valueOf(25000)
        };
        
        Object[] evolution2 = new Object[]{
                java.sql.Date.valueOf(debut.plusDays(1)),
                15L,
                BigDecimal.valueOf(37500)
        };
        
        when(venteProduitRepository.findEvolutionVentesProduit(eq(produitId), any(), any()))
                .thenReturn(Arrays.asList(evolution1, evolution2));
        
        // Act
        List<VenteProduitDTO> evolution = dashboardService.getEvolutionVentesProduit(produitId, debut, fin);
        
        // Assert
        assertNotNull(evolution);
        assertEquals(2, evolution.size());
        
        VenteProduitDTO jour1 = evolution.get(0);
        assertEquals(10, jour1.getQuantiteVendue());
        assertEquals(BigDecimal.valueOf(25000), jour1.getMontantVente());
    }
    
    @Test
    void testGetStatistiquesFrequentation() {
        // Arrange
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        StatistiqueFrequentation stat1 = new StatistiqueFrequentation();
        stat1.setDateStat(debut);
        stat1.setNombreClients(100);
        stat1.setNombreTransactions(50);
        stat1.setJourSemaine("MONDAY");
        stat1.setHeureDebut(LocalTime.of(18, 0));
        stat1.setEstHeurePointe(true);
        
        StatistiqueFrequentation stat2 = new StatistiqueFrequentation();
        stat2.setDateStat(debut.plusDays(5));
        stat2.setNombreClients(200);
        stat2.setNombreTransactions(100);
        stat2.setJourSemaine("SATURDAY");
        stat2.setHeureDebut(LocalTime.of(20, 0));
        stat2.setEstHeurePointe(true);
        
        when(frequentationRepository.findByDateStatBetween(debut, fin))
                .thenReturn(Arrays.asList(stat1, stat2));
        
        // Act
        FrequentationDTO frequentation = dashboardService.getStatistiquesFrequentation(debut, fin);
        
        // Assert
        assertNotNull(frequentation);
        assertEquals(300, frequentation.getClientsTotal());
        assertTrue(frequentation.getClientsMoyenParJour() > 0);
        assertNotNull(frequentation.getJourPlusFrequente());
        assertTrue(frequentation.getHeurePointeMoyenne() >= 0);
    }
    
    @Test
    void testGetStatistiquesFrequentationSansDonnees() {
        // Arrange
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        when(frequentationRepository.findByDateStatBetween(debut, fin))
                .thenReturn(new ArrayList<>());
        
        // Act
        FrequentationDTO frequentation = dashboardService.getStatistiquesFrequentation(debut, fin);
        
        // Assert
        assertNotNull(frequentation);
        assertEquals(0, frequentation.getClientsTotal());
        assertEquals(0, frequentation.getClientsMoyenParJour());
        assertEquals("Aucune donnée", frequentation.getJourPlusFrequente());
    }
}

