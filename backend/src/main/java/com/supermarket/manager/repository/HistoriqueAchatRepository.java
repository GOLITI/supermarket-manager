package com.supermarket.manager.repository;

import com.supermarket.manager.model.client.HistoriqueAchat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoriqueAchatRepository extends JpaRepository<HistoriqueAchat, Long> {

    Optional<HistoriqueAchat> findByNumeroTicket(String numeroTicket);
    
    List<HistoriqueAchat> findByClientIdOrderByDateAchatDesc(Long clientId);
    
    List<HistoriqueAchat> findByClientIdAndDateAchatBetween(
        Long clientId, 
        LocalDateTime dateDebut, 
        LocalDateTime dateFin
    );
    
    @Query("SELECT h FROM HistoriqueAchat h WHERE h.client.id = :clientId " +
           "ORDER BY h.dateAchat DESC")
    List<HistoriqueAchat> findDerniersAchatsClient(
        @Param("clientId") Long clientId,
        org.springframework.data.domain.Pageable pageable
    );
    
    @Query("SELECT SUM(h.montantTotal) FROM HistoriqueAchat h WHERE h.client.id = :clientId")
    BigDecimal calculerTotalAchatsClient(@Param("clientId") Long clientId);
    
    @Query("SELECT COUNT(h) FROM HistoriqueAchat h WHERE h.client.id = :clientId")
    Long compterAchatsClient(@Param("clientId") Long clientId);
    
    @Query("SELECT AVG(h.montantTotal) FROM HistoriqueAchat h WHERE h.client.id = :clientId")
    BigDecimal calculerPanierMoyenClient(@Param("clientId") Long clientId);
    
    @Query("SELECT h FROM HistoriqueAchat h WHERE h.dateAchat BETWEEN :dateDebut AND :dateFin")
    List<HistoriqueAchat> findAchatsEntreDates(
        @Param("dateDebut") LocalDateTime dateDebut,
        @Param("dateFin") LocalDateTime dateFin
    );
}

