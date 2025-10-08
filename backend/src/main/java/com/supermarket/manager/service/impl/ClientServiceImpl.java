package com.supermarket.manager.service.impl;

import com.supermarket.manager.exception.BusinessException;
import com.supermarket.manager.exception.ResourceNotFoundException;
import com.supermarket.manager.model.client.*;
import com.supermarket.manager.model.dto.*;
import com.supermarket.manager.repository.*;
import com.supermarket.manager.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final HistoriqueAchatRepository historiqueAchatRepository;
    private final MouvementPointsRepository mouvementPointsRepository;

    @Override
    public ClientDTO creerClient(ClientRequest request) {
        log.info("Création d'un nouveau client: {} {}", request.getNom(), request.getPrenom());
        
        // Vérifier l'unicité de l'email
        if (request.getEmail() != null && clientRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("Un client avec cet email existe déjà");
        }
        
        // Vérifier l'unicité du téléphone
        if (clientRepository.findByTelephone(request.getTelephone()).isPresent()) {
            throw new BusinessException("Un client avec ce téléphone existe déjà");
        }
        
        Client client = Client.builder()
            .numeroClient(genererNumeroClient())
            .nom(request.getNom())
            .prenom(request.getPrenom())
            .email(request.getEmail())
            .telephone(request.getTelephone())
            .adresse(request.getAdresse())
            .ville(request.getVille())
            .codePostal(request.getCodePostal())
            .dateNaissance(request.getDateNaissance())
            .dateInscription(LocalDateTime.now())
            .statut(StatutClient.ACTIF)
            .segment(SegmentClient.OCCASIONNEL)
            .pointsFidelite(0)
            .niveauFidelite(NiveauFidelite.BRONZE)
            .totalAchats(BigDecimal.ZERO)
            .nombreAchats(0)
            .panierMoyen(BigDecimal.ZERO)
            .accepteEmail(request.getAccepteEmail())
            .accepteSMS(request.getAccepteSMS())
            .accepteNotifications(request.getAccepteNotifications())
            .categoriesPreferees(request.getCategoriesPreferees())
            .build();
        
        client = clientRepository.save(client);
        log.info("Client créé avec succès: {} - {}", client.getNumeroClient(), client.getNumeroCarteFidelite());
        
        // Bonus de bienvenue
        ajouterPoints(client.getId(), 100, "BONUS_BIENVENUE", "Bonus de bienvenue");
        
        return convertirEnDTO(client);
    }

    @Override
    public ClientDTO modifierClient(Long id, ClientRequest request) {
        log.info("Modification du client ID: {}", id);
        
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + id));
        
        // Vérifier l'unicité de l'email si changé
        if (request.getEmail() != null && !request.getEmail().equals(client.getEmail())) {
            clientRepository.findByEmail(request.getEmail()).ifPresent(c -> {
                throw new BusinessException("Un client avec cet email existe déjà");
            });
        }
        
        // Vérifier l'unicité du téléphone si changé
        if (!request.getTelephone().equals(client.getTelephone())) {
            clientRepository.findByTelephone(request.getTelephone()).ifPresent(c -> {
                throw new BusinessException("Un client avec ce téléphone existe déjà");
            });
        }
        
        client.setNom(request.getNom());
        client.setPrenom(request.getPrenom());
        client.setEmail(request.getEmail());
        client.setTelephone(request.getTelephone());
        client.setAdresse(request.getAdresse());
        client.setVille(request.getVille());
        client.setCodePostal(request.getCodePostal());
        client.setDateNaissance(request.getDateNaissance());
        client.setAccepteEmail(request.getAccepteEmail());
        client.setAccepteSMS(request.getAccepteSMS());
        client.setAccepteNotifications(request.getAccepteNotifications());
        client.setCategoriesPreferees(request.getCategoriesPreferees());
        
        client = clientRepository.save(client);
        log.info("Client modifié avec succès: {}", client.getNumeroClient());
        
        return convertirEnDTO(client);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + id));
        return convertirEnDTO(client);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO getClientByNumeroCarteFidelite(String numeroCarteFidelite) {
        Client client = clientRepository.findByNumeroCarteFidelite(numeroCarteFidelite)
            .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec le numéro de carte: " + numeroCarteFidelite));
        return convertirEnDTO(client);
    }

    @Override
    public void supprimerClient(Long id) {
        log.info("Suppression du client ID: {}", id);
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + id));
        
        // Soft delete: on change le statut au lieu de supprimer
        client.setStatut(StatutClient.INACTIF);
        clientRepository.save(client);
        log.info("Client désactivé avec succès: {}", client.getNumeroClient());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
            .map(this::convertirEnDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> rechercherClients(String search) {
        return clientRepository.rechercherClients(search).stream()
            .map(this::convertirEnDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> getClientsBySegment(SegmentClient segment) {
        return clientRepository.findBySegment(segment).stream()
            .map(this::convertirEnDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> getClientsByStatut(StatutClient statut) {
        return clientRepository.findByStatut(statut).stream()
            .map(this::convertirEnDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FideliteInfoDTO getFideliteInfo(Long clientId) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + clientId));
        return construireFideliteInfo(client);
    }

    @Override
    @Transactional(readOnly = true)
    public FideliteInfoDTO getFideliteInfoByCarteFidelite(String numeroCarteFidelite) {
        Client client = clientRepository.findByNumeroCarteFidelite(numeroCarteFidelite)
            .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec le numéro de carte: " + numeroCarteFidelite));
        return construireFideliteInfo(client);
    }

    @Override
    public ClientDTO ajouterPoints(Long clientId, Integer points, String reference, String description) {
        log.info("Ajout de {} points au client ID: {}", points, clientId);
        
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + clientId));
        
        int soldeAvant = client.getPointsFidelite();
        client.ajouterPoints(points);
        
        // Enregistrer le mouvement
        MouvementPoints mouvement = MouvementPoints.builder()
            .client(client)
            .type(TypeMouvement.GAIN_ACHAT)
            .points(points)
            .dateMouvement(LocalDateTime.now())
            .reference(reference)
            .description(description)
            .soldeAvant(soldeAvant)
            .soldeApres(client.getPointsFidelite())
            .build();
        
        mouvementPointsRepository.save(mouvement);
        client = clientRepository.save(client);
        
        log.info("Points ajoutés avec succès. Nouveau solde: {}", client.getPointsFidelite());
        return convertirEnDTO(client);
    }

    @Override
    public ClientDTO utiliserPoints(Long clientId, Integer points, String reference) {
        log.info("Utilisation de {} points par le client ID: {}", points, clientId);
        
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + clientId));
        
        if (client.getPointsFidelite() < points) {
            throw new BusinessException("Points insuffisants. Disponibles: " + client.getPointsFidelite());
        }
        
        int soldeAvant = client.getPointsFidelite();
        client.utiliserPoints(points);
        
        // Enregistrer le mouvement
        MouvementPoints mouvement = MouvementPoints.builder()
            .client(client)
            .type(TypeMouvement.UTILISATION)
            .points(-points)
            .dateMouvement(LocalDateTime.now())
            .reference(reference)
            .description("Utilisation de points")
            .soldeAvant(soldeAvant)
            .soldeApres(client.getPointsFidelite())
            .build();
        
        mouvementPointsRepository.save(mouvement);
        client = clientRepository.save(client);
        
        log.info("Points utilisés avec succès. Nouveau solde: {}", client.getPointsFidelite());
        return convertirEnDTO(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MouvementPointsDTO> getHistoriquePoints(Long clientId) {
        return mouvementPointsRepository.findByClientIdOrderByDateMouvementDesc(clientId).stream()
            .map(this::convertirMouvementEnDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueAchatDTO> getHistoriqueAchats(Long clientId) {
        return historiqueAchatRepository.findByClientIdOrderByDateAchatDesc(clientId).stream()
            .map(this::convertirHistoriqueEnDTO)
            .collect(Collectors.toList());
    }

    @Override
    public HistoriqueAchatDTO enregistrerAchat(Long clientId, HistoriqueAchatDTO achatDTO) {
        log.info("Enregistrement d'un achat pour le client ID: {}", clientId);
        
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + clientId));
        
        // Créer l'historique d'achat
        HistoriqueAchat achat = HistoriqueAchat.builder()
            .client(client)
            .numeroTicket(achatDTO.getNumeroTicket())
            .dateAchat(achatDTO.getDateAchat())
            .montantTotal(achatDTO.getMontantTotal())
            .montantRemise(achatDTO.getMontantRemise())
            .montantPaye(achatDTO.getMontantPaye())
            .pointsGagnes(achatDTO.getPointsGagnes())
            .pointsUtilises(achatDTO.getPointsUtilises())
            .numeroCaisse(achatDTO.getNumeroCaisse())
            .nomCaissier(achatDTO.getNomCaissier())
            .build();
        
        // Ajouter les produits achetés
        if (achatDTO.getProduitsAchetes() != null) {
            List<HistoriqueAchat.ProduitAchete> produits = achatDTO.getProduitsAchetes().stream()
                .map(p -> new HistoriqueAchat.ProduitAchete(
                    p.getProduitId(),
                    p.getNomProduit(),
                    p.getCategorie(),
                    p.getQuantite(),
                    p.getPrixUnitaire(),
                    p.getMontantLigne()
                ))
                .collect(Collectors.toList());
            achat.setProduitsAchetes(produits);
        }
        
        achat = historiqueAchatRepository.save(achat);
        
        // Mettre à jour les statistiques du client
        client.setTotalAchats(client.getTotalAchats().add(achatDTO.getMontantTotal()));
        client.setNombreAchats(client.getNombreAchats() + 1);
        client.setDernierAchat(LocalDate.now());
        client.calculerPanierMoyen();
        client.calculerSegment();
        
        // Ajouter les points gagnés
        if (achatDTO.getPointsGagnes() > 0) {
            ajouterPoints(clientId, achatDTO.getPointsGagnes(), 
                         achatDTO.getNumeroTicket(), 
                         "Points gagnés sur achat");
        }
        
        // Déduire les points utilisés
        if (achatDTO.getPointsUtilises() > 0) {
            utiliserPoints(clientId, achatDTO.getPointsUtilises(), achatDTO.getNumeroTicket());
        }
        
        clientRepository.save(client);
        
        log.info("Achat enregistré avec succès: {}", achat.getNumeroTicket());
        return convertirHistoriqueEnDTO(achat);
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionPersonnaliseeDTO getPromotionsPersonnalisees(Long clientId) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + clientId));
        
        // Logique de recommandation basée sur les catégories préférées
        List<PromotionPersonnaliseeDTO.OffrePersonnalisee> offres = new ArrayList<>();
        
        // Exemple de recommandations basiques
        if (client.getCategoriesPreferees().contains("Fruits et Légumes")) {
            offres.add(PromotionPersonnaliseeDTO.OffrePersonnalisee.builder()
                .nomProduit("Fruits Bio")
                .categorie("Fruits et Légumes")
                .typePromotion("Réduction 15%")
                .description("15% de réduction sur tous les fruits bio")
                .raisonRecommandation("Basé sur vos achats habituels")
                .priorite(1)
                .build());
        }
        
        // Offre selon le niveau de fidélité
        String offreNiveau = switch (client.getNiveauFidelite()) {
            case DIAMANT -> "20% de réduction sur votre prochain achat";
            case OR -> "15% de réduction sur votre prochain achat";
            case ARGENT -> "10% de réduction sur votre prochain achat";
            default -> "5% de réduction sur votre prochain achat";
        };
        
        offres.add(PromotionPersonnaliseeDTO.OffrePersonnalisee.builder()
            .nomProduit("Offre fidélité")
            .categorie("Général")
            .typePromotion(offreNiveau)
            .description("Offre exclusive niveau " + client.getNiveauFidelite().getLibelle())
            .raisonRecommandation("Votre statut " + client.getNiveauFidelite().getLibelle())
            .priorite(2)
            .build());
        
        return PromotionPersonnaliseeDTO.builder()
            .clientId(client.getId())
            .nomClient(client.getNom() + " " + client.getPrenom())
            .numeroCarteFidelite(client.getNumeroCarteFidelite())
            .offres(offres)
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countClientsActifs() {
        return clientRepository.countClientsActifs();
    }

    @Override
    public void mettreAJourSegments() {
        log.info("Mise à jour des segments de tous les clients");
        List<Client> clients = clientRepository.findAll();
        clients.forEach(Client::calculerSegment);
        clientRepository.saveAll(clients);
        log.info("Segments mis à jour pour {} clients", clients.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> getClientsInactifs(int joursInactivite) {
        LocalDate dateLimit = LocalDate.now().minusDays(joursInactivite);
        return clientRepository.findClientsInactifsDernierAchatAvant(dateLimit).stream()
            .map(this::convertirEnDTO)
            .collect(Collectors.toList());
    }

    // Méthodes utilitaires privées

    private String genererNumeroClient() {
        return "CLI" + System.currentTimeMillis();
    }

    private FideliteInfoDTO construireFideliteInfo(Client client) {
        NiveauFidelite niveauActuel = client.getNiveauFidelite();
        NiveauFidelite procheNiveau = determinerProcheNiveau(niveauActuel);
        
        int pointsManquants = 0;
        if (procheNiveau != null) {
            pointsManquants = procheNiveau.getSeuilPoints() - client.getPointsFidelite();
        }
        
        // Calculer la réduction disponible (10 FCFA par point)
        BigDecimal reductionDisponible = BigDecimal.valueOf(client.getPointsFidelite() * 10);
        
        String message = String.format(
            "Vous êtes au niveau %s avec %d points. %s",
            niveauActuel.getLibelle(),
            client.getPointsFidelite(),
            procheNiveau != null 
                ? "Il vous manque " + pointsManquants + " points pour atteindre le niveau " + procheNiveau.getLibelle() + "."
                : "Vous êtes au niveau maximum !"
        );
        
        return FideliteInfoDTO.builder()
            .numeroCarteFidelite(client.getNumeroCarteFidelite())
            .pointsFidelite(client.getPointsFidelite())
            .niveauActuel(niveauActuel)
            .procheNiveau(procheNiveau)
            .pointsManquants(pointsManquants)
            .reductionDisponible(reductionDisponible)
            .reductionMaximale(niveauActuel.getReductionMaximale())
            .multiplicateurPoints(niveauActuel.getMultiplicateurPoints())
            .messageNiveau(message)
            .build();
    }

    private NiveauFidelite determinerProcheNiveau(NiveauFidelite niveauActuel) {
        return switch (niveauActuel) {
            case BRONZE -> NiveauFidelite.ARGENT;
            case ARGENT -> NiveauFidelite.OR;
            case OR -> NiveauFidelite.DIAMANT;
            case DIAMANT -> null;
        };
    }

    private ClientDTO convertirEnDTO(Client client) {
        return ClientDTO.builder()
            .id(client.getId())
            .numeroClient(client.getNumeroClient())
            .nom(client.getNom())
            .prenom(client.getPrenom())
            .email(client.getEmail())
            .telephone(client.getTelephone())
            .adresse(client.getAdresse())
            .ville(client.getVille())
            .codePostal(client.getCodePostal())
            .dateNaissance(client.getDateNaissance())
            .statut(client.getStatut())
            .segment(client.getSegment())
            .numeroCarteFidelite(client.getNumeroCarteFidelite())
            .pointsFidelite(client.getPointsFidelite())
            .niveauFidelite(client.getNiveauFidelite())
            .totalAchats(client.getTotalAchats())
            .nombreAchats(client.getNombreAchats())
            .panierMoyen(client.getPanierMoyen())
            .dernierAchat(client.getDernierAchat())
            .accepteEmail(client.getAccepteEmail())
            .accepteSMS(client.getAccepteSMS())
            .accepteNotifications(client.getAccepteNotifications())
            .categoriesPreferees(client.getCategoriesPreferees())
            .build();
    }

    private MouvementPointsDTO convertirMouvementEnDTO(MouvementPoints mouvement) {
        return MouvementPointsDTO.builder()
            .id(mouvement.getId())
            .type(mouvement.getType())
            .points(mouvement.getPoints())
            .dateMouvement(mouvement.getDateMouvement())
            .reference(mouvement.getReference())
            .description(mouvement.getDescription())
            .soldeAvant(mouvement.getSoldeAvant())
            .soldeApres(mouvement.getSoldeApres())
            .build();
    }

    private HistoriqueAchatDTO convertirHistoriqueEnDTO(HistoriqueAchat achat) {
        List<HistoriqueAchatDTO.ProduitAcheteDTO> produits = achat.getProduitsAchetes().stream()
            .map(p -> HistoriqueAchatDTO.ProduitAcheteDTO.builder()
                .produitId(p.getProduitId())
                .nomProduit(p.getNomProduit())
                .categorie(p.getCategorie())
                .quantite(p.getQuantite())
                .prixUnitaire(p.getPrixUnitaire())
                .montantLigne(p.getMontantLigne())
                .build())
            .collect(Collectors.toList());
        
        return HistoriqueAchatDTO.builder()
            .id(achat.getId())
            .numeroTicket(achat.getNumeroTicket())
            .dateAchat(achat.getDateAchat())
            .montantTotal(achat.getMontantTotal())
            .montantRemise(achat.getMontantRemise())
            .montantPaye(achat.getMontantPaye())
            .pointsGagnes(achat.getPointsGagnes())
            .pointsUtilises(achat.getPointsUtilises())
            .produitsAchetes(produits)
            .numeroCaisse(achat.getNumeroCaisse())
            .nomCaissier(achat.getNomCaissier())
            .build();
    }
}

