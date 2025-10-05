package comcom.supermarket.manager.repository;

import comcom.supermarket.manager.model.commande.Commande;
import comcom.supermarket.manager.model.commande.StatutCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    Optional<Commande> findByNumeroCommande(String numeroCommande);

    List<Commande> findByFournisseurId(Long fournisseurId);

    List<Commande> findByStatut(StatutCommande statut);

    List<Commande> findByDateCommandeBetween(LocalDate dateDebut, LocalDate dateFin);

    @Query("SELECT c FROM Commande c WHERE c.statut IN :statuts ORDER BY c.dateCommande DESC")
    List<Commande> findByStatutIn(@Param("statuts") List<StatutCommande> statuts);

    List<Commande> findByCommandeAutomatiqueTrue();
}

