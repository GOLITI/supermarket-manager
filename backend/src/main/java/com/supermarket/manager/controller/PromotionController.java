package com.supermarket.manager.controller;
import com.supermarket.manager.model.dto.PromotionDTO;
import com.supermarket.manager.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PromotionController {
    private final PromotionService promotionService;
    @PostMapping
    public ResponseEntity<PromotionDTO> creerPromotion(@Valid @RequestBody PromotionDTO promotionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promotionService.creerPromotion(promotionDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<PromotionDTO> obtenirPromotion(@PathVariable Long id) {
        return ResponseEntity.ok(promotionService.obtenirPromotion(id));
    }
    @GetMapping("/code/{code}")
    public ResponseEntity<PromotionDTO> obtenirParCode(@PathVariable String code) {
        return ResponseEntity.ok(promotionService.obtenirParCode(code));
    }
    @GetMapping
    public ResponseEntity<List<PromotionDTO>> obtenirToutesLesPromotions() {
        return ResponseEntity.ok(promotionService.obtenirToutesLesPromotions());
    }
    @GetMapping("/valides")
    public ResponseEntity<List<PromotionDTO>> obtenirPromotionsValides() {
        return ResponseEntity.ok(promotionService.obtenirPromotionsValides());
    }
    @GetMapping("/produit/{produitId}")
    public ResponseEntity<List<PromotionDTO>> obtenirPromotionsParProduit(@PathVariable Long produitId) {
        return ResponseEntity.ok(promotionService.obtenirPromotionsValidesParProduit(produitId));
    }
    @PutMapping("/{id}")
    public ResponseEntity<PromotionDTO> mettreAJourPromotion(@PathVariable Long id, @Valid @RequestBody PromotionDTO promotionDTO) {
        return ResponseEntity.ok(promotionService.mettreAJourPromotion(id, promotionDTO));
    }
    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<Void> desactiverPromotion(@PathVariable Long id) {
        promotionService.desactiverPromotion(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/activer")
    public ResponseEntity<Void> activerPromotion(@PathVariable Long id) {
        promotionService.activerPromotion(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerPromotion(@PathVariable Long id) {
        promotionService.supprimerPromotion(id);
        return ResponseEntity.noContent().build();
    }
}
