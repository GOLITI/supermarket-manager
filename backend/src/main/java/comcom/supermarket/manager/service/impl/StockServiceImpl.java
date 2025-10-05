package comcom.supermarket.manager.service.impl;

import comcom.supermarket.manager.exception.ResourceNotFoundException;
import comcom.supermarket.manager.exception.StockInsuffisantException;
import comcom.supermarket.manager.model.dto.StockAlertDTO;
import comcom.supermarket.manager.model.dto.StockDTO;
import comcom.supermarket.manager.model.produit.Stock;
import comcom.supermarket.manager.repository.StockRepository;
import comcom.supermarket.manager.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Override
    public Stock creerStock(Stock stock) {
        log.info("Création d'un nouveau stock pour le produit ID: {}", stock.getProduit().getId());
        return stockRepository.save(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public Stock getStockById(Long id) {
        return stockRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Stock", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Stock getStockByProduitAndEntrepot(Long produitId, Long entrepotId) {
        return stockRepository.findByProduitIdAndEntrepotId(produitId, entrepotId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Stock non trouvé pour produit %d et entrepôt %d", produitId, entrepotId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stock> getStocksByProduit(Long produitId) {
        return stockRepository.findByProduitId(produitId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stock> getStocksByEntrepot(Long entrepotId) {
        return stockRepository.findByEntrepotId(entrepotId);
    }

    @Override
    public Stock updateStock(Long id, Stock stock) {
        Stock existingStock = getStockById(id);

        existingStock.setQuantite(stock.getQuantite());
        existingStock.setSeuilReapprovisionnement(stock.getSeuilReapprovisionnement());
        existingStock.setQuantiteMaximale(stock.getQuantiteMaximale());
        existingStock.setQuantiteRecommandeeCommande(stock.getQuantiteRecommandeeCommande());
        existingStock.setDatePeremption(stock.getDatePeremption());

        return stockRepository.save(existingStock);
    }

    @Override
    public void deleteStock(Long id) {
        Stock stock = getStockById(id);
        stockRepository.delete(stock);
        log.info("Stock supprimé: {}", id);
    }

    @Override
    public Stock ajouterQuantite(Long stockId, Integer quantite) {
        Stock stock = getStockById(stockId);
        stock.ajouterQuantite(quantite);
        Stock updated = stockRepository.save(stock);
        log.info("Ajout de {} unités au stock {}", quantite, stockId);
        return updated;
    }

    @Override
    public Stock retirerQuantite(Long stockId, Integer quantite) {
        Stock stock = getStockById(stockId);

        if (stock.getQuantite() < quantite) {
            throw new StockInsuffisantException(
                stock.getProduit().getNom(),
                quantite,
                stock.getQuantite()
            );
        }

        stock.retirerQuantite(quantite);
        Stock updated = stockRepository.save(stock);
        log.info("Retrait de {} unités du stock {}", quantite, stockId);
        return updated;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockAlertDTO> getStocksEnAlerte() {
        List<Stock> stocks = stockRepository.findStocksEnAlerte();
        return stocks.stream()
            .map(this::toStockAlertDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockAlertDTO> getStocksEnAlerteByEntrepot(Long entrepotId) {
        List<Stock> stocks = stockRepository.findStocksEnAlerteByEntrepot(entrepotId);
        return stocks.stream()
            .map(this::toStockAlertDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stock> getStocksProchesPeremption(Integer joursAvantPeremption) {
        LocalDate dateLimite = LocalDate.now().plusDays(joursAvantPeremption);
        return stockRepository.findStocksProchesPeremption(dateLimite);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalStockProduit(Long produitId) {
        return stockRepository.getTotalStockProduit(produitId);
    }

    @Override
    public StockDTO toDTO(Stock stock) {
        return StockDTO.builder()
            .id(stock.getId())
            .produitId(stock.getProduit().getId())
            .nomProduit(stock.getProduit().getNom())
            .entrepotId(stock.getEntrepot().getId())
            .nomEntrepot(stock.getEntrepot().getNom())
            .quantite(stock.getQuantite())
            .seuilReapprovisionnement(stock.getSeuilReapprovisionnement())
            .quantiteMaximale(stock.getQuantiteMaximale())
            .quantiteRecommandeeCommande(stock.getQuantiteRecommandeeCommande())
            .datePeremption(stock.getDatePeremption())
            .alerteActive(stock.getAlerteActive())
            .build();
    }

    @Override
    public List<StockDTO> toDTOList(List<Stock> stocks) {
        return stocks.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    private StockAlertDTO toStockAlertDTO(Stock stock) {
        return StockAlertDTO.builder()
            .stockId(stock.getId())
            .produitId(stock.getProduit().getId())
            .codeProduit(stock.getProduit().getCode())
            .nomProduit(stock.getProduit().getNom())
            .categorie(stock.getProduit().getCategorie().getNom())
            .entrepotId(stock.getEntrepot().getId())
            .nomEntrepot(stock.getEntrepot().getNom())
            .quantiteActuelle(stock.getQuantite())
            .seuilReapprovisionnement(stock.getSeuilReapprovisionnement())
            .quantiteRecommandee(stock.getQuantiteRecommandeeCommande())
            .prixAchat(stock.getProduit().getPrixAchat())
            .fournisseurId(stock.getProduit().getFournisseur() != null ?
                stock.getProduit().getFournisseur().getId() : null)
            .nomFournisseur(stock.getProduit().getFournisseur() != null ?
                stock.getProduit().getFournisseur().getNom() : null)
            .delaiLivraisonJours(stock.getProduit().getFournisseur() != null ?
                stock.getProduit().getFournisseur().getDelaiLivraisonJours() : null)
            .build();
    }
}

