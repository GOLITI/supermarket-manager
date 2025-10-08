package comcom.supermarket.manager.repository;

import comcom.supermarket.manager.model.client.CampagneMarketing;
import comcom.supermarket.manager.model.client.StatutCampagne;
import comcom.supermarket.manager.model.client.TypeCampagne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CampagneMarketingRepository extends JpaRepository<CampagneMarketing, Long> {

    Optional<CampagneMarketing> findByCode(String code);
    
    List<CampagneMarketing> findByStatut(StatutCampagne statut);
    
    List<CampagneMarketing> findByType(TypeCampagne type);
    
    @Query("SELECT c FROM CampagneMarketing c WHERE c.statut = 'EN_COURS' " +
           "AND CURRENT_DATE BETWEEN c.dateDebut AND c.dateFin")
    List<CampagneMarketing> findCampagnesActives();
    
    @Query("SELECT c FROM CampagneMarketing c WHERE c.statut = 'PROGRAMMEE' " +
           "AND c.dateDebut <= :date")
    List<CampagneMarketing> findCampagnesADemarrer(@Param("date") LocalDate date);
    
    @Query("SELECT c FROM CampagneMarketing c WHERE c.statut = 'EN_COURS' " +
           "AND c.dateFin < :date")
    List<CampagneMarketing> findCampagnesATerminer(@Param("date") LocalDate date);
    
    @Query("SELECT c FROM CampagneMarketing c WHERE c.dateDebut BETWEEN :dateDebut AND :dateFin " +
           "ORDER BY c.dateDebut DESC")
    List<CampagneMarketing> findCampagnesEntreDates(
        @Param("dateDebut") LocalDate dateDebut,
        @Param("dateFin") LocalDate dateFin
    );
    
    @Query("SELECT c FROM CampagneMarketing c ORDER BY c.dateCreation DESC")
    List<CampagneMarketing> findAllOrderByDateCreationDesc();
}
