package comcom.supermarket.manager.service.impl;

import comcom.supermarket.manager.exception.BusinessException;
import comcom.supermarket.manager.exception.ResourceNotFoundException;
import comcom.supermarket.manager.model.dto.PlanningDTO;
import comcom.supermarket.manager.model.rh.Employe;
import comcom.supermarket.manager.model.rh.Planning;
import comcom.supermarket.manager.repository.EmployeRepository;
import comcom.supermarket.manager.repository.PlanningRepository;
import comcom.supermarket.manager.service.PlanningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PlanningServiceImpl implements PlanningService {
    
    private final PlanningRepository planningRepository;
    private final EmployeRepository employeRepository;
    
    @Override
    public PlanningDTO creerPlanning(PlanningDTO planningDTO) {
        log.info("Création d'un planning pour l'employé: ID={}", planningDTO.getEmployeId());
        
        Employe employe = employeRepository.findById(planningDTO.getEmployeId())
            .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec l'ID: " + planningDTO.getEmployeId()));
        
        // Vérifier qu'il n'y a pas de conflit d'horaires
        List<Planning> planningsExistants = planningRepository.findByEmployeIdAndDate(
            planningDTO.getEmployeId(),
            planningDTO.getDate()
        );
        
        if (!planningsExistants.isEmpty()) {
            throw new BusinessException("Un planning existe déjà pour cet employé à cette date");
        }
        
        Planning planning = Planning.builder()
            .employe(employe)
            .date(planningDTO.getDate())
            .heureDebut(planningDTO.getHeureDebut())
            .heureFin(planningDTO.getHeureFin())
            .typeShift(planningDTO.getTypeShift())
            .posteAssigne(planningDTO.getPosteAssigne())
            .notes(planningDTO.getNotes())
            .valide(false)
            .build();
        
        Planning saved = planningRepository.save(planning);
        log.info("Planning créé avec succès: ID={}", saved.getId());
        
        return mapToDTO(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PlanningDTO obtenirPlanning(Long id) {
        log.info("Récupération du planning: ID={}", id);
        Planning planning = planningRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Planning non trouvé avec l'ID: " + id));
        return mapToDTO(planning);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PlanningDTO> obtenirPlanningsEmploye(Long employeId) {
        log.info("Récupération des plannings de l'employé: ID={}", employeId);
        return planningRepository.findByEmployeId(employeId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PlanningDTO> obtenirPlanningsParDate(LocalDate date) {
        log.info("Récupération des plannings pour la date: {}", date);
        return planningRepository.findByDate(date).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PlanningDTO> obtenirPlanningsParPeriode(LocalDate debut, LocalDate fin) {
        log.info("Récupération des plannings pour la période: {} - {}", debut, fin);
        return planningRepository.findByPeriode(debut, fin).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PlanningDTO> obtenirPlanningsEmployePeriode(Long employeId, LocalDate debut, LocalDate fin) {
        log.info("Récupération des plannings de l'employé {} pour la période: {} - {}", employeId, debut, fin);
        return planningRepository.findByEmployeAndPeriode(employeId, debut, fin).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public PlanningDTO modifierPlanning(Long id, PlanningDTO planningDTO) {
        log.info("Modification du planning: ID={}", id);
        
        Planning planning = planningRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Planning non trouvé avec l'ID: " + id));
        
        planning.setDate(planningDTO.getDate());
        planning.setHeureDebut(planningDTO.getHeureDebut());
        planning.setHeureFin(planningDTO.getHeureFin());
        planning.setTypeShift(planningDTO.getTypeShift());
        planning.setPosteAssigne(planningDTO.getPosteAssigne());
        planning.setNotes(planningDTO.getNotes());
        
        Planning updated = planningRepository.save(planning);
        log.info("Planning modifié avec succès");
        
        return mapToDTO(updated);
    }
    
    @Override
    public PlanningDTO validerPlanning(Long id) {
        log.info("Validation du planning: ID={}", id);
        
        Planning planning = planningRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Planning non trouvé avec l'ID: " + id));
        
        planning.setValide(true);
        Planning updated = planningRepository.save(planning);
        
        log.info("Planning validé avec succès");
        return mapToDTO(updated);
    }
    
    @Override
    public void supprimerPlanning(Long id) {
        log.info("Suppression du planning: ID={}", id);
        
        if (!planningRepository.existsById(id)) {
            throw new ResourceNotFoundException("Planning non trouvé avec l'ID: " + id);
        }
        
        planningRepository.deleteById(id);
        log.info("Planning supprimé avec succès");
    }
    
    @Override
    public List<PlanningDTO> genererPlanningHebdomadaire(LocalDate dateDebut) {
        log.info("Génération du planning hebdomadaire à partir du: {}", dateDebut);
        
        List<PlanningDTO> plannings = new ArrayList<>();
        // Logique de génération automatique à implémenter selon les besoins
        // Par exemple, assigner automatiquement les employés disponibles
        
        log.info("Planning hebdomadaire généré");
        return plannings;
    }
    
    // Méthode de mapping
    private PlanningDTO mapToDTO(Planning planning) {
        return PlanningDTO.builder()
            .id(planning.getId())
            .employeId(planning.getEmploye().getId())
            .employeNom(planning.getEmploye().getNomComplet())
            .date(planning.getDate())
            .heureDebut(planning.getHeureDebut())
            .heureFin(planning.getHeureFin())
            .dureeHeures(planning.getDureeHeures())
            .typeShift(planning.getTypeShift())
            .typeShiftLibelle(planning.getTypeShift().getLibelle())
            .posteAssigne(planning.getPosteAssigne())
            .posteAssigneLibelle(planning.getPosteAssigne().getLibelle())
            .notes(planning.getNotes())
            .valide(planning.getValide())
            .build();
    }
}

