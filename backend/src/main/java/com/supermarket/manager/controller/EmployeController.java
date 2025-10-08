package com.supermarket.manager.controller;

import com.supermarket.manager.model.dto.EmployeDTO;
import com.supermarket.manager.model.rh.Employe;
import com.supermarket.manager.service.EmployeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmployeController {

    private final EmployeService employeService;

    @GetMapping
    public ResponseEntity<List<EmployeDTO>> getAllEmployes() {
        return ResponseEntity.ok(employeService.getAllEmployes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeDTO> getEmployeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeService.getEmployeById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeDTO> createEmploye(@RequestBody EmployeDTO employeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeService.createEmploye(employeDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeDTO> updateEmploye(@PathVariable Long id, @RequestBody EmployeDTO employeDTO) {
        return ResponseEntity.ok(employeService.updateEmploye(id, employeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        employeService.deleteEmploye(id);
        return ResponseEntity.noContent().build();
    }
}

