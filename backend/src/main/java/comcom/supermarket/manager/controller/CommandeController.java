package comcom.supermarket.manager.controller;

import comcom.supermarket.manager.model.commande.Commande;
import comcom.supermarket.manager.model.commande.StatutCommande;
import comcom.supermarket.manager.model.dto.CommandeDTO;
import comcom.supermarket.manager.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CommandeController {

    private final CommandeService commandeService;

    @GetMapping
    public ResponseEntity<List<CommandeDTO>> getAllCommandes() {
        List<Commande> commandes = commandeService.getAllCommandes();
        return ResponseEntity.ok(commandeService.toDTOList(commandes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeDTO> getCommandeById(@PathVariable Long id) {
        Commande commande = commandeService.getCommandeById(id);
        return ResponseEntity.ok(commandeService.toDTO(commande));
    }

    @GetMapping("/numero/{numeroCommande}")
    public ResponseEntity<CommandeDTO> getCommandeByNumero(@PathVariable String numeroCommande) {
        Commande commande = commandeService.getCommandeByNumero(numeroCommande);
        return ResponseEntity.ok(commandeService.toDTO(commande));
    }

    @GetMapping("/fournisseur/{fournisseurId}")
    public ResponseEntity<List<CommandeDTO>> getCommandesByFournisseur(@PathVariable Long fournisseurId) {
        List<Commande> commandes = commandeService.getCommandesByFournisseur(fournisseurId);
        return ResponseEntity.ok(commandeService.toDTOList(commandes));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<CommandeDTO>> getCommandesByStatut(@PathVariable StatutCommande statut) {
        List<Commande> commandes = commandeService.getCommandesByStatut(statut);
        return ResponseEntity.ok(commandeService.toDTOList(commandes));
    }

    @GetMapping("/periode")
    public ResponseEntity<List<CommandeDTO>> getCommandesByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<Commande> commandes = commandeService.getCommandesByPeriode(dateDebut, dateFin);
        return ResponseEntity.ok(commandeService.toDTOList(commandes));
    }

    @PostMapping
    public ResponseEntity<CommandeDTO> creerCommande(@RequestBody Commande commande) {
        Commande created = commandeService.creerCommande(commande);
        return ResponseEntity.status(HttpStatus.CREATED).body(commandeService.toDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommandeDTO> updateCommande(@PathVariable Long id, @RequestBody Commande commande) {
        Commande updated = commandeService.updateCommande(id, commande);
        return ResponseEntity.ok(commandeService.toDTO(updated));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<CommandeDTO> changerStatut(
            @PathVariable Long id,
            @RequestParam StatutCommande statut) {
        Commande commande = commandeService.changerStatut(id, statut);
        return ResponseEntity.ok(commandeService.toDTO(commande));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        commandeService.deleteCommande(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/valider")
    public ResponseEntity<CommandeDTO> validerCommande(@PathVariable Long id) {
        Commande commande = commandeService.validerCommande(id);
        return ResponseEntity.ok(commandeService.toDTO(commande));
    }

    @PostMapping("/{id}/recevoir")
    public ResponseEntity<CommandeDTO> recevoirCommande(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateLivraison) {
        Commande commande = commandeService.recevoirCommande(id, dateLivraison);
        return ResponseEntity.ok(commandeService.toDTO(commande));
    }

    @PostMapping("/{id}/annuler")
    public ResponseEntity<CommandeDTO> annulerCommande(@PathVariable Long id) {
        Commande commande = commandeService.annulerCommande(id);
        return ResponseEntity.ok(commandeService.toDTO(commande));
    }

    @PostMapping("/generer-automatiques")
    public ResponseEntity<List<CommandeDTO>> genererCommandesAutomatiques() {
        List<Commande> commandes = commandeService.genererCommandesAutomatiques();
        return ResponseEntity.status(HttpStatus.CREATED).body(commandeService.toDTOList(commandes));
    }
}

