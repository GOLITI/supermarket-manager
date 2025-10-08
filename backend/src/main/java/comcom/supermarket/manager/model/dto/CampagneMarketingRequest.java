package comcom.supermarket.manager.model.dto;

import comcom.supermarket.manager.model.client.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampagneMarketingRequest {
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String description;

    @NotNull(message = "Le type de campagne est obligatoire")
    private TypeCampagne type;

    @NotNull(message = "La date de début est obligatoire")
    @FutureOrPresent(message = "La date de début doit être aujourd'hui ou dans le futur")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    @Future(message = "La date de fin doit être dans le futur")
    private LocalDate dateFin;

    private List<SegmentClient> segmentsCibles = new ArrayList<>();
    
    private List<NiveauFidelite> niveauxCibles = new ArrayList<>();

    @NotBlank(message = "Le sujet est obligatoire")
    private String sujet;

    @NotBlank(message = "Le message est obligatoire")
    private String message;

    private String urlImage;

    private TypeOffre typeOffre;
    
    private BigDecimal valeurOffre;
    
    private String codePromo;
}

