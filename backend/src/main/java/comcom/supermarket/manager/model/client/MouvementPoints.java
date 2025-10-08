package comcom.supermarket.manager.model.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mouvements_points")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MouvementPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMouvement type;

    @Column(nullable = false)
    private Integer points;

    @Column(nullable = false)
    private LocalDateTime dateMouvement;

    private String reference; // Numéro de ticket ou autre référence

    private String description;

    @Column(nullable = false)
    private Integer soldeAvant;

    @Column(nullable = false)
    private Integer soldeApres;
}

