package comcom.supermarket.manager.repository;

import comcom.supermarket.manager.model.caisse.CarteFidelite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CarteFideliteRepository extends JpaRepository<CarteFidelite, Long> {
    Optional<CarteFidelite> findByNumeroCarte(String numeroCarte);
    Optional<CarteFidelite> findByEmail(String email);
    boolean existsByNumeroCarte(String numeroCarte);
    boolean existsByEmail(String email);

    @Query("SELECT COUNT(c) FROM CarteFidelite c WHERE c.active = true")
    long countActiveCards();
}

