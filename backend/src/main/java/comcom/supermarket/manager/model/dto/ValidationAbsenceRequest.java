package comcom.supermarket.manager.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationAbsenceRequest {
    
    @NotNull(message = "L'ID du validateur est obligatoire")
    private Long validateurId;
    
    @NotNull(message = "La décision est obligatoire")
    private Boolean approuvee;
    
    @Size(max = 500, message = "Le commentaire ne peut pas dépasser 500 caractères")
    private String commentaire;
}

