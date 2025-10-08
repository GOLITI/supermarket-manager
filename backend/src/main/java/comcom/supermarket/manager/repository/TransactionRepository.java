package comcom.supermarket.manager.repository;

import comcom.supermarket.manager.model.caisse.StatutTransaction;
import comcom.supermarket.manager.model.caisse.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByNumeroTransaction(String numeroTransaction);
    List<Transaction> findByStatut(StatutTransaction statut);
    List<Transaction> findByDateHeureBetween(LocalDateTime debut, LocalDateTime fin);
    List<Transaction> findByCarteFideliteId(Long carteFideliteId);

    @Query("SELECT t FROM Transaction t WHERE t.dateHeure >= :debut AND t.dateHeure <= :fin AND t.statut = :statut ORDER BY t.dateHeure DESC")
    List<Transaction> findTransactionsPeriode(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin, @Param("statut") StatutTransaction statut);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.dateHeure >= :debut AND t.dateHeure <= :fin AND t.statut = 'COMPLETEE'")
    Long countTransactionsCompletees(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    @Query("SELECT SUM(t.montantNet) FROM Transaction t WHERE t.dateHeure >= :debut AND t.dateHeure <= :fin AND t.statut = 'COMPLETEE'")
    BigDecimal sumMontantNetPeriode(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    @Query("SELECT SUM(t.montantRemises) FROM Transaction t WHERE t.dateHeure >= :debut AND t.dateHeure <= :fin AND t.statut = 'COMPLETEE'")
    BigDecimal sumMontantRemisesPeriode(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(DISTINCT t.carteFidelite) FROM Transaction t WHERE t.dateHeure >= :debut AND t.dateHeure <= :fin AND t.statut = 'COMPLETEE' AND t.carteFidelite IS NOT NULL")
    Long countCartesUtilisees(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);
}

