package comcom.supermarket.manager.model.dto;

import comcom.supermarket.manager.model.rh.TypeDocument;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDTO {
    private Long id;
    private Long employeId;
    private String employeNom;
    private String titre;
    private TypeDocument type;
    private String typeLibelle;
    private String cheminFichier;
    private String nomFichier;
    private Long tailleFichier;
    private String typeMime;
    private String description;
    private LocalDateTime dateAjout;
    private Boolean confidentiel;
    private LocalDateTime dateExpiration;
    private Boolean expire;
}

