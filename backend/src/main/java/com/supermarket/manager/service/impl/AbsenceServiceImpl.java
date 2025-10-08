package com.supermarket.manager.service.impl;

import com.supermarket.manager.exception.BusinessException;
import com.supermarket.manager.exception.ResourceNotFoundException;
import com.supermarket.manager.model.dto.AbsenceDTO;
import com.supermarket.manager.model.rh.Absence;
import com.supermarket.manager.model.rh.Employe;
import com.supermarket.manager.model.rh.StatutAbsence;
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
    public AbsenceDTO demanderAbsence(AbsenceDTO absenceDTO) {
        log.info("Nouvelle demande d'absence pour l'employé ID: {}", absenceDTO.getEmployeId());

        Employe employe = employeRepository.findById(absenceDTO.getEmployeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec l'ID: " + absenceDTO.getEmployeId()));

        // Validation des dates
        if (absenceDTO.getDateFin().isBefore(absenceDTO.getDateDebut())) {
            throw new BusinessException("La date de fin doit être après la date de début");
        }

        // Vérification des chevauchements
        List<Absence> absencesExistantes = absenceRepository
                .findByEmployeAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
                        employe, absenceDTO.getDateFin(), absenceDTO.getDateDebut());

        if (!absencesExistantes.isEmpty()) {
            throw new BusinessException("Il existe déjà une absence pour cette période");
        }

        Absence absence = new Absence();
        absence.setEmploye(employe);
        absence.setType(absenceDTO.getType());
        absence.setDateDebut(absenceDTO.getDateDebut());
        absence.setDateFin(absenceDTO.getDateFin());
        absence.setMotif(absenceDTO.getMotif());
        absence.setStatut(StatutAbsence.EN_ATTENTE);

        absence = absenceRepository.save(absence);
        log.info("Demande d'absence créée avec succès: ID {}", absence.getId());

        return convertirEnDTO(absence);
    }

    @Override
    public AbsenceDTO approuverAbsence(Long id) {
        log.info("Approbation de l'absence ID: {}", id);

        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Absence non trouvée avec l'ID: " + id));

        if (absence.getStatut() != StatutAbsence.EN_ATTENTE) {
            throw new BusinessException("Seules les absences en attente peuvent être approuvées");
        }

        absence.setStatut(StatutAbsence.APPROUVEE);
        absence = absenceRepository.save(absence);

        return convertirEnDTO(absence);
    }

    @Override
    public AbsenceDTO rejeterAbsence(Long id) {
        log.info("Rejet de l'absence ID: {}", id);

        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Absence non trouvée avec l'ID: " + id));

        if (absence.getStatut() != StatutAbsence.EN_ATTENTE) {
            throw new BusinessException("Seules les absences en attente peuvent être rejetées");
        }

        absence.setStatut(StatutAbsence.REJETEE);
        absence = absenceRepository.save(absence);

        return convertirEnDTO(absence);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsenceDTO> getAbsencesByEmploye(Long employeId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec l'ID: " + employeId));

        return absenceRepository.findByEmployeOrderByDateDebutDesc(employe).stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsenceDTO> getAbsencesByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return absenceRepository
                .findByDateDebutBetweenOrDateFinBetween(dateDebut, dateFin, dateDebut, dateFin)
                .stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsenceDTO> getAbsencesEnAttente() {
        return absenceRepository.findByStatut(StatutAbsence.EN_ATTENTE).stream()
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
                .nombreJours((int) nombreJours)
                .motif(absence.getMotif())
                .statut(absence.getStatut())
                .build();
    }
}

