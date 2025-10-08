package comcom.supermarket.manager.model.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockAlertDTO {

    private Long stockId;
    private Long produitId;
    private String codeProduit;
    private String nomProduit;
    private String categorie;
    private Long entrepotId;
    private String nomEntrepot;
    private Integer quantiteActuelle;
    private Integer seuilReapprovisionnement;
    private Integer quantiteRecommandee;
    private BigDecimal prixAchat;
    private Long fournisseurId;
    private String nomFournisseur;
    private Integer delaiLivraisonJours;

    public String getNiveauAlerte() {
        if (quantiteActuelle == 0) {
            return "CRITIQUE";
        } else if (quantiteActuelle <= seuilReapprovisionnement / 2) {
            return "CRITIQUE";
        } else if (quantiteActuelle <= seuilReapprovisionnement) {
            return "MOYEN";
        }
        return "BAS";
    }
}
