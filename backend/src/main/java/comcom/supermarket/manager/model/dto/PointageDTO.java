package comcom.supermarket.manager.model.dto;

import comcom.supermarket.manager.model.rh.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointageDTO {
    private Long id;
    private Long employeId;
    private String employeNom;
    private LocalDateTime heureEntree;
    private LocalDateTime heureSortie;
    private TypePointage type;
    private String typeLibelle;
    private String notes;
    private Double heuresTravaillees;
    private Double heuresSupplementaires;
    private Boolean valide;
    private Boolean enCours;
}

