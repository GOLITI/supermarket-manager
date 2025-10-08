package comcom.supermarket.manager.model.dto;

import comcom.supermarket.manager.model.client.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampagneMarketingDTO {
    private Long id;
    private String code;
    private String nom;
    private String description;
    private TypeCampagne type;
    private StatutCampagne statut;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private List<SegmentClient> segmentsCibles;
    private List<NiveauFidelite> niveauxCibles;
    private String sujet;
    private String message;
    private String urlImage;
    private TypeOffre typeOffre;
    private BigDecimal valeurOffre;
    private String codePromo;
    private Integer nombreCibles;
    private Integer nombreEnvoyes;
    private Integer nombreOuvertures;
    private Integer nombreClics;
    private Integer nombreConversions;
    private BigDecimal chiffreAffaireGenere;
    private Double tauxOuverture;
    private Double tauxClics;
    private Double tauxConversion;
}

