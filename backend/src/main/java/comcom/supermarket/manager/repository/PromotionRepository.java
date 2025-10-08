package comcom.supermarket.manager.repository;

import comcom.supermarket.manager.model.caisse.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> findByCode(String code);

    @Query("SELECT p FROM Promotion p WHERE p.active = true AND p.dateDebut <= :date AND p.dateFin >= :date AND (p.utilisationsMax IS NULL OR p.utilisationsActuelles < p.utilisationsMax)")
    List<Promotion> findPromotionsValides(@Param("date") LocalDate date);

    @Query("SELECT p FROM Promotion p WHERE p.produit.id = :produitId AND p.active = true AND p.dateDebut <= :date AND p.dateFin >= :date")
    List<Promotion> findPromotionsValidesParProduit(@Param("produitId") Long produitId, @Param("date") LocalDate date);

    List<Promotion> findByActiveTrue();

    @Query("SELECT p FROM Promotion p WHERE p.dateFin < :date")
    List<Promotion> findPromotionsExpirees(@Param("date") LocalDate date);
}

