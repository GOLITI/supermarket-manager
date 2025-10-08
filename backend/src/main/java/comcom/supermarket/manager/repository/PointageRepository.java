package comcom.supermarket.manager.repository;

import comcom.supermarket.manager.model.rh.Pointage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PointageRepository extends JpaRepository<Pointage, Long> {

    List<Pointage> findByEmployeId(Long employeId);

    @Query("SELECT p FROM Pointage p WHERE p.employe.id = :employeId " +
           "AND DATE(p.heureEntree) = :date")
    List<Pointage> findByEmployeAndDate(@Param("employeId") Long employeId,
                                        @Param("date") LocalDate date);

    @Query("SELECT p FROM Pointage p WHERE p.employe.id = :employeId " +
           "AND p.heureSortie IS NULL")
    Optional<Pointage> findPointageEnCours(@Param("employeId") Long employeId);

    @Query("SELECT p FROM Pointage p WHERE p.employe.id = :employeId " +
           "AND p.heureEntree >= :debut AND p.heureEntree <= :fin")
    List<Pointage> findByEmployeAndPeriode(@Param("employeId") Long employeId,
                                           @Param("debut") LocalDateTime debut,
                                           @Param("fin") LocalDateTime fin);

    @Query("SELECT SUM(p.heuresTravaillees) FROM Pointage p " +
           "WHERE p.employe.id = :employeId " +
           "AND p.heureEntree >= :debut AND p.heureEntree <= :fin " +
           "AND p.valide = true")
    Double sumHeuresTravailleesByEmployeAndPeriode(@Param("employeId") Long employeId,
                                                    @Param("debut") LocalDateTime debut,
                                                    @Param("fin") LocalDateTime fin);

    @Query("SELECT SUM(p.heuresSupplementaires) FROM Pointage p " +
           "WHERE p.employe.id = :employeId " +
           "AND p.heureEntree >= :debut AND p.heureEntree <= :fin " +
           "AND p.valide = true")
    Double sumHeuresSupplementairesByEmployeAndPeriode(@Param("employeId") Long employeId,
                                                        @Param("debut") LocalDateTime debut,
                                                        @Param("fin") LocalDateTime fin);
}

