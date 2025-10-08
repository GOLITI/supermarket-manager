package comcom.supermarket.manager.repository;

import comcom.supermarket.manager.model.caisse.LigneTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LigneTransactionRepository extends JpaRepository<LigneTransaction, Long> {
    List<LigneTransaction> findByTransactionId(Long transactionId);

    @Query("SELECT lt.produit.id, lt.produit.nom, lt.produit.codeBarre, SUM(lt.quantite), SUM(lt.quantite * lt.prixUnitaire), COUNT(DISTINCT lt.transaction) FROM LigneTransaction lt WHERE lt.transaction.dateHeure >= :debut AND lt.transaction.dateHeure <= :fin AND lt.transaction.statut = 'COMPLETEE' GROUP BY lt.produit.id, lt.produit.nom, lt.produit.codeBarre ORDER BY SUM(lt.quantite) DESC")
    List<Object[]> produitsLesPlusVendus(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    @Query("SELECT SUM(lt.quantite) FROM LigneTransaction lt WHERE lt.transaction.dateHeure >= :debut AND lt.transaction.dateHeure <= :fin AND lt.transaction.statut = 'COMPLETEE'")
    Long totalArticlesVendus(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);
}
