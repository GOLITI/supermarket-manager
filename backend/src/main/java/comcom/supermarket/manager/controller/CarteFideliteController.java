package comcom.supermarket.manager.controller;
import comcom.supermarket.manager.model.dto.CarteFideliteDTO;
import comcom.supermarket.manager.service.CarteFideliteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/cartes-fidelite")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CarteFideliteController {
    private final CarteFideliteService carteFideliteService;
    @PostMapping
    public ResponseEntity<CarteFideliteDTO> creerCarte(@Valid @RequestBody CarteFideliteDTO carteFideliteDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carteFideliteService.creerCarteFidelite(carteFideliteDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CarteFideliteDTO> obtenirCarte(@PathVariable Long id) {
        return ResponseEntity.ok(carteFideliteService.obtenirCarteFidelite(id));
    }
    @GetMapping("/numero/{numeroCarte}")
    public ResponseEntity<CarteFideliteDTO> obtenirParNumero(@PathVariable String numeroCarte) {
        return ResponseEntity.ok(carteFideliteService.obtenirParNumeroCarte(numeroCarte));
    }
    @GetMapping
    public ResponseEntity<List<CarteFideliteDTO>> obtenirToutesLesCartes() {
        return ResponseEntity.ok(carteFideliteService.obtenirToutesLesCartes());
    }
    @PutMapping("/{id}")
    public ResponseEntity<CarteFideliteDTO> mettreAJourCarte(@PathVariable Long id, @Valid @RequestBody CarteFideliteDTO carteFideliteDTO) {
        return ResponseEntity.ok(carteFideliteService.mettreAJourCarteFidelite(id, carteFideliteDTO));
    }
    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<Void> desactiverCarte(@PathVariable Long id) {
        carteFideliteService.desactiverCarteFidelite(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/activer")
    public ResponseEntity<Void> activerCarte(@PathVariable Long id) {
        carteFideliteService.activerCarteFidelite(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCarte(@PathVariable Long id) {
        carteFideliteService.supprimerCarteFidelite(id);
        return ResponseEntity.noContent().build();
    }
}
