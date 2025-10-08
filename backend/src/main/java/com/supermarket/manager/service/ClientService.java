package com.supermarket.manager.service;
import com.supermarket.manager.model.dto.*;
import com.supermarket.manager.model.client.SegmentClient;
import com.supermarket.manager.model.client.StatutClient;
import java.util.List;
public interface ClientService {
    ClientDTO creerClient(ClientRequest request);
    ClientDTO modifierClient(Long id, ClientRequest request);
    ClientDTO getClientById(Long id);
    ClientDTO getClientByNumeroCarteFidelite(String numeroCarteFidelite);
    void supprimerClient(Long id);
    List<ClientDTO> getAllClients();
    List<ClientDTO> rechercherClients(String search);
    List<ClientDTO> getClientsBySegment(SegmentClient segment);
    List<ClientDTO> getClientsByStatut(StatutClient statut);
    FideliteInfoDTO getFideliteInfo(Long clientId);
    FideliteInfoDTO getFideliteInfoByCarteFidelite(String numeroCarteFidelite);
    ClientDTO ajouterPoints(Long clientId, Integer points, String reference, String description);
    ClientDTO utiliserPoints(Long clientId, Integer points, String reference);
    List<MouvementPointsDTO> getHistoriquePoints(Long clientId);
    List<HistoriqueAchatDTO> getHistoriqueAchats(Long clientId);
    HistoriqueAchatDTO enregistrerAchat(Long clientId, HistoriqueAchatDTO achatDTO);
    PromotionPersonnaliseeDTO getPromotionsPersonnalisees(Long clientId);
    Long countClientsActifs();
    void mettreAJourSegments();
    List<ClientDTO> getClientsInactifs(int joursInactivite);
}
