package comcom.supermarket.manager.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RapportRHDTO {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer nombreEmployesActifs;
    private Integer nombreAbsences;
    private Integer nombreCongesEnCours;
    private Double tauxAbsenteisme;
    private Double totalHeuresTravaillees;
    private Double totalHeuresSupplementaires;
    private Map<String, Integer> absencesParType;
    private Map<String, Integer> employesParPoste;
    private Map<String, Double> heuresParEmploye;
}

