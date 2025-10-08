package comcom.supermarket.manager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneTransactionDTO {
    private Long id;
    private Long produitId;
    private String nomProduit;
    private String codeBarre;
    private Integer quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal remiseLigne;
    private Long promotionId;
    private BigDecimal montantTotal;
}

