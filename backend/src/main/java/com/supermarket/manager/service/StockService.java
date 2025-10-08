package com.supermarket.manager.service;

import com.supermarket.manager.model.dto.StockAlertDTO;
import com.supermarket.manager.model.dto.StockDTO;
import com.supermarket.manager.model.produit.Stock;

import java.time.LocalDate;
import java.util.List;

public interface StockService {

    Stock creerStock(Stock stock);

    Stock getStockById(Long id);

    Stock getStockByProduitAndEntrepot(Long produitId, Long entrepotId);

    List<Stock> getAllStocks();

    List<Stock> getStocksByProduit(Long produitId);

    List<Stock> getStocksByEntrepot(Long entrepotId);

    Stock updateStock(Long id, Stock stock);

    void deleteStock(Long id);

    // Gestion des mouvements de stock
    Stock ajouterQuantite(Long stockId, Integer quantite);

    Stock retirerQuantite(Long stockId, Integer quantite);

    // Alertes et rapports
    List<StockAlertDTO> getStocksEnAlerte();

    List<StockAlertDTO> getStocksEnAlerteByEntrepot(Long entrepotId);

    List<Stock> getStocksProchesPeremption(Integer joursAvantPeremption);

    Integer getTotalStockProduit(Long produitId);

    // Conversion DTO
    StockDTO toDTO(Stock stock);

    List<StockDTO> toDTOList(List<Stock> stocks);
}

