package comcom.supermarket.manager.scheduler;

import comcom.supermarket.manager.model.dto.StockAlertDTO;
import comcom.supermarket.manager.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockAlertScheduler {

    private final StockService stockService;

    /**
     * Vérifie les stocks en alerte toutes les heures
     * Cron: 0 0 * * * * = chaque heure à la minute 0
     */
    @Scheduled(cron = "0 0 * * * *")
    public void verifierAlertes() {
        log.info("Début de la vérification des alertes de stock");

        try {
            List<StockAlertDTO> alertes = stockService.getStocksEnAlerte();

            if (!alertes.isEmpty()) {
                log.warn("⚠️ {} produit(s) en alerte de stock détecté(s)", alertes.size());

                // Compter les alertes par niveau
                long alertesCritiques = alertes.stream()
                    .filter(a -> "CRITIQUE".equals(a.getNiveauAlerte()))
                    .count();

                long alertesMoyennes = alertes.stream()
                    .filter(a -> "MOYEN".equals(a.getNiveauAlerte()))
                    .count();

                log.warn("   - Alertes CRITIQUES: {}", alertesCritiques);
                log.warn("   - Alertes MOYENNES: {}", alertesMoyennes);

                // Afficher les produits en alerte critique
                alertes.stream()
                    .filter(a -> "CRITIQUE".equals(a.getNiveauAlerte()))
                    .forEach(alerte -> log.error(
                        "STOCK CRITIQUE: {} - Quantité actuelle: {}, Seuil: {}",
                        alerte.getNomProduit(),
                        alerte.getQuantiteActuelle(),
                        alerte.getSeuilReapprovisionnement()
                    ));

                // TODO: Envoyer des notifications par email aux responsables
                // TODO: Générer des rapports automatiques

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

                stocksProchesPeremption.forEach(stock ->
                    log.warn("PÉREMPTION PROCHE: {} - Date de péremption: {}",
                        stock.getProduit().getNom(),
                        stock.getDatePeremption()
                    ));

                // TODO: Envoyer des notifications
                // TODO: Suggérer des promotions pour écouler les stocks

            } else {
                log.info("✓ Aucun produit proche de la péremption");
            }

        } catch (Exception e) {
            log.error("Erreur lors de la vérification des péremptions", e);
        }

        log.info("Fin de la vérification des péremptions");
    }
}

