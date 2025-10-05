package comcom.supermarket.manager.service;

import comcom.supermarket.manager.model.commande.Commande;
import comcom.supermarket.manager.model.commande.StatutCommande;
import comcom.supermarket.manager.model.dto.CommandeDTO;

import java.time.LocalDate;
import java.util.List;

public interface CommandeService {

    Commande creerCommande(Commande commande);

    Commande getCommandeById(Long id);

    Commande getCommandeByNumero(String numeroCommande);

    List<Commande> getAllCommandes();

    List<Commande> getCommandesByFournisseur(Long fournisseurId);

    List<Commande> getCommandesByStatut(StatutCommande statut);

    List<Commande> getCommandesByPeriode(LocalDate dateDebut, LocalDate dateFin);

    Commande updateCommande(Long id, Commande commande);

    Commande changerStatut(Long id, StatutCommande nouveauStatut);

    void deleteCommande(Long id);

    // Fonctionnalités spécifiques
    Commande validerCommande(Long id);

    Commande recevoirCommande(Long id, LocalDate dateLivraison);

    Commande annulerCommande(Long id);

    // Commande automatique basée sur les alertes stock
    List<Commande> genererCommandesAutomatiques();

    // Conversion DTO
    CommandeDTO toDTO(Commande commande);

    List<CommandeDTO> toDTOList(List<Commande> commandes);
}

