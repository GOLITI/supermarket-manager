package comcom.supermarket.manager.service;

import comcom.supermarket.manager.model.caisse.CarteFidelite;
import comcom.supermarket.manager.model.dto.CarteFideliteDTO;
import java.util.List;

public interface CarteFideliteService {
    CarteFideliteDTO creerCarteFidelite(CarteFideliteDTO carteFideliteDTO);
    CarteFideliteDTO obtenirCarteFidelite(Long id);
    CarteFideliteDTO obtenirParNumeroCarte(String numeroCarte);
    List<CarteFideliteDTO> obtenirToutesLesCartes();
    CarteFideliteDTO mettreAJourCarteFidelite(Long id, CarteFideliteDTO carteFideliteDTO);
    void desactiverCarteFidelite(Long id);
    void activerCarteFidelite(Long id);
    void supprimerCarteFidelite(Long id);
    boolean carteExiste(String numeroCarte);
    CarteFidelite convertirVersEntite(CarteFideliteDTO dto);
    CarteFideliteDTO convertirVersDTO(CarteFidelite entite);
}

