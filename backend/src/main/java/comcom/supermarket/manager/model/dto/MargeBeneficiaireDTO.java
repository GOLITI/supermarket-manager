package comcom.supermarket.manager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MargeBeneficiaireDTO {
    private String categorie;
    private BigDecimal chiffreAffaires;
    private BigDecimal coutTotal;
    private BigDecimal margeBrute;
    private BigDecimal pourcentageMarge;
    private Integer nombreProduits;
}

