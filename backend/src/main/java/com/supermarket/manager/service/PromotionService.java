package com.supermarket.manager.service;

import com.supermarket.manager.model.caisse.Promotion;
import com.supermarket.manager.model.dto.PromotionDTO;
import java.time.LocalDate;
import java.util.List;

public interface PromotionService {
    PromotionDTO creerPromotion(PromotionDTO promotionDTO);
    PromotionDTO obtenirPromotion(Long id);
    PromotionDTO obtenirParCode(String code);
    List<PromotionDTO> obtenirToutesLesPromotions();
    List<PromotionDTO> obtenirPromotionsValides();
    List<PromotionDTO> obtenirPromotionsValidesParProduit(Long produitId);
    PromotionDTO mettreAJourPromotion(Long id, PromotionDTO promotionDTO);
    void desactiverPromotion(Long id);
    void activerPromotion(Long id);
    void supprimerPromotion(Long id);
    void nettoyerPromotionsExpirees();
    Promotion convertirVersEntite(PromotionDTO dto);
    PromotionDTO convertirVersDTO(Promotion entite);
}

