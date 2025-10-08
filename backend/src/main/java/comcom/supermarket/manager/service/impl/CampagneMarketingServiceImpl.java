package comcom.supermarket.manager.service.impl;

import comcom.supermarket.manager.exception.BusinessException;
import comcom.supermarket.manager.exception.ResourceNotFoundException;
import comcom.supermarket.manager.model.client.*;
import comcom.supermarket.manager.model.dto.CampagneMarketingDTO;
import comcom.supermarket.manager.model.dto.CampagneMarketingRequest;
import comcom.supermarket.manager.repository.CampagneMarketingRepository;
import comcom.supermarket.manager.repository.ClientRepository;
import comcom.supermarket.manager.service.CampagneMarketingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CampagneMarketingServiceImpl implements CampagneMarketingService {

    private final CampagneMarketingRepository campagneRepository;
    private final ClientRepository clientRepository;

    @Override
    public CampagneMarketingDTO creerCampagne(CampagneMarketingRequest request) {
        log.info("Création d'une nouvelle campagne marketing: {}", request.getNom());
        
        // Validation des dates
        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new BusinessException("La date de fin doit être après la date de début");
        }
        
        CampagneMarketing campagne = CampagneMarketing.builder()
            .code(genererCodeCampagne())
            .nom(request.getNom())
            .description(request.getDescription())
            .type(request.getType())
            .statut(StatutCampagne.BROUILLON)
            .dateDebut(request.getDateDebut())
            .dateFin(request.getDateFin())
            .segmentsCibles(request.getSegmentsCibles())
            .niveauxCibles(request.getNiveauxCibles())
            .sujet(request.getSujet())
            .message(request.getMessage())
            .urlImage(request.getUrlImage())
            .typeOffre(request.getTypeOffre())
            .valeurOffre(request.getValeurOffre())
            .codePromo(request.getCodePromo())
            .nombreCibles(0)
            .nombreEnvoyes(0)
            .nombreOuvertures(0)
            .nombreClics(0)
            .nombreConversions(0)
            .chiffreAffaireGenere(BigDecimal.ZERO)
            .build();
        
        campagne = campagneRepository.save(campagne);
        log.info("Campagne créée avec succès: {}", campagne.getCode());
        
        return convertirEnDTO(campagne);
    }

    @Override
    public CampagneMarketingDTO modifierCampagne(Long id, CampagneMarketingRequest request) {
        log.info("Modification de la campagne ID: {}", id);
        
        CampagneMarketing campagne = campagneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Campagne non trouvée avec l'ID: " + id));
        
        // Vérifier que la campagne peut être modifiée
        if (campagne.getStatut() == StatutCampagne.EN_COURS || campagne.getStatut() == StatutCampagne.TERMINEE) {
            throw new BusinessException("Impossible de modifier une campagne en cours ou terminée");
        }
        
        campagne.setNom(request.getNom());
        campagne.setDescription(request.getDescription());
        campagne.setType(request.getType());
        campagne.setDateDebut(request.getDateDebut());
        campagne.setDateFin(request.getDateFin());
        campagne.setSegmentsCibles(request.getSegmentsCibles());
        campagne.setNiveauxCibles(request.getNiveauxCibles());
        campagne.setSujet(request.getSujet());
        campagne.setMessage(request.getMessage());
        campagne.setUrlImage(request.getUrlImage());
        campagne.setTypeOffre(request.getTypeOffre());
        campagne.setValeurOffre(request.getValeurOffre());
        campagne.setCodePromo(request.getCodePromo());
        
        campagne = campagneRepository.save(campagne);
        log.info("Campagne modifiée avec succès: {}", campagne.getCode());
        
        return convertirEnDTO(campagne);
    }

    @Override
    @Transactional(readOnly = true)
    public CampagneMarketingDTO getCampagneById(Long id) {
        CampagneMarketing campagne = campagneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Campagne non trouvée avec l'ID: " + id));
        return convertirEnDTO(campagne);
    }

    @Override
    public void supprimerCampagne(Long id) {
        log.info("Suppression de la campagne ID: {}", id);
        
        CampagneMarketing campagne = campagneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Campagne non trouvée avec l'ID: " + id));
        
        if (campagne.getStatut() == StatutCampagne.EN_COURS) {
            throw new BusinessException("Impossible de supprimer une campagne en cours");
        }
        
        campagneRepository.delete(campagne);
        log.info("Campagne supprimée avec succès: {}", campagne.getCode());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CampagneMarketingDTO> getAllCampagnes() {
        return campagneRepository.findAllOrderByDateCreationDesc().stream()
            .map(this::convertirEnDTO)
            .collect(Collectors.toList());
    }

    @Override
    public CampagneMarketingDTO programmerCampagne(Long id, LocalDate dateEnvoi) {
        log.info("Programmation de la campagne ID: {} pour le {}", id, dateEnvoi);
        
        CampagneMarketing campagne = campagneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Campagne non trouvée avec l'ID: " + id));
        
        if (campagne.getStatut() != StatutCampagne.BROUILLON) {
            throw new BusinessException("Seules les campagnes en brouillon peuvent être programmées");
        }
        
        if (dateEnvoi.isBefore(LocalDate.now())) {
            throw new BusinessException("La date d'envoi doit être dans le futur");
        }
        
        campagne.setStatut(StatutCampagne.PROGRAMMEE);
        campagne.setDateDebut(dateEnvoi);
        
        campagne = campagneRepository.save(campagne);
        log.info("Campagne programmée avec succès: {}", campagne.getCode());
        
        return convertirEnDTO(campagne);
    }

    @Override
    public CampagneMarketingDTO lancerCampagne(Long id) {
        log.info("Lancement de la campagne ID: {}", id);
        
        CampagneMarketing campagne = campagneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Campagne non trouvée avec l'ID: " + id));
        
        if (campagne.getStatut() != StatutCampagne.PROGRAMMEE && campagne.getStatut() != StatutCampagne.BROUILLON) {
            throw new BusinessException("La campagne ne peut pas être lancée dans son état actuel");
        }
        
        // Calculer les cibles
        calculerCibles(id);
        
        campagne.setStatut(StatutCampagne.EN_COURS);
        campagne.setDateEnvoi(LocalDateTime.now());
        
        campagne = campagneRepository.save(campagne);
        log.info("Campagne lancée avec succès: {}", campagne.getCode());
        
        return convertirEnDTO(campagne);
    }

    @Override
    public CampagneMarketingDTO terminerCampagne(Long id) {
        log.info("Clôture de la campagne ID: {}", id);
        
        CampagneMarketing campagne = campagneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Campagne non trouvée avec l'ID: " + id));
        
        campagne.setStatut(StatutCampagne.TERMINEE);
        campagne = campagneRepository.save(campagne);
        
        log.info("Campagne terminée avec succès: {}", campagne.getCode());
        return convertirEnDTO(campagne);
    }

    @Override
    public CampagneMarketingDTO annulerCampagne(Long id) {
        log.info("Annulation de la campagne ID: {}", id);
        
        CampagneMarketing campagne = campagneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Campagne non trouvée avec l'ID: " + id));
        
        if (campagne.getStatut() == StatutCampagne.TERMINEE) {
            throw new BusinessException("Impossible d'annuler une campagne terminée");
        }
        
        campagne.setStatut(StatutCampagne.ANNULEE);
        campagne = campagneRepository.save(campagne);
        
        log.info("Campagne annulée avec succès: {}", campagne.getCode());
        return convertirEnDTO(campagne);
    }

    @Override
    public CampagneMarketingDTO calculerCibles(Long id) {
        log.info("Calcul des cibles pour la campagne ID: {}", id);
        
        CampagneMarketing campagne = campagneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Campagne non trouvée avec l'ID: " + id));
        
        // Créer une variable finale pour utilisation dans les lambdas
        final CampagneMarketing campagneFinal = campagne;

        List<Client> clientsCibles = clientRepository.findByStatut(StatutClient.ACTIF);
        
        // Filtrer par segments si définis
        if (!campagneFinal.getSegmentsCibles().isEmpty()) {
            clientsCibles = clientsCibles.stream()
                .filter(c -> campagneFinal.getSegmentsCibles().contains(c.getSegment()))
                .collect(Collectors.toList());
        }
        
        // Filtrer par niveaux de fidélité si définis
        if (!campagneFinal.getNiveauxCibles().isEmpty()) {
            clientsCibles = clientsCibles.stream()
                .filter(c -> campagneFinal.getNiveauxCibles().contains(c.getNiveauFidelite()))
                .collect(Collectors.toList());
        }
        
        // Filtrer selon le type de campagne et les préférences des clients
        switch (campagneFinal.getType()) {
            case EMAIL -> clientsCibles = clientsCibles.stream()
                .filter(Client::getAccepteEmail)
                .filter(c -> c.getEmail() != null && !c.getEmail().isEmpty())
                .collect(Collectors.toList());
            case SMS -> clientsCibles = clientsCibles.stream()
                .filter(Client::getAccepteSMS)
                .collect(Collectors.toList());
            case NOTIFICATION -> clientsCibles = clientsCibles.stream()
                .filter(Client::getAccepteNotifications)
                .collect(Collectors.toList());
            case MULTI_CANAL -> {
                // Pour multi-canal, on garde tous ceux qui acceptent au moins un canal
                clientsCibles = clientsCibles.stream()
                    .filter(c -> c.getAccepteEmail() || c.getAccepteSMS() || c.getAccepteNotifications())
                    .collect(Collectors.toList());
            }
        }
        
        campagne.setNombreCibles(clientsCibles.size());
        campagne = campagneRepository.save(campagne);
        
        log.info("Cibles calculées: {} clients", clientsCibles.size());
        return convertirEnDTO(campagne);
    }

    @Override
    public CampagneMarketingDTO envoyerCampagne(Long id) {
        log.info("Envoi de la campagne ID: {}", id);
        
        CampagneMarketing campagne = campagneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Campagne non trouvée avec l'ID: " + id));
        
        if (campagne.getStatut() != StatutCampagne.EN_COURS) {
            throw new BusinessException("La campagne doit être en cours pour être envoyée");
        }
        
        // Simuler l'envoi (dans une vraie application, on intégrerait un service d'envoi d'emails/SMS)
        campagne.setNombreEnvoyes(campagne.getNombreCibles());
        campagne.setDateEnvoi(LocalDateTime.now());
        
        campagne = campagneRepository.save(campagne);
        log.info("Campagne envoyée à {} clients", campagne.getNombreEnvoyes());
        
        return convertirEnDTO(campagne);
    }

    @Override
    public CampagneMarketingDTO enregistrerOuverture(Long id, Long clientId) {
        CampagneMarketing campagne = campagneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Campagne non trouvée avec l'ID: " + id));
        
        campagne.setNombreOuvertures(campagne.getNombreOuvertures() + 1);
        campagne = campagneRepository.save(campagne);
        
        return convertirEnDTO(campagne);
    }

    @Override
    public CampagneMarketingDTO enregistrerClic(Long id, Long clientId) {
        CampagneMarketing campagne = campagneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Campagne non trouvée avec l'ID: " + id));
        
        campagne.setNombreClics(campagne.getNombreClics() + 1);
        campagne = campagneRepository.save(campagne);
        
        return convertirEnDTO(campagne);
    }

    @Override
    public CampagneMarketingDTO enregistrerConversion(Long id, Long clientId) {
        CampagneMarketing campagne = campagneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Campagne non trouvée avec l'ID: " + id));
        
        campagne.setNombreConversions(campagne.getNombreConversions() + 1);
        campagne = campagneRepository.save(campagne);
        
        return convertirEnDTO(campagne);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CampagneMarketingDTO> getCampagnesByStatut(StatutCampagne statut) {
        return campagneRepository.findByStatut(statut).stream()
            .map(this::convertirEnDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CampagneMarketingDTO> getCampagnesByType(TypeCampagne type) {
        return campagneRepository.findByType(type).stream()
            .map(this::convertirEnDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CampagneMarketingDTO> getCampagnesActives() {
        return campagneRepository.findCampagnesActives().stream()
            .map(this::convertirEnDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CampagneMarketingDTO> getCampagnesEntreDates(LocalDate dateDebut, LocalDate dateFin) {
        return campagneRepository.findCampagnesEntreDates(dateDebut, dateFin).stream()
            .map(this::convertirEnDTO)
            .collect(Collectors.toList());
    }

    @Override
    public void verifierCampagnesADemarrer() {
        log.info("Vérification des campagnes à démarrer");
        
        List<CampagneMarketing> campagnes = campagneRepository.findCampagnesADemarrer(LocalDate.now());
        
        for (CampagneMarketing campagne : campagnes) {
            try {
                lancerCampagne(campagne.getId());
                log.info("Campagne démarrée automatiquement: {}", campagne.getCode());
            } catch (Exception e) {
                log.error("Erreur lors du démarrage automatique de la campagne {}: {}", 
                         campagne.getCode(), e.getMessage());
            }
        }
    }

    @Override
    public void verifierCampagnesATerminer() {
        log.info("Vérification des campagnes à terminer");
        
        List<CampagneMarketing> campagnes = campagneRepository.findCampagnesATerminer(LocalDate.now());
        
        for (CampagneMarketing campagne : campagnes) {
            try {
                terminerCampagne(campagne.getId());
                log.info("Campagne terminée automatiquement: {}", campagne.getCode());
            } catch (Exception e) {
                log.error("Erreur lors de la terminaison automatique de la campagne {}: {}", 
                         campagne.getCode(), e.getMessage());
            }
        }
    }

    // Méthodes utilitaires privées

    private String genererCodeCampagne() {
        return "CAMP" + System.currentTimeMillis();
    }

    private CampagneMarketingDTO convertirEnDTO(CampagneMarketing campagne) {
        return CampagneMarketingDTO.builder()
            .id(campagne.getId())
            .code(campagne.getCode())
            .nom(campagne.getNom())
            .description(campagne.getDescription())
            .type(campagne.getType())
            .statut(campagne.getStatut())
            .dateDebut(campagne.getDateDebut())
            .dateFin(campagne.getDateFin())
            .segmentsCibles(campagne.getSegmentsCibles())
            .niveauxCibles(campagne.getNiveauxCibles())
            .sujet(campagne.getSujet())
            .message(campagne.getMessage())
            .urlImage(campagne.getUrlImage())
            .typeOffre(campagne.getTypeOffre())
            .valeurOffre(campagne.getValeurOffre())
            .codePromo(campagne.getCodePromo())
            .nombreCibles(campagne.getNombreCibles())
            .nombreEnvoyes(campagne.getNombreEnvoyes())
            .nombreOuvertures(campagne.getNombreOuvertures())
            .nombreClics(campagne.getNombreClics())
            .nombreConversions(campagne.getNombreConversions())
            .chiffreAffaireGenere(campagne.getChiffreAffaireGenere())
            .tauxOuverture(campagne.getTauxOuverture())
            .tauxClics(campagne.getTauxClics())
            .tauxConversion(campagne.getTauxConversion())
            .build();
    }
}
