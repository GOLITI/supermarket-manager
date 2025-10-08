package comcom.supermarket.manager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeurePointeDTO {
    private String jourSemaine;
    private String plageHoraire;
    private Integer nombreClients;
    private Integer nombreTransactions;
    private Double intensite; // 0-100
}

