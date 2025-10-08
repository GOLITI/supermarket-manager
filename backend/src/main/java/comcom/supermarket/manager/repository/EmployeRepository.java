package comcom.supermarket.manager.repository;

import comcom.supermarket.manager.model.rh.Employe;
import comcom.supermarket.manager.model.rh.PosteEmploye;
import comcom.supermarket.manager.model.rh.StatutEmploye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {

    Optional<Employe> findByMatricule(String matricule);

    Optional<Employe> findByEmail(String email);

    List<Employe> findByStatut(StatutEmploye statut);

    List<Employe> findByPoste(PosteEmploye poste);

    List<Employe> findByStatutAndPoste(StatutEmploye statut, PosteEmploye poste);

    @Query("SELECT e FROM Employe e WHERE LOWER(e.nom) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(e.prenom) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(e.matricule) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Employe> searchEmployes(@Param("search") String search);

    long countByStatut(StatutEmploye statut);

    long countByPoste(PosteEmploye poste);
}

