package comcom.supermarket.manager.model.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDTO {

    private Long id;
    private Long produitId;
    private String nomProduit;
    private Long entrepotId;
    private String nomEntrepot;
    private Integer quantite;
    private Integer seuilReapprovisionnement;
    private Integer quantiteMaximale;
    private Integer quantiteRecommandeeCommande;
    private LocalDate datePeremption;
    private Boolean alerteActive;
}

