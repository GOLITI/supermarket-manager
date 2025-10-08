package comcom.supermarket.manager.repository;

import comcom.supermarket.manager.model.reporting.StatistiqueFrequentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StatistiqueFrequentationRepository extends JpaRepository<StatistiqueFrequentation, Long> {

    List<StatistiqueFrequentation> findByDateStatBetween(LocalDate debut, LocalDate fin);

    @Query("SELECT s FROM StatistiqueFrequentation s WHERE s.estHeurePointe = true " +
           "AND s.dateStat BETWEEN :debut AND :fin ORDER BY s.nombreClients DESC")
    List<StatistiqueFrequentation> findHeuresPointe(@Param("debut") LocalDate debut,
                                                      @Param("fin") LocalDate fin);

    @Query("SELECT s.jourSemaine, AVG(s.nombreClients), AVG(s.nombreTransactions) " +
           "FROM StatistiqueFrequentation s WHERE s.dateStat BETWEEN :debut AND :fin " +
           "GROUP BY s.jourSemaine ORDER BY AVG(s.nombreClients) DESC")
    List<Object[]> findFrequentationByJourSemaine(@Param("debut") LocalDate debut,
                                                    @Param("fin") LocalDate fin);

    @Query("SELECT s.heureDebut, SUM(s.nombreClients), SUM(s.nombreTransactions) " +
           "FROM StatistiqueFrequentation s WHERE s.dateStat BETWEEN :debut AND :fin " +
           "GROUP BY s.heureDebut ORDER BY SUM(s.nombreClients) DESC")
    List<Object[]> findFrequentationByPlageHoraire(@Param("debut") LocalDate debut,
                                                     @Param("fin") LocalDate fin);
}

