package com.supermarket.manager.repository;

import com.supermarket.manager.model.rh.Document;
import com.supermarket.manager.model.rh.TypeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByEmployeId(Long employeId);

    List<Document> findByEmployeIdAndType(Long employeId, TypeDocument type);

    @Query("SELECT d FROM Document d WHERE d.employe.id = :employeId " +
           "AND d.confidentiel = false")
    List<Document> findDocumentsNonConfidentiels(@Param("employeId") Long employeId);

    @Query("SELECT d FROM Document d WHERE d.dateExpiration IS NOT NULL " +
           "AND d.dateExpiration < :date")
    List<Document> findDocumentsExpires(@Param("date") LocalDateTime date);

    @Query("SELECT d FROM Document d WHERE d.dateExpiration IS NOT NULL " +
           "AND d.dateExpiration BETWEEN :debut AND :fin")
    List<Document> findDocumentsExpirantBientot(@Param("debut") LocalDateTime debut,
                                                 @Param("fin") LocalDateTime fin);
}

