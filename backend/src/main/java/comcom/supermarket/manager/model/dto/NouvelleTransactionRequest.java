package comcom.supermarket.manager.model.dto;

import comcom.supermarket.manager.model.caisse.MethodePaiement;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NouvelleTransactionRequest {
    @NotEmpty(message = "La transaction doit contenir au moins un article")
    private List<ArticleTransactionRequest> articles = new ArrayList<>();
    private String numeroCarteFidelite;
    private List<String> codesPromotion = new ArrayList<>();
    @NotNull(message = "La méthode de paiement est requise")
    private MethodePaiement methodePaiement;
    @NotNull(message = "Le montant payé est requis")
    @Positive(message = "Le montant payé doit être positif")
    private BigDecimal montantPaye;
    private String caissierId;
    private String remarques;
}

