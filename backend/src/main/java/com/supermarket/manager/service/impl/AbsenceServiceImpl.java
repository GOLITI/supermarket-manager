package com.supermarket.manager.service.impl;

import com.supermarket.manager.exception.BusinessException;
import com.supermarket.manager.exception.ResourceNotFoundException;
import com.supermarket.manager.model.dto.AbsenceDTO;
import com.supermarket.manager.model.rh.Absence;
import com.supermarket.manager.model.rh.Employe;
import com.supermarket.manager.model.rh.StatutDemande;
import com.supermarket.manager.repository.AbsenceRepository;
import com.supermarket.manager.repository.EmployeRepository;
import com.supermarket.manager.service.AbsenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final EmployeRepository employeRepository;

    @Override
    public AbsenceDTO creerDemandeAbsence(com.supermarket.manager.model.dto.DemandeAbsenceRequest request) {
        log.info("Nouvelle demande d'absence pour l'employé ID: {}", request.getEmployeId());

        Employe employe = employeRepository.findById(request.getEmployeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec l'ID: " + request.getEmployeId()));

        // Validation des dates
        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new BusinessException("La date de fin doit être après la date de début");
        }

        // Vérification des chevauchements
        List<Absence> absencesExistantes = absenceRepository
                .findByEmployeAndPeriodeOverlap(employe, request.getDateDebut(), request.getDateFin());

        if (!absencesExistantes.isEmpty()) {
            throw new BusinessException("Il existe déjà une absence pour cette période");
        }

        Absence absence = new Absence();
        absence.setEmploye(employe);
        absence.setType(request.getType());
        absence.setDateDebut(request.getDateDebut());
        absence.setDateFin(request.getDateFin());
        absence.setMotif(request.getMotif());
        absence.setStatut(StatutDemande.EN_ATTENTE);

        absence = absenceRepository.save(absence);
        log.info("Demande d'absence créée avec succès: ID {}", absence.getId());

        return convertirEnDTO(absence);
    }

    @Override
    public AbsenceDTO obtenirAbsence(Long id) {
        log.info("Récupération de l'absence ID: {}", id);
        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Absence non trouvée avec l'ID: " + id));
        return convertirEnDTO(absence);
    }

    @Override
    public AbsenceDTO validerAbsence(Long absenceId, com.supermarket.manager.model.dto.ValidationAbsenceRequest request) {
        log.info("Validation de l'absence ID: {}", absenceId);

        Absence absence = absenceRepository.findById(absenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Absence non trouvée avec l'ID: " + absenceId));

        if (absence.getStatut() != StatutDemande.EN_ATTENTE) {
            throw new BusinessException("Seules les absences en attente peuvent être validées");
        }

        absence.setStatut(request.getApprouvee() ? StatutDemande.APPROUVEE : StatutDemande.REFUSEE);
        absence.setCommentaireValidation(request.getCommentaire());
        absence = absenceRepository.save(absence);

        return convertirEnDTO(absence);
    }

    @Override
    public AbsenceDTO annulerAbsence(Long absenceId) {
        log.info("Annulation de l'absence ID: {}", absenceId);

        Absence absence = absenceRepository.findById(absenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Absence non trouvée avec l'ID: " + absenceId));

        if (absence.getStatut() == StatutDemande.APPROUVEE) {
            throw new BusinessException("Impossible d'annuler une absence déjà approuvée");
        }

        absence.setStatut(StatutDemande.ANNULEE);
        absence = absenceRepository.save(absence);

        return convertirEnDTO(absence);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsenceDTO> obtenirAbsencesEmploye(Long employeId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec l'ID: " + employeId));

        return absenceRepository.findByEmploye(employe).stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsenceDTO> obtenirAbsencesParPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return absenceRepository
                .findByPeriode(dateDebut, dateFin)
                .stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsenceDTO> obtenirAbsencesEnAttente() {
        return absenceRepository.findByStatut(StatutDemande.EN_ATTENTE).stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsenceDTO> obtenirAbsencesEnCoursEmploye(Long employeId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec l'ID: " + employeId));

        LocalDate now = LocalDate.now();
        return absenceRepository.findByEmploye(employe).stream()
                .filter(absence -> absence.getStatut() == StatutDemande.APPROUVEE)
                .filter(absence -> !absence.getDateFin().isBefore(now))
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    private AbsenceDTO convertirEnDTO(Absence absence) {
        long nombreJours = ChronoUnit.DAYS.between(absence.getDateDebut(), absence.getDateFin()) + 1;

        return AbsenceDTO.builder()
                .id(absence.getId())
                .employeId(absence.getEmploye().getId())
                .employeNom(absence.getEmploye().getNom() + " " + absence.getEmploye().getPrenom())
                .type(absence.getType())
                .dateDebut(absence.getDateDebut())
                .dateFin(absence.getDateFin())
                .nombreJours(nombreJours)
                .motif(absence.getMotif())
                .statut(absence.getStatut())
                .build();
    }
}
