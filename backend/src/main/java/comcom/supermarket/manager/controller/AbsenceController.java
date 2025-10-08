package comcom.supermarket.manager.controller;

import comcom.supermarket.manager.model.dto.AbsenceDTO;
import comcom.supermarket.manager.model.dto.DemandeAbsenceRequest;
import comcom.supermarket.manager.model.dto.ValidationAbsenceRequest;
import comcom.supermarket.manager.service.AbsenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/absences")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AbsenceController {
    
    private final AbsenceService absenceService;
    
    @PostMapping
    public ResponseEntity<AbsenceDTO> creerDemandeAbsence(@Valid @RequestBody DemandeAbsenceRequest request) {
        log.info("POST /api/absences - Création d'une demande d'absence");
        return ResponseEntity.status(HttpStatus.CREATED).body(absenceService.creerDemandeAbsence(request));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AbsenceDTO> obtenirAbsence(@PathVariable Long id) {
        return ResponseEntity.ok(absenceService.obtenirAbsence(id));
    }
    
    @GetMapping("/employe/{employeId}")
    public ResponseEntity<List<AbsenceDTO>> obtenirAbsencesEmploye(@PathVariable Long employeId) {
        return ResponseEntity.ok(absenceService.obtenirAbsencesEmploye(employeId));
    }
    
    @GetMapping("/employe/{employeId}/en-cours")
    public ResponseEntity<List<AbsenceDTO>> obtenirAbsencesEnCoursEmploye(@PathVariable Long employeId) {
        return ResponseEntity.ok(absenceService.obtenirAbsencesEnCoursEmploye(employeId));
    }
    
    @GetMapping("/en-attente")
    public ResponseEntity<List<AbsenceDTO>> obtenirAbsencesEnAttente() {
        return ResponseEntity.ok(absenceService.obtenirAbsencesEnAttente());
    }
    
    @GetMapping("/periode")
    public ResponseEntity<List<AbsenceDTO>> obtenirAbsencesParPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(absenceService.obtenirAbsencesParPeriode(debut, fin));
    }
    
    @PatchMapping("/{id}/valider")
    public ResponseEntity<AbsenceDTO> validerAbsence(@PathVariable Long id,
                                                      @Valid @RequestBody ValidationAbsenceRequest request) {
        return ResponseEntity.ok(absenceService.validerAbsence(id, request));
    }
    
    @PatchMapping("/{id}/annuler")
    public ResponseEntity<AbsenceDTO> annulerAbsence(@PathVariable Long id) {
        return ResponseEntity.ok(absenceService.annulerAbsence(id));
    }
}

