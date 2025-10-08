package comcom.supermarket.manager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionPersonnaliseeDTO {
    private Long clientId;
    private String nomClient;
    private String numeroCarteFidelite;
    private List<OffrePersonnalisee> offres;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OffrePersonnalisee {
        private Long produitId;
        private String nomProduit;
        private String categorie;
        private String typePromotion;
        private String description;
        private String raisonRecommandation;
        private Integer priorite;
    }
}
