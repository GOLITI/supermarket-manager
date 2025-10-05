package comcom.supermarket.manager.controller;

import comcom.supermarket.manager.model.dto.ProduitDTO;
import comcom.supermarket.manager.model.produit.Produit;
import comcom.supermarket.manager.service.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProduitController {

    private final ProduitService produitService;

    @GetMapping
    public ResponseEntity<List<ProduitDTO>> getAllProduits() {
        List<Produit> produits = produitService.getAllProduits();
        return ResponseEntity.ok(produitService.toDTOList(produits));
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<ProduitDTO>> getProduitsActifs() {
        List<Produit> produits = produitService.getProduitsActifs();
        return ResponseEntity.ok(produitService.toDTOList(produits));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitDTO> getProduitById(@PathVariable Long id) {
        Produit produit = produitService.getProduitById(id);
        return ResponseEntity.ok(produitService.toDTO(produit));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ProduitDTO> getProduitByCode(@PathVariable String code) {
        Produit produit = produitService.getProduitByCode(code);
        return ResponseEntity.ok(produitService.toDTO(produit));
    }

    @GetMapping("/code-barres/{codeBarres}")
    public ResponseEntity<ProduitDTO> getProduitByCodeBarres(@PathVariable String codeBarres) {
        Produit produit = produitService.getProduitByCodeBarres(codeBarres);
        return ResponseEntity.ok(produitService.toDTO(produit));
    }

    @GetMapping("/categorie/{categorieId}")
    public ResponseEntity<List<ProduitDTO>> getProduitsByCategorie(@PathVariable Long categorieId) {
        List<Produit> produits = produitService.getProduitsByCategorie(categorieId);
        return ResponseEntity.ok(produitService.toDTOList(produits));
    }

    @GetMapping("/fournisseur/{fournisseurId}")
    public ResponseEntity<List<ProduitDTO>> getProduitsByFournisseur(@PathVariable Long fournisseurId) {
        List<Produit> produits = produitService.getProduitsByFournisseur(fournisseurId);
        return ResponseEntity.ok(produitService.toDTOList(produits));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProduitDTO>> searchProduits(@RequestParam String q) {
        List<Produit> produits = produitService.searchProduits(q);
        return ResponseEntity.ok(produitService.toDTOList(produits));
    }

    @PostMapping
    public ResponseEntity<ProduitDTO> creerProduit(@RequestBody Produit produit) {
        Produit created = produitService.creerProduit(produit);
        return ResponseEntity.status(HttpStatus.CREATED).body(produitService.toDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduitDTO> updateProduit(@PathVariable Long id, @RequestBody Produit produit) {
        Produit updated = produitService.updateProduit(id, produit);
        return ResponseEntity.ok(produitService.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }
}

