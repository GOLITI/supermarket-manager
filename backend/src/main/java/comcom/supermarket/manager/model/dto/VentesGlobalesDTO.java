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
public class VentesGlobalesDTO {
    private BigDecimal chiffreAffaires;
    private BigDecimal margeTotale;
    private Integer nombreVentes;
    private BigDecimal panierMoyen;
    private BigDecimal evolutionPourcentage;
}

