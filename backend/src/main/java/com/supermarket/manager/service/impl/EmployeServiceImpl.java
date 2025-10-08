package com.supermarket.manager.service.impl;

import com.supermarket.manager.exception.BusinessException;
import com.supermarket.manager.exception.ResourceNotFoundException;
import com.supermarket.manager.model.dto.EmployeDTO;
import com.supermarket.manager.model.rh.Employe;
import com.supermarket.manager.model.rh.PosteEmploye;
import com.supermarket.manager.model.rh.StatutEmploye;
import com.supermarket.manager.repository.EmployeRepository;
import com.supermarket.manager.service.EmployeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeServiceImpl implements EmployeService {
    
    private final EmployeRepository employeRepository;
    
    @Override
    public EmployeDTO creerEmploye(EmployeDTO employeDTO) {
        log.info("Création d'un nouvel employé: {}", employeDTO.getMatricule());
        
        // Vérifier unicité du matricule
        if (employeRepository.findByMatricule(employeDTO.getMatricule()).isPresent()) {
            throw new BusinessException("Un employé avec ce matricule existe déjà");
        }
        
        // Vérifier unicité de l'email
        if (employeRepository.findByEmail(employeDTO.getEmail()).isPresent()) {
            throw new BusinessException("Un employé avec cet email existe déjà");
        }
        
        Employe employe = mapToEntity(employeDTO);
        Employe saved = employeRepository.save(employe);
        
        log.info("Employé créé avec succès: ID={}", saved.getId());
        return mapToDTO(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EmployeDTO obtenirEmploye(Long id) {
        log.info("Récupération de l'employé: ID={}", id);
        Employe employe = employeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec l'ID: " + id));
        return mapToDTO(employe);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EmployeDTO obtenirParMatricule(String matricule) {
        log.info("Récupération de l'employé par matricule: {}", matricule);
        Employe employe = employeRepository.findByMatricule(matricule)
            .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec le matricule: " + matricule));
        return mapToDTO(employe);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeDTO> obtenirTousLesEmployes() {
        log.info("Récupération de tous les employés");
        return employeRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeDTO> obtenirEmployesActifs() {
        log.info("Récupération des employés actifs");
        return employeRepository.findByStatut(StatutEmploye.ACTIF).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeDTO> obtenirEmployesParPoste(PosteEmploye poste) {
        log.info("Récupération des employés par poste: {}", poste);
        return employeRepository.findByPoste(poste).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeDTO> rechercherEmployes(String search) {
        log.info("Recherche d'employés: {}", search);
        return employeRepository.searchEmployes(search).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public EmployeDTO modifierEmploye(Long id, EmployeDTO employeDTO) {
        log.info("Modification de l'employé: ID={}", id);
        
        Employe employe = employeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec l'ID: " + id));
        
        // Vérifier unicité de l'email si modifié
        if (!employe.getEmail().equals(employeDTO.getEmail())) {
            if (employeRepository.findByEmail(employeDTO.getEmail()).isPresent()) {
                throw new BusinessException("Un employé avec cet email existe déjà");
            }
            employe.setEmail(employeDTO.getEmail());
        }
        
        employe.setNom(employeDTO.getNom());
        employe.setPrenom(employeDTO.getPrenom());
        employe.setTelephone(employeDTO.getTelephone());
        employe.setAdresse(employeDTO.getAdresse());
        employe.setPoste(employeDTO.getPoste());
        employe.setStatut(employeDTO.getStatut());
        employe.setTypeContrat(employeDTO.getTypeContrat());
        employe.setSalaireBase(employeDTO.getSalaireBase());
        
        Employe updated = employeRepository.save(employe);
        log.info("Employé modifié avec succès: ID={}", updated.getId());
        return mapToDTO(updated);
    }
    
    @Override
    public EmployeDTO changerStatut(Long id, StatutEmploye statut) {
        log.info("Changement du statut de l'employé: ID={}, nouveau statut={}", id, statut);
        
        Employe employe = employeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec l'ID: " + id));
        
        employe.setStatut(statut);
        Employe updated = employeRepository.save(employe);
        
        log.info("Statut modifié avec succès");
        return mapToDTO(updated);
    }
    
    @Override
    public void supprimerEmploye(Long id) {
        log.info("Suppression de l'employé: ID={}", id);
        
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employé non trouvé avec l'ID: " + id);
        }
        
        employeRepository.deleteById(id);
        log.info("Employé supprimé avec succès");
    }
    
    @Override
    public List<EmployeDTO> getAllEmployes() {
        return obtenirTousLesEmployes();
    }

    @Override
    public EmployeDTO getEmployeById(Long id) {
        return obtenirEmploye(id);
    }

    @Override
    public EmployeDTO createEmploye(EmployeDTO employeDTO) {
        return creerEmploye(employeDTO);
    }

    @Override
    public EmployeDTO updateEmploye(Long id, EmployeDTO employeDTO) {
        return modifierEmploye(id, employeDTO);
    }

    @Override
    public void deleteEmploye(Long id) {
        supprimerEmploye(id);
    }

    // Méthodes de mapping
    private EmployeDTO mapToDTO(Employe employe) {
        return EmployeDTO.builder()
            .id(employe.getId())
            .matricule(employe.getMatricule())
            .nom(employe.getNom())
            .prenom(employe.getPrenom())
            .nomComplet(employe.getNomComplet())
            .email(employe.getEmail())
            .telephone(employe.getTelephone())
            .dateNaissance(employe.getDateNaissance())
            .adresse(employe.getAdresse())
            .dateEmbauche(employe.getDateEmbauche())
            .poste(employe.getPoste())
            .posteLibelle(employe.getPoste().getLibelle())
            .statut(employe.getStatut())
            .typeContrat(employe.getTypeContrat())
            .typeContratLibelle(employe.getTypeContrat().getLibelle())
            .salaireBase(employe.getSalaireBase())
            .nombreAbsences(employe.getAbsences().size())
            .build();
    }
    
    private Employe mapToEntity(EmployeDTO dto) {
        return Employe.builder()
            .matricule(dto.getMatricule())
            .nom(dto.getNom())
            .prenom(dto.getPrenom())
            .email(dto.getEmail())
            .telephone(dto.getTelephone())
            .dateNaissance(dto.getDateNaissance())
            .adresse(dto.getAdresse())
            .dateEmbauche(dto.getDateEmbauche())
            .poste(dto.getPoste())
            .statut(dto.getStatut() != null ? dto.getStatut() : StatutEmploye.ACTIF)
            .typeContrat(dto.getTypeContrat())
            .salaireBase(dto.getSalaireBase())
            .build();
    }
}
