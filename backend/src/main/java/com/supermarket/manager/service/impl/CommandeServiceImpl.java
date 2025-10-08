package com.supermarket.manager.service.impl;

import com.supermarket.manager.exception.BusinessException;
import com.supermarket.manager.exception.ResourceNotFoundException;
import com.supermarket.manager.model.commande.Commande;
import com.supermarket.manager.model.commande.LigneCommande;
import com.supermarket.manager.model.commande.StatutCommande;
import com.supermarket.manager.model.dto.CommandeDTO;
import com.supermarket.manager.model.dto.LigneCommandeDTO;
import com.supermarket.manager.model.dto.StockAlertDTO;
import com.supermarket.manager.model.produit.Stock;
import com.supermarket.manager.repository.CommandeRepository;
import com.supermarket.manager.repository.FournisseurRepository;
import com.supermarket.manager.repository.ProduitRepository;
import com.supermarket.manager.service.CommandeService;
import com.supermarket.manager.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final FournisseurRepository fournisseurRepository;
    private final ProduitRepository produitRepository;
    private final StockService stockService;

    @Override
    public Commande creerCommande(Commande commande) {
        log.info("Création d'une nouvelle commande pour le fournisseur: {}",
            commande.getFournisseur().getNom());

        // Générer un numéro de commande unique
        if (commande.getNumeroCommande() == null) {
            commande.setNumeroCommande(genererNumeroCommande());
        }

        commande.calculerMontantTotal();
        return commandeRepository.save(commande);
    }

    @Override
    @Transactional(readOnly = true)
    public Commande getCommandeById(Long id) {
        return commandeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Commande", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Commande getCommandeByNumero(String numeroCommande) {
        return commandeRepository.findByNumeroCommande(numeroCommande)
            .orElseThrow(() -> new ResourceNotFoundException("Commande", "numéro", numeroCommande));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Commande> getCommandesByFournisseur(Long fournisseurId) {
        return commandeRepository.findByFournisseurId(fournisseurId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Commande> getCommandesByStatut(StatutCommande statut) {
        return commandeRepository.findByStatut(statut);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Commande> getCommandesByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return commandeRepository.findByDateCommandeBetween(dateDebut, dateFin);
    }

    @Override
    public Commande updateCommande(Long id, Commande commande) {
        Commande existingCommande = getCommandeById(id);

        if (existingCommande.getStatut() != StatutCommande.BROUILLON &&
            existingCommande.getStatut() != StatutCommande.EN_ATTENTE) {
            throw new BusinessException("Impossible de modifier une commande avec le statut: " +
                existingCommande.getStatut());
        }

        existingCommande.setDateLivraisonPrevue(commande.getDateLivraisonPrevue());
        existingCommande.setNotes(commande.getNotes());
        existingCommande.calculerMontantTotal();

        return commandeRepository.save(existingCommande);
    }

    @Override
    public Commande changerStatut(Long id, StatutCommande nouveauStatut) {
        Commande commande = getCommandeById(id);

        // Valider la transition de statut
        validerTransitionStatut(commande.getStatut(), nouveauStatut);

        commande.setStatut(nouveauStatut);
        log.info("Changement de statut de la commande {} vers {}", id, nouveauStatut);

        return commandeRepository.save(commande);
    }

    @Override
    public void deleteCommande(Long id) {
        Commande commande = getCommandeById(id);

        if (commande.getStatut() != StatutCommande.BROUILLON) {
            throw new BusinessException("Impossible de supprimer une commande qui n'est pas en brouillon");
        }

        commandeRepository.delete(commande);
        log.info("Commande supprimée: {}", id);
    }

    @Override
    public Commande validerCommande(Long id) {
        Commande commande = getCommandeById(id);

        if (commande.getLignes().isEmpty()) {
            throw new BusinessException("Impossible de valider une commande sans lignes");
        }

        commande.setStatut(StatutCommande.CONFIRMEE);
        log.info("Commande {} validée", id);

        return commandeRepository.save(commande);
    }

    @Override
    public Commande recevoirCommande(Long id, LocalDate dateLivraison) {
        Commande commande = getCommandeById(id);

        if (commande.getStatut() != StatutCommande.EN_COURS_LIVRAISON &&
            commande.getStatut() != StatutCommande.CONFIRMEE) {
            throw new BusinessException("La commande doit être confirmée ou en cours de livraison");
        }

        commande.setDateLivraisonEffective(dateLivraison);

        // Vérifier si toutes les lignes sont reçues
        boolean toutRecu = commande.getLignes().stream()
            .allMatch(ligne -> ligne.getQuantiteRecue().equals(ligne.getQuantite()));

        if (toutRecu) {
            commande.setStatut(StatutCommande.LIVREE);
        } else {
            commande.setStatut(StatutCommande.PARTIELLEMENT_LIVREE);
        }

        // Mettre à jour les stocks
        for (LigneCommande ligne : commande.getLignes()) {
            if (ligne.getQuantiteRecue() > 0) {
                // Trouver le stock correspondant et ajouter la quantité reçue
                List<Stock> stocks = stockService.getStocksByProduit(ligne.getProduit().getId());
                if (!stocks.isEmpty()) {
                    // Utiliser le premier entrepôt disponible
                    stockService.ajouterQuantite(stocks.get(0).getId(), ligne.getQuantiteRecue());
                }
            }
        }

        log.info("Commande {} reçue le {}", id, dateLivraison);
        return commandeRepository.save(commande);
    }

    @Override
    public Commande annulerCommande(Long id) {
        Commande commande = getCommandeById(id);

        if (commande.getStatut() == StatutCommande.LIVREE) {
            throw new BusinessException("Impossible d'annuler une commande déjà livrée");
        }

        commande.setStatut(StatutCommande.ANNULEE);
        log.info("Commande {} annulée", id);

        return commandeRepository.save(commande);
    }

    @Override
    public List<Commande> genererCommandesAutomatiques() {
        log.info("Génération des commandes automatiques basées sur les alertes stock");

        List<StockAlertDTO> alertes = stockService.getStocksEnAlerte();

        // Regrouper les alertes par fournisseur
        Map<Long, List<StockAlertDTO>> alertesParFournisseur = alertes.stream()
            .filter(alerte -> alerte.getFournisseurId() != null)
            .collect(Collectors.groupingBy(StockAlertDTO::getFournisseurId));

        List<Commande> commandesCreees = new ArrayList<>();

        for (Map.Entry<Long, List<StockAlertDTO>> entry : alertesParFournisseur.entrySet()) {
            Long fournisseurId = entry.getKey();
            List<StockAlertDTO> alertesFournisseur = entry.getValue();

            Commande commande = Commande.builder()
                .numeroCommande(genererNumeroCommande())
                .fournisseur(fournisseurRepository.findById(fournisseurId)
                    .orElseThrow(() -> new ResourceNotFoundException("Fournisseur", "id", fournisseurId)))
                .statut(StatutCommande.BROUILLON)
                .dateCommande(LocalDate.now())
                .commandeAutomatique(true)
                .notes("Commande générée automatiquement suite aux alertes de stock")
                .build();

            for (StockAlertDTO alerte : alertesFournisseur) {
                LigneCommande ligne = LigneCommande.builder()
                    .produit(produitRepository.findById(alerte.getProduitId())
                        .orElseThrow(() -> new ResourceNotFoundException("Produit", "id", alerte.getProduitId())))
                    .quantite(alerte.getQuantiteRecommandee())
                    .quantiteRecue(0)
                    .prixUnitaire(alerte.getPrixAchat())
                    .notes("Recommandé automatiquement - Stock actuel: " + alerte.getQuantiteActuelle())
                    .build();

                commande.ajouterLigne(ligne);
            }

            // Calculer la date de livraison prévue
            Integer delaiLivraison = commande.getFournisseur().getDelaiLivraisonJours();
            if (delaiLivraison != null) {
                commande.setDateLivraisonPrevue(LocalDate.now().plusDays(delaiLivraison));
            }

            Commande commandeCreee = commandeRepository.save(commande);
            commandesCreees.add(commandeCreee);

            log.info("Commande automatique {} créée pour le fournisseur {} avec {} lignes",
                commandeCreee.getNumeroCommande(),
                commande.getFournisseur().getNom(),
                commande.getLignes().size());
        }

        return commandesCreees;
    }

    @Override
    public CommandeDTO toDTO(Commande commande) {
        List<LigneCommandeDTO> lignesDTO = commande.getLignes().stream()
            .map(ligne -> LigneCommandeDTO.builder()
                .id(ligne.getId())
                .produitId(ligne.getProduit().getId())
                .nomProduit(ligne.getProduit().getNom())
                .quantite(ligne.getQuantite())
                .quantiteRecue(ligne.getQuantiteRecue())
                .prixUnitaire(ligne.getPrixUnitaire())
                .montantLigne(ligne.getMontantLigne())
                .notes(ligne.getNotes())
                .build())
            .collect(Collectors.toList());

        return CommandeDTO.builder()
            .id(commande.getId())
            .numeroCommande(commande.getNumeroCommande())
            .fournisseurId(commande.getFournisseur().getId())
            .nomFournisseur(commande.getFournisseur().getNom())
            .statut(commande.getStatut())
            .dateCommande(commande.getDateCommande())
            .dateLivraisonPrevue(commande.getDateLivraisonPrevue())
            .dateLivraisonEffective(commande.getDateLivraisonEffective())
            .montantTotal(commande.getMontantTotal())
            .notes(commande.getNotes())
            .commandeAutomatique(commande.getCommandeAutomatique())
            .lignes(lignesDTO)
            .build();
    }

    @Override
    public List<CommandeDTO> toDTOList(List<Commande> commandes) {
        return commandes.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    private String genererNumeroCommande() {
        String prefix = "CMD";
        String date = LocalDate.now().toString().replace("-", "");
        String random = String.format("%04d", new Random().nextInt(10000));
        return prefix + "-" + date + "-" + random;
    }

    private void validerTransitionStatut(StatutCommande ancienStatut, StatutCommande nouveauStatut) {
        // Logique de validation des transitions de statut
        if (ancienStatut == StatutCommande.ANNULEE) {
            throw new BusinessException("Impossible de changer le statut d'une commande annulée");
        }

        if (ancienStatut == StatutCommande.LIVREE && nouveauStatut != StatutCommande.ANNULEE) {
            throw new BusinessException("Une commande livrée ne peut qu'être annulée");
        }
    }
}

