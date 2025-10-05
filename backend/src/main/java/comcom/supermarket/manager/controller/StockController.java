package comcom.supermarket.manager.controller;

import comcom.supermarket.manager.model.dto.StockAlertDTO;
import comcom.supermarket.manager.model.dto.StockDTO;
import comcom.supermarket.manager.model.produit.Stock;
import comcom.supermarket.manager.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockController {

    private final StockService stockService;

    @GetMapping
    public ResponseEntity<List<StockDTO>> getAllStocks() {
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stockService.toDTOList(stocks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> getStockById(@PathVariable Long id) {
        Stock stock = stockService.getStockById(id);
        return ResponseEntity.ok(stockService.toDTO(stock));
    }

    @GetMapping("/produit/{produitId}")
    public ResponseEntity<List<StockDTO>> getStocksByProduit(@PathVariable Long produitId) {
        List<Stock> stocks = stockService.getStocksByProduit(produitId);
        return ResponseEntity.ok(stockService.toDTOList(stocks));
    }

    @GetMapping("/entrepot/{entrepotId}")
    public ResponseEntity<List<StockDTO>> getStocksByEntrepot(@PathVariable Long entrepotId) {
        List<Stock> stocks = stockService.getStocksByEntrepot(entrepotId);
        return ResponseEntity.ok(stockService.toDTOList(stocks));
    }

    @GetMapping("/produit/{produitId}/entrepot/{entrepotId}")
    public ResponseEntity<StockDTO> getStockByProduitAndEntrepot(
            @PathVariable Long produitId,
            @PathVariable Long entrepotId) {
        Stock stock = stockService.getStockByProduitAndEntrepot(produitId, entrepotId);
        return ResponseEntity.ok(stockService.toDTO(stock));
    }

    @PostMapping
    public ResponseEntity<StockDTO> creerStock(@RequestBody Stock stock) {
        Stock created = stockService.creerStock(stock);
        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.toDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockDTO> updateStock(@PathVariable Long id, @RequestBody Stock stock) {
        Stock updated = stockService.updateStock(id, stock);
        return ResponseEntity.ok(stockService.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/ajouter")
    public ResponseEntity<StockDTO> ajouterQuantite(
            @PathVariable Long id,
            @RequestParam Integer quantite) {
        Stock stock = stockService.ajouterQuantite(id, quantite);
        return ResponseEntity.ok(stockService.toDTO(stock));
    }

    @PostMapping("/{id}/retirer")
    public ResponseEntity<StockDTO> retirerQuantite(
            @PathVariable Long id,
            @RequestParam Integer quantite) {
        Stock stock = stockService.retirerQuantite(id, quantite);
        return ResponseEntity.ok(stockService.toDTO(stock));
    }

    @GetMapping("/alertes")
    public ResponseEntity<List<StockAlertDTO>> getStocksEnAlerte() {
        List<StockAlertDTO> alertes = stockService.getStocksEnAlerte();
        return ResponseEntity.ok(alertes);
    }

    @GetMapping("/alertes/entrepot/{entrepotId}")
    public ResponseEntity<List<StockAlertDTO>> getStocksEnAlerteByEntrepot(@PathVariable Long entrepotId) {
        List<StockAlertDTO> alertes = stockService.getStocksEnAlerteByEntrepot(entrepotId);
        return ResponseEntity.ok(alertes);
    }

    @GetMapping("/peremption")
    public ResponseEntity<List<StockDTO>> getStocksProchesPeremption(
            @RequestParam(defaultValue = "30") Integer joursAvantPeremption) {
        List<Stock> stocks = stockService.getStocksProchesPeremption(joursAvantPeremption);
        return ResponseEntity.ok(stockService.toDTOList(stocks));
    }

    @GetMapping("/produit/{produitId}/total")
    public ResponseEntity<Integer> getTotalStockProduit(@PathVariable Long produitId) {
        Integer total = stockService.getTotalStockProduit(produitId);
        return ResponseEntity.ok(total);
    }
}

