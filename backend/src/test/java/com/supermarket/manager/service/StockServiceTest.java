package com.supermarket.manager.service;

import com.supermarket.manager.exception.ResourceNotFoundException;
import com.supermarket.manager.exception.StockInsuffisantException;
import com.supermarket.manager.model.dto.StockAlertDTO;
import com.supermarket.manager.model.dto.StockDTO;
import com.supermarket.manager.model.fournisseur.Fournisseur;
import com.supermarket.manager.model.fournisseur.TypeFournisseur;
import com.supermarket.manager.model.produit.*;
import com.supermarket.manager.repository.StockRepository;
import com.supermarket.manager.service.impl.StockServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockServiceImpl stockService;

    private Stock stock;
    private Produit produit;
    private Entrepot entrepot;
    private Categorie categorie;
    private Fournisseur fournisseur;

    @BeforeEach
    void setUp() {
        // Créer les objets de test
        categorie = Categorie.builder()
                .id(1L)
                .nom("Farines")
                .type(TypeProduit.ALIMENTAIRE_SEC)
                .build();

        fournisseur = Fournisseur.builder()
                .id(1L)
                .nom("Meunerie Test")
                .code("FOUR-001")
                .type(TypeFournisseur.PRODUCTEUR_LOCAL)
                .delaiLivraisonJours(3)
                .build();

        produit = Produit.builder()
                .id(1L)
                .code("PROD-001")
                .nom("Farine T45")
                .categorie(categorie)
                .fournisseur(fournisseur)
                .prixAchat(new BigDecimal("1500.00"))
                .prixVente(new BigDecimal("2000.00"))
                .build();

        entrepot = Entrepot.builder()
                .id(1L)
                .code("ENT-001")
                .nom("Entrepôt Principal")
                .build();

        stock = Stock.builder()
                .id(1L)
                .produit(produit)
                .entrepot(entrepot)
                .quantite(50)
                .seuilReapprovisionnement(100)
                .quantiteRecommandeeCommande(500)
                .alerteActive(true)
                .build();
    }

    @Test
    void testCreerStock() {
        // Given
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        // When
        Stock result = stockService.creerStock(stock);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    void testGetStockById_Success() {
        // Given
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

        // When
        Stock result = stockService.getStockById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getQuantite()).isEqualTo(50);
        verify(stockRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStockById_NotFound() {
        // Given
        when(stockRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> stockService.getStockById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Stock");
    }

    @Test
    void testAjouterQuantite() {
        // Given
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        // When
        Stock result = stockService.ajouterQuantite(1L, 100);

        // Then
        assertThat(result.getQuantite()).isEqualTo(150); // 50 + 100
        assertThat(result.getAlerteActive()).isFalse(); // 150 > 100
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void testRetirerQuantite_Success() {
        // Given
        stock.setQuantite(100);
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        // When
        Stock result = stockService.retirerQuantite(1L, 30);

        // Then
        assertThat(result.getQuantite()).isEqualTo(70); // 100 - 30
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void testRetirerQuantite_StockInsuffisant() {
        // Given
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

        // When & Then
        assertThatThrownBy(() -> stockService.retirerQuantite(1L, 100))
                .isInstanceOf(StockInsuffisantException.class)
                .hasMessageContaining("Stock insuffisant");
    }

    @Test
    void testGetStocksEnAlerte() {
        // Given
        List<Stock> stocksEnAlerte = Arrays.asList(stock);
        when(stockRepository.findStocksEnAlerte()).thenReturn(stocksEnAlerte);

        // When
        List<StockAlertDTO> result = stockService.getStocksEnAlerte();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNomProduit()).isEqualTo("Farine T45");
        assertThat(result.get(0).getQuantiteActuelle()).isEqualTo(50);
        assertThat(result.get(0).getSeuilReapprovisionnement()).isEqualTo(100);
        assertThat(result.get(0).getNiveauAlerte()).isIn("CRITIQUE", "MOYEN");
        verify(stockRepository, times(1)).findStocksEnAlerte();
    }

    @Test
    void testGetStocksProchesPeremption() {
        // Given
        stock.setDatePeremption(LocalDate.now().plusDays(20));
        List<Stock> stocksProches = Arrays.asList(stock);
        when(stockRepository.findStocksProchesPeremption(any(LocalDate.class)))
                .thenReturn(stocksProches);

        // When
        List<Stock> result = stockService.getStocksProchesPeremption(30);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDatePeremption())
                .isBefore(LocalDate.now().plusDays(31));
        verify(stockRepository, times(1))
                .findStocksProchesPeremption(any(LocalDate.class));
    }

    @Test
    void testGetTotalStockProduit() {
        // Given
        when(stockRepository.getTotalStockProduit(1L)).thenReturn(250);

        // When
        Integer result = stockService.getTotalStockProduit(1L);

        // Then
        assertThat(result).isEqualTo(250);
        verify(stockRepository, times(1)).getTotalStockProduit(1L);
    }

    @Test
    void testToDTO() {
        // When
        StockDTO result = stockService.toDTO(stock);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNomProduit()).isEqualTo("Farine T45");
        assertThat(result.getNomEntrepot()).isEqualTo("Entrepôt Principal");
        assertThat(result.getQuantite()).isEqualTo(50);
        assertThat(result.getSeuilReapprovisionnement()).isEqualTo(100);
        assertThat(result.getAlerteActive()).isTrue();
    }

    @Test
    void testUpdateStock() {
        // Given
        Stock updatedStock = Stock.builder()
                .quantite(200)
                .seuilReapprovisionnement(150)
                .quantiteRecommandeeCommande(600)
                .build();

        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        // When
        Stock result = stockService.updateStock(1L, updatedStock);

        // Then
        assertThat(result.getQuantite()).isEqualTo(200);
        assertThat(result.getSeuilReapprovisionnement()).isEqualTo(150);
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void testDeleteStock() {
        // Given
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        doNothing().when(stockRepository).delete(stock);

        // When
        stockService.deleteStock(1L);

        // Then
        verify(stockRepository, times(1)).delete(stock);
    }

    @Test
    void testNiveauAlerte_Critique() {
        // Given
        stock.setQuantite(0);
        List<Stock> stocks = Arrays.asList(stock);
        when(stockRepository.findStocksEnAlerte()).thenReturn(stocks);

        // When
        List<StockAlertDTO> result = stockService.getStocksEnAlerte();

        // Then
        assertThat(result.get(0).getNiveauAlerte()).isEqualTo("CRITIQUE");
    }

    @Test
    void testNiveauAlerte_Moyen() {
        // Given - pour avoir MOYEN, il faut quantite > seuil/2 ET quantite <= seuil
        stock.setQuantite(60); // 60 > 50 (100/2) ET 60 <= 100
        stock.setSeuilReapprovisionnement(100);
        List<Stock> stocks = Arrays.asList(stock);
        when(stockRepository.findStocksEnAlerte()).thenReturn(stocks);

        // When
        List<StockAlertDTO> result = stockService.getStocksEnAlerte();

        // Then
        assertThat(result.get(0).getNiveauAlerte()).isEqualTo("MOYEN");
    }
}

