package comcom.supermarket.manager.model.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationDTO {
    private Long id;
    private Long employeId;
    private String employeNom;
    private Long evaluateurId;
    private String evaluateurNom;
    private LocalDate dateEvaluation;
    private String periodeEvaluee;
    private Integer noteGlobale;
    private Integer notePonctualite;
    private Integer noteQualiteTravail;
    private Integer noteProductivite;
    private Integer noteRelationClient;
    private Integer noteEspritEquipe;
    private Double noteMoyenne;
    private String pointsForts;
    private String pointsAmeliorer;
    private String objectifs;
    private String commentaires;
    private Boolean valide;
}

