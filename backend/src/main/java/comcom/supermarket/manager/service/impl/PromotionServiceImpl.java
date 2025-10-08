package comcom.supermarket.manager.service.impl;
import comcom.supermarket.manager.exception.BusinessException;
import comcom.supermarket.manager.exception.ResourceNotFoundException;
import comcom.supermarket.manager.model.caisse.Promotion;
import comcom.supermarket.manager.model.dto.PromotionDTO;
import comcom.supermarket.manager.model.produit.Produit;
import comcom.supermarket.manager.repository.ProduitRepository;
import comcom.supermarket.manager.repository.PromotionRepository;
import comcom.supermarket.manager.service.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final ProduitRepository produitRepository;
    @Override
    public PromotionDTO creerPromotion(PromotionDTO promotionDTO) {
        if (promotionRepository.findByCode(promotionDTO.getCode()).isPresent()) {
            throw new BusinessException("Code promo existe déjà");
        }
        if (promotionDTO.getDateDebut().isAfter(promotionDTO.getDateFin())) {
            throw new BusinessException("Dates invalides");
        }
        Promotion promotion = new Promotion();
        promotion.setCode(promotionDTO.getCode());
        promotion.setDescription(promotionDTO.getDescription());
        promotion.setType(promotionDTO.getType());
        promotion.setValeur(promotionDTO.getValeur());
        promotion.setMontantMinimum(promotionDTO.getMontantMinimum());
        promotion.setDateDebut(promotionDTO.getDateDebut());
        promotion.setDateFin(promotionDTO.getDateFin());
        promotion.setActive(promotionDTO.getActive() != null ? promotionDTO.getActive() : true);
        promotion.setUtilisationsMax(promotionDTO.getUtilisationsMax());
        promotion.setUtilisationsActuelles(0);
        promotion.setDateCreation(LocalDateTime.now());
        if (promotionDTO.getProduitId() != null) {
            Produit produit = produitRepository.findById(promotionDTO.getProduitId())
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé"));
            promotion.setProduit(produit);
        }
        return convertirVersDTO(promotionRepository.save(promotion));
    }
    @Override
    @Transactional(readOnly = true)
    public PromotionDTO obtenirPromotion(Long id) {
        return convertirVersDTO(promotionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Promotion non trouvée")));
    }
    @Override
    @Transactional(readOnly = true)
    public PromotionDTO obtenirParCode(String code) {
        return convertirVersDTO(promotionRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Promotion non trouvée")));
    }
    @Override
    @Transactional(readOnly = true)
    public List<PromotionDTO> obtenirToutesLesPromotions() {
        return promotionRepository.findAll().stream().map(this::convertirVersDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<PromotionDTO> obtenirPromotionsValides() {
        return promotionRepository.findPromotionsValides(LocalDate.now()).stream()
            .map(this::convertirVersDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<PromotionDTO> obtenirPromotionsValidesParProduit(Long produitId) {
        return promotionRepository.findPromotionsValidesParProduit(produitId, LocalDate.now()).stream()
            .map(this::convertirVersDTO).collect(Collectors.toList());
    }
    @Override
    public PromotionDTO mettreAJourPromotion(Long id, PromotionDTO promotionDTO) {
        Promotion promotion = promotionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Promotion non trouvée"));
        promotion.setCode(promotionDTO.getCode());
        promotion.setDescription(promotionDTO.getDescription());
        promotion.setType(promotionDTO.getType());
        promotion.setValeur(promotionDTO.getValeur());
        promotion.setMontantMinimum(promotionDTO.getMontantMinimum());
        promotion.setDateDebut(promotionDTO.getDateDebut());
        promotion.setDateFin(promotionDTO.getDateFin());
        promotion.setActive(promotionDTO.getActive());
        return convertirVersDTO(promotionRepository.save(promotion));
    }
    @Override
    public void desactiverPromotion(Long id) {
        Promotion promotion = promotionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Promotion non trouvée"));
        promotion.setActive(false);
        promotionRepository.save(promotion);
    }
    @Override
    public void activerPromotion(Long id) {
        Promotion promotion = promotionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Promotion non trouvée"));
        promotion.setActive(true);
        promotionRepository.save(promotion);
    }
    @Override
    public void supprimerPromotion(Long id) {
        promotionRepository.deleteById(id);
    }
    @Override
    public void nettoyerPromotionsExpirees() {
        List<Promotion> expirees = promotionRepository.findPromotionsExpirees(LocalDate.now());
        expirees.forEach(p -> p.setActive(false));
        promotionRepository.saveAll(expirees);
    }
    @Override
    public Promotion convertirVersEntite(PromotionDTO dto) {
        if (dto == null) return null;
        Promotion entite = new Promotion();
        entite.setId(dto.getId());
        entite.setCode(dto.getCode());
        entite.setDescription(dto.getDescription());
        entite.setType(dto.getType());
        entite.setValeur(dto.getValeur());
        entite.setMontantMinimum(dto.getMontantMinimum());
        entite.setDateDebut(dto.getDateDebut());
        entite.setDateFin(dto.getDateFin());
        entite.setActive(dto.getActive());
        return entite;
    }
    @Override
    public PromotionDTO convertirVersDTO(Promotion entite) {
        if (entite == null) return null;
        PromotionDTO dto = new PromotionDTO();
        dto.setId(entite.getId());
        dto.setCode(entite.getCode());
        dto.setDescription(entite.getDescription());
        dto.setType(entite.getType());
        dto.setValeur(entite.getValeur());
        dto.setMontantMinimum(entite.getMontantMinimum());
        dto.setProduitId(entite.getProduit() != null ? entite.getProduit().getId() : null);
        dto.setDateDebut(entite.getDateDebut());
        dto.setDateFin(entite.getDateFin());
        dto.setActive(entite.getActive());
        dto.setUtilisationsMax(entite.getUtilisationsMax());
        dto.setUtilisationsActuelles(entite.getUtilisationsActuelles());
        return dto;
    }
}
