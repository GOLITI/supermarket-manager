package comcom.supermarket.manager.model.dto;

import comcom.supermarket.manager.model.caisse.TypePromotion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDTO {
    private Long id;
    private String code;
    private String description;
    private TypePromotion type;
    private BigDecimal valeur;
    private BigDecimal montantMinimum;
    private Long produitId;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Boolean active;
    private Integer utilisationsMax;
    private Integer utilisationsActuelles;
}

