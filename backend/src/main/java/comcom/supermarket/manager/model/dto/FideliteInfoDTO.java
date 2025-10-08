package comcom.supermarket.manager.model.dto;

import comcom.supermarket.manager.model.client.NiveauFidelite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FideliteInfoDTO {
    private String numeroCarteFidelite;
    private Integer pointsFidelite;
    private NiveauFidelite niveauActuel;
    private NiveauFidelite procheNiveau;
    private Integer pointsManquants;
    private BigDecimal reductionDisponible;
    private BigDecimal reductionMaximale;
    private BigDecimal multiplicateurPoints;
    private String messageNiveau;
}

