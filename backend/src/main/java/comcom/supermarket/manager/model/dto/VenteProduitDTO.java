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
public class VenteProduitDTO {
    private Long produitId;
    private String nomProduit;
    private String categorie;
    private Integer quantiteVendue;
    private BigDecimal montantVente;
    private BigDecimal margeBeneficiaire;
    private BigDecimal pourcentageMarge;
    private BigDecimal evolutionPourcentage;
    private String periode;
}

