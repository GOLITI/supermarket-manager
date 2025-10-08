package comcom.supermarket.manager.model.dto;

import comcom.supermarket.manager.model.rh.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbsenceDTO {
    private Long id;
    private Long employeId;
    private String employeNom;
    private TypeAbsence type;
    private String typeLibelle;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long nombreJours;
    private String motif;
    private StatutDemande statut;
    private String statutLibelle;
    private LocalDate dateValidation;
    private Long validateurId;
    private String validateurNom;
    private String commentaireValidation;
    private String documentJustificatif;
}

