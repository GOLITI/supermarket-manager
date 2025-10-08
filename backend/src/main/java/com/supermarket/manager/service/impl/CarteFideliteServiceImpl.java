package com.supermarket.manager.service.impl;

import com.supermarket.manager.exception.BusinessException;
import com.supermarket.manager.exception.ResourceNotFoundException;
import com.supermarket.manager.model.caisse.CarteFidelite;
import com.supermarket.manager.model.dto.CarteFideliteDTO;
import com.supermarket.manager.repository.CarteFideliteRepository;
import com.supermarket.manager.service.CarteFideliteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CarteFideliteServiceImpl implements CarteFideliteService {

    private final CarteFideliteRepository carteFideliteRepository;

    @Override
    public CarteFideliteDTO creerCarteFidelite(CarteFideliteDTO carteFideliteDTO) {
        log.info("Création carte de fidélité pour {} {}", carteFideliteDTO.getNomClient(), carteFideliteDTO.getPrenomClient());
        if (carteFideliteDTO.getEmail() != null && carteFideliteRepository.existsByEmail(carteFideliteDTO.getEmail())) {
            throw new BusinessException("Une carte existe déjà pour cet email");
        }
        CarteFidelite carteFidelite = new CarteFidelite();
        String numeroCarte;
        do {
            numeroCarte = genererNumeroCarte();
        } while (carteFideliteRepository.existsByNumeroCarte(numeroCarte));
        carteFidelite.setNumeroCarte(numeroCarte);
        carteFidelite.setNomClient(carteFideliteDTO.getNomClient());
        carteFidelite.setPrenomClient(carteFideliteDTO.getPrenomClient());
        carteFidelite.setEmail(carteFideliteDTO.getEmail());
        carteFidelite.setTelephone(carteFideliteDTO.getTelephone());
        carteFidelite.setDateInscription(LocalDate.now());
        carteFidelite.setActive(true);
        carteFidelite.setDateExpiration(LocalDate.now().plusYears(2));
        CarteFidelite saved = carteFideliteRepository.save(carteFidelite);
        log.info("Carte créée : {}", saved.getNumeroCarte());
        return convertirVersDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CarteFideliteDTO obtenirCarteFidelite(Long id) {
        return convertirVersDTO(carteFideliteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Carte non trouvée : " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public CarteFideliteDTO obtenirParNumeroCarte(String numeroCarte) {
        return convertirVersDTO(carteFideliteRepository.findByNumeroCarte(numeroCarte)
            .orElseThrow(() -> new ResourceNotFoundException("Carte non trouvée : " + numeroCarte)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarteFideliteDTO> obtenirToutesLesCartes() {
        return carteFideliteRepository.findAll().stream().map(this::convertirVersDTO).collect(Collectors.toList());
    }

    @Override
    public CarteFideliteDTO mettreAJourCarteFidelite(Long id, CarteFideliteDTO carteFideliteDTO) {
        CarteFidelite carteFidelite = carteFideliteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Carte non trouvée : " + id));
        if (carteFideliteDTO.getEmail() != null && !carteFideliteDTO.getEmail().equals(carteFidelite.getEmail())
            && carteFideliteRepository.existsByEmail(carteFideliteDTO.getEmail())) {
            throw new BusinessException("Cet email est déjà associé à une autre carte");
        }
        carteFidelite.setNomClient(carteFideliteDTO.getNomClient());
        carteFidelite.setPrenomClient(carteFideliteDTO.getPrenomClient());
        carteFidelite.setEmail(carteFideliteDTO.getEmail());
        carteFidelite.setTelephone(carteFideliteDTO.getTelephone());
        return convertirVersDTO(carteFideliteRepository.save(carteFidelite));
    }

    @Override
    public void desactiverCarteFidelite(Long id) {
        CarteFidelite carte = carteFideliteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Carte non trouvée : " + id));
        carte.setActive(false);
        carteFideliteRepository.save(carte);
    }

    @Override
    public void activerCarteFidelite(Long id) {
        CarteFidelite carte = carteFideliteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Carte non trouvée : " + id));
        carte.setActive(true);
        carteFideliteRepository.save(carte);
    }

    @Override
    public void supprimerCarteFidelite(Long id) {
        if (!carteFideliteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Carte non trouvée : " + id);
        }
        carteFideliteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean carteExiste(String numeroCarte) {
        return carteFideliteRepository.existsByNumeroCarte(numeroCarte);
    }

    @Override
    public CarteFidelite convertirVersEntite(CarteFideliteDTO dto) {
        if (dto == null) return null;
        CarteFidelite entite = new CarteFidelite();
        entite.setId(dto.getId());
        entite.setNumeroCarte(dto.getNumeroCarte());
        entite.setNomClient(dto.getNomClient());
        entite.setPrenomClient(dto.getPrenomClient());
        entite.setEmail(dto.getEmail());
        entite.setTelephone(dto.getTelephone());
        entite.setDateInscription(dto.getDateInscription());
        entite.setPointsFidelite(dto.getPointsFidelite());
        entite.setTotalAchats(dto.getTotalAchats());
        entite.setNiveau(dto.getNiveau());
        entite.setActive(dto.getActive());
        entite.setDateExpiration(dto.getDateExpiration());
        return entite;
    }

    @Override
    public CarteFideliteDTO convertirVersDTO(CarteFidelite entite) {
        if (entite == null) return null;
        CarteFideliteDTO dto = new CarteFideliteDTO();
        dto.setId(entite.getId());
        dto.setNumeroCarte(entite.getNumeroCarte());
        dto.setNomClient(entite.getNomClient());
        dto.setPrenomClient(entite.getPrenomClient());
        dto.setEmail(entite.getEmail());
        dto.setTelephone(entite.getTelephone());
        dto.setDateInscription(entite.getDateInscription());
        dto.setPointsFidelite(entite.getPointsFidelite());
        dto.setTotalAchats(entite.getTotalAchats());
        dto.setNiveau(entite.getNiveau());
        dto.setActive(entite.getActive());
        dto.setDateExpiration(entite.getDateExpiration());
        return dto;
    }

    private String genererNumeroCarte() {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
        return String.format("%s-%s-%s-%s", uuid.substring(0,4), uuid.substring(4,8), uuid.substring(8,12), uuid.substring(12,16));
    }
}

