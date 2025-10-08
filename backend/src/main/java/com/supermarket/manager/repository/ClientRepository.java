package com.supermarket.manager.repository;
import com.supermarket.manager.model.client.Client;
import com.supermarket.manager.model.client.SegmentClient;
import com.supermarket.manager.model.client.StatutClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByNumeroClient(String numeroClient);
    Optional<Client> findByNumeroCarteFidelite(String numeroCarteFidelite);
    Optional<Client> findByEmail(String email);
    Optional<Client> findByTelephone(String telephone);
    List<Client> findByStatut(StatutClient statut);
    List<Client> findBySegment(SegmentClient segment);
    @Query("SELECT c FROM Client c WHERE c.dernierAchat < :date AND c.statut = 'ACTIF'")
    List<Client> findClientsInactifsDernierAchatAvant(@Param("date") LocalDate date);
    @Query("SELECT c FROM Client c WHERE " +
           "(LOWER(c.nom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.prenom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "c.telephone LIKE CONCAT('%', :search, '%'))")
    List<Client> rechercherClients(@Param("search") String search);
    @Query("SELECT COUNT(c) FROM Client c WHERE c.statut = 'ACTIF'")
    Long countClientsActifs();
}
