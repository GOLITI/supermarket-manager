package comcom.supermarket.manager.controller;
import comcom.supermarket.manager.model.dto.CampagneMarketingDTO;
import comcom.supermarket.manager.model.dto.CampagneMarketingRequest;
import comcom.supermarket.manager.model.client.StatutCampagne;
import comcom.supermarket.manager.service.CampagneMarketingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/campagnes-marketing")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CampagneMarketingController {
    private final CampagneMarketingService campagneService;
    @PostMapping
    public ResponseEntity<CampagneMarketingDTO> creerCampagne(@Valid @RequestBody CampagneMarketingRequest request) {
        CampagneMarketingDTO campagne = campagneService.creerCampagne(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(campagne);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CampagneMarketingDTO> getCampagneById(@PathVariable Long id) {
        CampagneMarketingDTO campagne = campagneService.getCampagneById(id);
        return ResponseEntity.ok(campagne);
    }
    @GetMapping
    public ResponseEntity<List<CampagneMarketingDTO>> getAllCampagnes() {
        List<CampagneMarketingDTO> campagnes = campagneService.getAllCampagnes();
        return ResponseEntity.ok(campagnes);
    }
    @PostMapping("/{id}/lancer")
    public ResponseEntity<CampagneMarketingDTO> lancerCampagne(@PathVariable Long id) {
        CampagneMarketingDTO campagne = campagneService.lancerCampagne(id);
        return ResponseEntity.ok(campagne);
    }
    @GetMapping("/actives")
    public ResponseEntity<List<CampagneMarketingDTO>> getCampagnesActives() {
        List<CampagneMarketingDTO> campagnes = campagneService.getCampagnesActives();
        return ResponseEntity.ok(campagnes);
    }
}
