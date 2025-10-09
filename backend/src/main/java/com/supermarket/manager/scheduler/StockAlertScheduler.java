package com.supermarket.manager.scheduler;

import com.supermarket.manager.model.dto.StockAlertDTO;
import com.supermarket.manager.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockAlertScheduler {

    private final StockService stockService;
    private final com.supermarket.manager.service.EmailService emailService;

    /**
     * Vérifie les stocks en alerte toutes les heures
     * Cron: 0 0 * * * * = chaque heure à la minute 0
     */
    @Scheduled(cron = "0 0 * * * *")
    public void verifierAlertes() {
        log.info("Début de la vérification des alertes de stock");

        try {
            List<com.supermarket.manager.model.produit.Stock> alertes = stockService.getStocksEnAlerte()
                .stream()
                .map(dto -> stockService.getStockById(dto.getStockId()))
                .toList();

            if (!alertes.isEmpty()) {
                log.warn("⚠️ {} produit(s) en alerte de stock détecté(s)", alertes.size());

                // Envoyer des emails pour chaque alerte critique
                alertes.forEach(stock -> {
                    if (stock.getQuantite() <= stock.getSeuilReapprovisionnement() / 2) {
                        emailService.sendAlertStock(stock);
                        log.info("Email d'alerte envoyé pour: {}", stock.getProduit().getNom());
                    }
                });

            } else {
                log.info("✓ Aucune alerte de stock détectée");
            }

        } catch (Exception e) {
            log.error("Erreur lors de la vérification des alertes de stock", e);
        }

        log.info("Fin de la vérification des alertes de stock");
    }

    /**
     * Vérifie les produits proches de la péremption chaque jour à 8h
     * Cron: 0 0 8 * * * = tous les jours à 8h00
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void verifierPeremption() {
        log.info("Début de la vérification des produits proches de la péremption");

        try {
            var stocksProchesPeremption = stockService.getStocksProchesPeremption(30);

            if (!stocksProchesPeremption.isEmpty()) {
                log.warn("⚠️ {} produit(s) proche(s) de la péremption (30 jours)",
                    stocksProchesPeremption.size());

                // Envoyer des emails d'alerte péremption
                stocksProchesPeremption.forEach(stock -> {
                    emailService.sendAlertPeremption(stock);
                    log.info("Email d'alerte péremption envoyé pour: {}", stock.getProduit().getNom());
                });

            } else {
                log.info("✓ Aucun produit proche de la péremption");
            }

        } catch (Exception e) {
            log.error("Erreur lors de la vérification des produits proches de la péremption", e);
        }

        log.info("Fin de la vérification des produits proches de la péremption");
    }
}
