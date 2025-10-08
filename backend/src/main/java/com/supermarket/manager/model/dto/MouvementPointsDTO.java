package com.supermarket.manager.model.dto;

import com.supermarket.manager.model.client.TypeMouvement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MouvementPointsDTO {
    private Long id;
    private TypeMouvement type;
    private Integer points;
    private LocalDateTime dateMouvement;
    private String reference;
    private String description;
    private Integer soldeAvant;
    private Integer soldeApres;
}

