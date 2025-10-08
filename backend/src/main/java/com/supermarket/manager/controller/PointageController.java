package com.supermarket.manager.controller;

import com.supermarket.manager.model.dto.PointageDTO;
import com.supermarket.manager.service.PointageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pointages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PointageController {

    private final PointageService pointageService;

    @PostMapping("/entree")
    public ResponseEntity<PointageDTO> enregistrerEntree(@RequestParam Long employeId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pointageService.pointer(employeId));
    }

    @PostMapping("/sortie")
    public ResponseEntity<PointageDTO> enregistrerSortie(@RequestParam Long employeId) {
        return ResponseEntity.ok(pointageService.pointer(employeId));
    }

    @GetMapping("/employe/{employeId}")
    public ResponseEntity<List<PointageDTO>> getPointagesByEmploye(
            @PathVariable Long employeId,
            @RequestParam LocalDate dateDebut,
            @RequestParam LocalDate dateFin) {
        return ResponseEntity.ok(pointageService.obtenirPointagesParPeriode(employeId, dateDebut, dateFin));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<PointageDTO>> getPointagesByDate(
            @PathVariable LocalDate date,
            @RequestParam Long employeId) {
        return ResponseEntity.ok(pointageService.obtenirPointagesEmployeParDate(employeId, date));
    }
}
