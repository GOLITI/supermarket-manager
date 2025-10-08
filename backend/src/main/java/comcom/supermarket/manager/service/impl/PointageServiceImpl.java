package comcom.supermarket.manager.service.impl;

import comcom.supermarket.manager.exception.BusinessException;
import comcom.supermarket.manager.exception.ResourceNotFoundException;
import comcom.supermarket.manager.model.dto.PointageDTO;
import comcom.supermarket.manager.model.rh.Employe;
import comcom.supermarket.manager.model.rh.Pointage;
import comcom.supermarket.manager.model.rh.TypePointage;
import comcom.supermarket.manager.repository.EmployeRepository;
import comcom.supermarket.manager.repository.PointageRepository;
import comcom.supermarket.manager.service.PointageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PointageServiceImpl implements PointageService {

    private final PointageRepository pointageRepository;
    private final EmployeRepository employeRepository;

    @Override
    public PointageDTO pointer(Long employeId) {
        log.info("Pointage pour l'employé: ID={}", employeId);

        Employe employe = employeRepository.findById(employeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec l'ID: " + employeId));

        Optional<Pointage> pointageEnCours = pointageRepository.findPointageEnCours(employeId);

        if (pointageEnCours.isPresent()) {
            Pointage pointage = pointageEnCours.get();
            pointage.setHeureSortie(LocalDateTime.now());
            pointage.calculerHeuresTravaillees();

            Pointage updated = pointageRepository.save(pointage);
            log.info("Pointage de sortie enregistré: ID={}, heures={}", updated.getId(), updated.getHeuresTravaillees());

            return mapToDTO(updated);
        } else {
            Pointage pointage = Pointage.builder()
                .employe(employe)
                .heureEntree(LocalDateTime.now())
                .type(TypePointage.NORMAL)
                .valide(false)
                .build();

            Pointage saved = pointageRepository.save(pointage);
            log.info("Pointage d'entrée enregistré: ID={}", saved.getId());

            return mapToDTO(saved);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PointageDTO obtenirPointage(Long id) {
        log.info("Récupération du pointage: ID={}", id);
        Pointage pointage = pointageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pointage non trouvé avec l'ID: " + id));
        return mapToDTO(pointage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PointageDTO> obtenirPointagesEmploye(Long employeId) {
        log.info("Récupération des pointages de l'employé: ID={}", employeId);
        return pointageRepository.findByEmployeId(employeId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PointageDTO> obtenirPointagesEmployeParDate(Long employeId, LocalDate date) {
        log.info("Récupération des pointages de l'employé {} pour la date: {}", employeId, date);
        return pointageRepository.findByEmployeAndDate(employeId, date).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PointageDTO> obtenirPointagesParPeriode(Long employeId, LocalDate debut, LocalDate fin) {
        log.info("Récupération des pointages de l'employé {} pour la période: {} - {}", employeId, debut, fin);

        LocalDateTime debutDateTime = debut.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(LocalTime.MAX);

        return pointageRepository.findByEmployeAndPeriode(employeId, debutDateTime, finDateTime).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public PointageDTO validerPointage(Long id) {
        log.info("Validation du pointage: ID={}", id);

        Pointage pointage = pointageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pointage non trouvé avec l'ID: " + id));

        if (pointage.getHeureSortie() == null) {
            throw new BusinessException("Impossible de valider un pointage en cours");
        }

        pointage.setValide(true);
        Pointage updated = pointageRepository.save(pointage);

        log.info("Pointage validé avec succès");
        return mapToDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculerHeuresTravaillees(Long employeId, LocalDate debut, LocalDate fin) {
        log.info("Calcul des heures travaillées pour l'employé {} période: {} - {}", employeId, debut, fin);

        LocalDateTime debutDateTime = debut.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(LocalTime.MAX);

        Double heures = pointageRepository.sumHeuresTravailleesByEmployeAndPeriode(
            employeId, debutDateTime, finDateTime
        );

        return heures != null ? heures : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculerHeuresSupplementaires(Long employeId, LocalDate debut, LocalDate fin) {
        log.info("Calcul des heures supplémentaires pour l'employé {} période: {} - {}", employeId, debut, fin);

        LocalDateTime debutDateTime = debut.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(LocalTime.MAX);

        Double heures = pointageRepository.sumHeuresSupplementairesByEmployeAndPeriode(
            employeId, debutDateTime, finDateTime
        );

        return heures != null ? heures : 0.0;
    }

    private PointageDTO mapToDTO(Pointage pointage) {
        return PointageDTO.builder()
            .id(pointage.getId())
            .employeId(pointage.getEmploye().getId())
            .employeNom(pointage.getEmploye().getNomComplet())
            .heureEntree(pointage.getHeureEntree())
            .heureSortie(pointage.getHeureSortie())
            .type(pointage.getType())
            .typeLibelle(pointage.getType().getLibelle())
            .notes(pointage.getNotes())
            .heuresTravaillees(pointage.getHeuresTravaillees())
            .heuresSupplementaires(pointage.getHeuresSupplementaires())
            .valide(pointage.getValide())
            .enCours(pointage.estEnCours())
            .build();
    }
}

