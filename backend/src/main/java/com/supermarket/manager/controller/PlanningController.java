package com.supermarket.manager.controller;

import com.supermarket.manager.model.dto.PlanningDTO;
import com.supermarket.manager.service.PlanningService;
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
@RequestMapping("/api/plannings")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PlanningController {
    
    private final PlanningService planningService;
    
    @PostMapping
    public ResponseEntity<PlanningDTO> creerPlanning(@Valid @RequestBody PlanningDTO planningDTO) {
        log.info("POST /api/plannings - Création d'un planning");
        return ResponseEntity.status(HttpStatus.CREATED).body(planningService.creerPlanning(planningDTO));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PlanningDTO> obtenirPlanning(@PathVariable Long id) {
        return ResponseEntity.ok(planningService.obtenirPlanning(id));
    }
    
    @GetMapping("/employe/{employeId}")
    public ResponseEntity<List<PlanningDTO>> obtenirPlanningsEmploye(@PathVariable Long employeId) {
        return ResponseEntity.ok(planningService.obtenirPlanningsEmploye(employeId));
    }
    
    @GetMapping("/date/{date}")
    public ResponseEntity<List<PlanningDTO>> obtenirPlanningsParDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(planningService.obtenirPlanningsParDate(date));
    }
    
    @GetMapping("/periode")
    public ResponseEntity<List<PlanningDTO>> obtenirPlanningsParPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(planningService.obtenirPlanningsParPeriode(debut, fin));
    }
    
    @GetMapping("/employe/{employeId}/periode")
    public ResponseEntity<List<PlanningDTO>> obtenirPlanningsEmployePeriode(
            @PathVariable Long employeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(planningService.obtenirPlanningsEmployePeriode(employeId, debut, fin));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PlanningDTO> modifierPlanning(@PathVariable Long id,
                                                        @Valid @RequestBody PlanningDTO planningDTO) {
        return ResponseEntity.ok(planningService.modifierPlanning(id, planningDTO));
    }
    
    @PatchMapping("/{id}/valider")
    public ResponseEntity<PlanningDTO> validerPlanning(@PathVariable Long id) {
        return ResponseEntity.ok(planningService.validerPlanning(id));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerPlanning(@PathVariable Long id) {
        planningService.supprimerPlanning(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/generer-hebdomadaire")
    public ResponseEntity<List<PlanningDTO>> genererPlanningHebdomadaire(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut) {
        return ResponseEntity.ok(planningService.genererPlanningHebdomadaire(dateDebut));
    }
}

