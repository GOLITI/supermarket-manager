package comcom.supermarket.manager.service;

import comcom.supermarket.manager.model.dto.CampagneMarketingDTO;
import comcom.supermarket.manager.model.dto.CampagneMarketingRequest;
import comcom.supermarket.manager.model.client.StatutCampagne;
import comcom.supermarket.manager.model.client.TypeCampagne;

import java.time.LocalDate;
import java.util.List;

public interface CampagneMarketingService {
    
    // CRUD de base
    CampagneMarketingDTO creerCampagne(CampagneMarketingRequest request);
    CampagneMarketingDTO modifierCampagne(Long id, CampagneMarketingRequest request);
    CampagneMarketingDTO getCampagneById(Long id);
    void supprimerCampagne(Long id);
    List<CampagneMarketingDTO> getAllCampagnes();
    
    // Gestion du cycle de vie
    CampagneMarketingDTO programmerCampagne(Long id, LocalDate dateEnvoi);
    CampagneMarketingDTO lancerCampagne(Long id);
    CampagneMarketingDTO terminerCampagne(Long id);
    CampagneMarketingDTO annulerCampagne(Long id);
    
    // Envoi et ciblage
    CampagneMarketingDTO calculerCibles(Long id);
    CampagneMarketingDTO envoyerCampagne(Long id);
    
    // Tracking et statistiques
    CampagneMarketingDTO enregistrerOuverture(Long id, Long clientId);
    CampagneMarketingDTO enregistrerClic(Long id, Long clientId);
    CampagneMarketingDTO enregistrerConversion(Long id, Long clientId);
    
    // Recherche et filtrage
    List<CampagneMarketingDTO> getCampagnesByStatut(StatutCampagne statut);
    List<CampagneMarketingDTO> getCampagnesByType(TypeCampagne type);
    List<CampagneMarketingDTO> getCampagnesActives();
    List<CampagneMarketingDTO> getCampagnesEntreDates(LocalDate dateDebut, LocalDate dateFin);
    
    // Automatisation
    void verifierCampagnesADemarrer();
    void verifierCampagnesATerminer();
}
