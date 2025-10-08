package com.supermarket.manager.service;

import com.supermarket.manager.model.dto.*;
import com.supermarket.manager.model.produit.Categorie;
import com.supermarket.manager.model.produit.Produit;
import com.supermarket.manager.model.reporting.StatistiqueFrequentation;
import com.supermarket.manager.model.reporting.VenteProduit;
import com.supermarket.manager.repository.StatistiqueFrequentationRepository;
import com.supermarket.manager.repository.StockRepository;
import com.supermarket.manager.repository.VenteProduitRepository;
import com.supermarket.manager.service.impl.DashboardServiceImpl;
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
        produitGlace.setPrixVente(BigDecimal.valueOf(2500));
        
        produitEau = new Produit();
        produitEau.setId(2L);
        produitEau.setNom("Eau minérale");
        produitEau.setCategorie(categorieSurgelés);
        produitEau.setPrixVente(BigDecimal.valueOf(500));
        
        ventesTest = new ArrayList<>();
        
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
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        when(venteProduitRepository.findByDateVenteBetween(any(), any())).thenReturn(ventesTest);
        when(frequentationRepository.findByDateStatBetween(any(), any())).thenReturn(new ArrayList<>());
        when(frequentationRepository.findHeuresPointe(any(), any())).thenReturn(new ArrayList<>());
        when(venteProduitRepository.findStatistiquesByCategorie(any(), any())).thenReturn(new ArrayList<>());
        when(stockRepository.findStocksEnDessousDuSeuil()).thenReturn(new ArrayList<>());
        
        DashboardDTO dashboard = dashboardService.getDashboard(debut, fin);
        
        assertNotNull(dashboard);
        assertNotNull(dashboard.getPeriode());
        assertNotNull(dashboard.getVentesGlobales());
        assertNotNull(dashboard.getTopProduits());
        
        verify(venteProduitRepository, atLeastOnce()).findByDateVenteBetween(any(), any());
    }
    
    @Test
    void testGetVentesProduits() {
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        when(venteProduitRepository.findByDateVenteBetween(any(), any())).thenReturn(ventesTest);
        
        List<VenteProduitDTO> ventes = dashboardService.getVentesProduits(debut, fin);
        
        assertNotNull(ventes);
        assertFalse(ventes.isEmpty());
        assertEquals(2, ventes.size());
        
        VenteProduitDTO premierProduit = ventes.get(0);
        assertNotNull(premierProduit.getNomProduit());
        assertNotNull(premierProduit.getCategorie());
        assertTrue(premierProduit.getQuantiteVendue() > 0);
    }
    
    @Test
    void testGetHeuresPointe() {
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
        
        List<HeurePointeDTO> heuresPointe = dashboardService.getHeuresPointe(debut, fin);
        
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
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        Object[] categorieStat = new Object[]{
                "Surgelés",
                BigDecimal.valueOf(50000),
                BigDecimal.valueOf(35000),
                BigDecimal.valueOf(15000),
                2L
        };
        
        List<Object[]> statistiques = new ArrayList<>();
        statistiques.add(categorieStat);

        when(venteProduitRepository.findStatistiquesByCategorie(any(), any()))
                .thenReturn(statistiques);

        List<MargeBeneficiaireDTO> marges = dashboardService.getMargesParCategorie(debut, fin);
        
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
    void testGetStatistiquesFrequentation() {
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        StatistiqueFrequentation stat1 = new StatistiqueFrequentation();
        stat1.setDateStat(debut);
        stat1.setNombreClients(100);
        stat1.setNombreTransactions(50);
        stat1.setJourSemaine("MONDAY");
        stat1.setHeureDebut(LocalTime.of(18, 0));
        stat1.setHeureFin(LocalTime.of(19, 0));
        stat1.setEstHeurePointe(true);
        
        StatistiqueFrequentation stat2 = new StatistiqueFrequentation();
        stat2.setDateStat(debut.plusDays(5));
        stat2.setNombreClients(200);
        stat2.setNombreTransactions(100);
        stat2.setJourSemaine("SATURDAY");
        stat2.setHeureDebut(LocalTime.of(20, 0));
        stat2.setHeureFin(LocalTime.of(21, 0));
        stat2.setEstHeurePointe(true);
        
        when(frequentationRepository.findByDateStatBetween(debut, fin))
                .thenReturn(Arrays.asList(stat1, stat2));
        
        FrequentationDTO frequentation = dashboardService.getStatistiquesFrequentation(debut, fin);
        
        assertNotNull(frequentation);
        assertEquals(300, frequentation.getClientsTotal());
        assertTrue(frequentation.getClientsMoyenParJour() > 0);
        assertNotNull(frequentation.getJourPlusFrequente());
        assertTrue(frequentation.getHeurePointeMoyenne() >= 0);
    }
    
    @Test
    void testGetStatistiquesFrequentationSansDonnees() {
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        
        when(frequentationRepository.findByDateStatBetween(debut, fin))
                .thenReturn(new ArrayList<>());
        
        FrequentationDTO frequentation = dashboardService.getStatistiquesFrequentation(debut, fin);
        
        assertNotNull(frequentation);
        assertEquals(0, frequentation.getClientsTotal());
        assertEquals(0, frequentation.getClientsMoyenParJour());
        assertEquals("Aucune donnée", frequentation.getJourPlusFrequente());
    }
}
