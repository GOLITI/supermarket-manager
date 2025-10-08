package com.supermarket.manager.repository;

import com.supermarket.manager.model.client.MouvementPoints;
import com.supermarket.manager.model.client.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MouvementPointsRepository extends JpaRepository<MouvementPoints, Long> {

    List<MouvementPoints> findByClientIdOrderByDateMouvementDesc(Long clientId);
    
    List<MouvementPoints> findByClientIdAndTypeOrderByDateMouvementDesc(Long clientId, TypeMouvement type);
    
    List<MouvementPoints> findByClientIdAndDateMouvementBetween(
        Long clientId,
        LocalDateTime dateDebut,
        LocalDateTime dateFin
    );
    
    @Query("SELECT SUM(m.points) FROM MouvementPoints m " +
           "WHERE m.client.id = :clientId AND m.type = :type")
    Integer sumPointsByClientAndType(
        @Param("clientId") Long clientId,
        @Param("type") TypeMouvement type
    );
    
    @Query("SELECT m FROM MouvementPoints m WHERE m.client.id = :clientId " +
           "ORDER BY m.dateMouvement DESC")
    List<MouvementPoints> findDerniersMouvementsClient(
        @Param("clientId") Long clientId,
        org.springframework.data.domain.Pageable pageable
    );
}

