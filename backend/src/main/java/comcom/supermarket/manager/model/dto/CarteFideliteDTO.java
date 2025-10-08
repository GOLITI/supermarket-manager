package comcom.supermarket.manager.model.dto;

import comcom.supermarket.manager.model.caisse.NiveauFidelite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarteFideliteDTO {
    private Long id;
    private String numeroCarte;
    private String nomClient;
    private String prenomClient;
    private String email;
    private String telephone;
    private LocalDate dateInscription;
    private BigDecimal pointsFidelite;
    private BigDecimal totalAchats;
    private NiveauFidelite niveau;
    private Boolean active;
    private LocalDate dateExpiration;
}

