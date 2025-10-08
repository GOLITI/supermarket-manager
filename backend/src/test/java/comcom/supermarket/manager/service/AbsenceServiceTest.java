import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du service Absence")
class AbsenceServiceTest {
    
    @Mock
    private AbsenceRepository absenceRepository;
    
    @Mock
    private EmployeRepository employeRepository;
    
    @InjectMocks
    private AbsenceServiceImpl absenceService;
    
    private Employe employe;
    private Employe validateur;
    private Absence absence;
    private DemandeAbsenceRequest demandeRequest;
    
    @BeforeEach
    void setUp() {
        employe = Employe.builder()
            .id(1L)
            .matricule("EMP001")
            .nom("Dupont")
            .prenom("Sarah")
            .email("sarah.dupont@supermarket.com")
            .poste(PosteEmploye.CAISSIER)
            .statut(StatutEmploye.ACTIF)
            .build();
        
        validateur = Employe.builder()
            .id(2L)
            .matricule("MGR001")
            .nom("Manager")
            .prenom("John")
            .email("john.manager@supermarket.com")
            .poste(PosteEmploye.RESPONSABLE_RH)
            .statut(StatutEmploye.ACTIF)
            .build();
        
        absence = Absence.builder()
            .id(1L)
            .employe(employe)
            .type(TypeAbsence.CONGE_PAYE)
            .dateDebut(LocalDate.now().plusDays(1))
            .dateFin(LocalDate.now().plusDays(5))
            .motif("Congés annuels")
            .statut(StatutDemande.EN_ATTENTE)
            .build();
        
        demandeRequest = DemandeAbsenceRequest.builder()
            .employeId(1L)
            .type(TypeAbsence.CONGE_PAYE)
            .dateDebut(LocalDate.now().plusDays(1))
            .dateFin(LocalDate.now().plusDays(5))
            .motif("Congés annuels")
            .build();
    }
    
    @Test
    @DisplayName("Créer une demande d'absence avec succès")
    void creerDemandeAbsence_Success() {
        // Given
        when(employeRepository.findById(1L)).thenReturn(Optional.of(employe));
        when(absenceRepository.findByEmployeAndPeriode(any(), any(), any())).thenReturn(List.of());
        when(absenceRepository.save(any(Absence.class))).thenReturn(absence);
        
        // When
        AbsenceDTO result = absenceService.creerDemandeAbsence(demandeRequest);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatut()).isEqualTo(StatutDemande.EN_ATTENTE);
        assertThat(result.getType()).isEqualTo(TypeAbsence.CONGE_PAYE);
        verify(absenceRepository, times(1)).save(any(Absence.class));
    }
    
    @Test
    @DisplayName("Créer demande - Employé non trouvé")
    void creerDemandeAbsence_EmployeNotFound_ThrowsException() {
        // Given
        when(employeRepository.findById(999L)).thenReturn(Optional.empty());
        demandeRequest.setEmployeId(999L);
        
        // When & Then
        assertThatThrownBy(() -> absenceService.creerDemandeAbsence(demandeRequest))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Employé non trouvé");
        
        verify(absenceRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Créer demande - Dates invalides")
    void creerDemandeAbsence_DatesInvalides_ThrowsException() {
        // Given
        when(employeRepository.findById(1L)).thenReturn(Optional.of(employe));
        demandeRequest.setDateFin(LocalDate.now().minusDays(1));
        
        // When & Then
        assertThatThrownBy(() -> absenceService.creerDemandeAbsence(demandeRequest))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("date de fin doit être après");
        
        verify(absenceRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Créer demande - Chevauchement d'absences")
    void creerDemandeAbsence_Chevauchement_ThrowsException() {
        // Given
        when(employeRepository.findById(1L)).thenReturn(Optional.of(employe));
        when(absenceRepository.findByEmployeAndPeriode(any(), any(), any()))
            .thenReturn(Arrays.asList(absence));
        
        // When & Then
        assertThatThrownBy(() -> absenceService.creerDemandeAbsence(demandeRequest))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("absence existe déjà");
        
        verify(absenceRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Obtenir une absence par ID")
    void obtenirAbsence_Success() {
        // Given
        when(absenceRepository.findById(1L)).thenReturn(Optional.of(absence));
        
        // When
        AbsenceDTO result = absenceService.obtenirAbsence(1L);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmployeNom()).contains("Sarah Dupont");
    }
    
    @Test
    @DisplayName("Obtenir absences d'un employé")
    void obtenirAbsencesEmploye_Success() {
        // Given
        when(absenceRepository.findByEmployeId(1L))
            .thenReturn(Arrays.asList(absence));
        
        // When
        List<AbsenceDTO> result = absenceService.obtenirAbsencesEmploye(1L);
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmployeId()).isEqualTo(1L);
    }
    
    @Test
    @DisplayName("Obtenir absences en attente")
    void obtenirAbsencesEnAttente_Success() {
        // Given
        when(absenceRepository.findByStatut(StatutDemande.EN_ATTENTE))
            .thenReturn(Arrays.asList(absence));
        
        // When
        List<AbsenceDTO> result = absenceService.obtenirAbsencesEnAttente();
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatut()).isEqualTo(StatutDemande.EN_ATTENTE);
    }
    
    @Test
    @DisplayName("Valider une absence - Approuvée")
    void validerAbsence_Approuvee_Success() {
        // Given
        ValidationAbsenceRequest validationRequest = ValidationAbsenceRequest.builder()
            .validateurId(2L)
            .approuvee(true)
            .commentaire("Approuvé")
            .build();
        
        when(absenceRepository.findById(1L)).thenReturn(Optional.of(absence));
        when(employeRepository.findById(2L)).thenReturn(Optional.of(validateur));
        when(absenceRepository.save(any(Absence.class))).thenReturn(absence);
        
        // When
        AbsenceDTO result = absenceService.validerAbsence(1L, validationRequest);
        
        // Then
        assertThat(result).isNotNull();
        verify(absenceRepository, times(1)).save(any(Absence.class));
    }
    
    @Test
    @DisplayName("Valider une absence - Refusée")
    void validerAbsence_Refusee_Success() {
        // Given
        ValidationAbsenceRequest validationRequest = ValidationAbsenceRequest.builder()
            .validateurId(2L)
            .approuvee(false)
            .commentaire("Refusé - Période chargée")
            .build();
        
        when(absenceRepository.findById(1L)).thenReturn(Optional.of(absence));
        when(employeRepository.findById(2L)).thenReturn(Optional.of(validateur));
        when(absenceRepository.save(any(Absence.class))).thenReturn(absence);
        
        // When
        AbsenceDTO result = absenceService.validerAbsence(1L, validationRequest);
        
        // Then
        assertThat(result).isNotNull();
        verify(absenceRepository, times(1)).save(any(Absence.class));
    }
    
    @Test
    @DisplayName("Valider absence - Déjà traitée")
    void validerAbsence_DejaTraitee_ThrowsException() {
        // Given
        absence.setStatut(StatutDemande.APPROUVEE);
        ValidationAbsenceRequest validationRequest = ValidationAbsenceRequest.builder()
            .validateurId(2L)
            .approuvee(true)
            .build();
        
        when(absenceRepository.findById(1L)).thenReturn(Optional.of(absence));
        
        // When & Then
        assertThatThrownBy(() -> absenceService.validerAbsence(1L, validationRequest))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("déjà été traitée");
        
        verify(absenceRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Annuler une absence")
    void annulerAbsence_Success() {
        // Given
        when(absenceRepository.findById(1L)).thenReturn(Optional.of(absence));
        when(absenceRepository.save(any(Absence.class))).thenReturn(absence);
        
        // When
        AbsenceDTO result = absenceService.annulerAbsence(1L);
        
        // Then
        assertThat(result).isNotNull();
        verify(absenceRepository, times(1)).save(any(Absence.class));
    }
    
    @Test
    @DisplayName("Annuler absence - Déjà annulée")
    void annulerAbsence_DejaAnnulee_ThrowsException() {
        // Given
        absence.setStatut(StatutDemande.ANNULEE);
        when(absenceRepository.findById(1L)).thenReturn(Optional.of(absence));
        
        // When & Then
        assertThatThrownBy(() -> absenceService.annulerAbsence(1L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("déjà annulée");
        
        verify(absenceRepository, never()).save(any());
    }
}
package comcom.supermarket.manager.service;

import comcom.supermarket.manager.exception.BusinessException;
import comcom.supermarket.manager.exception.ResourceNotFoundException;
import comcom.supermarket.manager.model.dto.AbsenceDTO;
import comcom.supermarket.manager.model.dto.DemandeAbsenceRequest;
import comcom.supermarket.manager.model.dto.ValidationAbsenceRequest;
import comcom.supermarket.manager.model.rh.*;
import comcom.supermarket.manager.repository.AbsenceRepository;
import comcom.supermarket.manager.repository.EmployeRepository;
import comcom.supermarket.manager.service.impl.AbsenceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
package comcom.supermarket.manager.service;

import comcom.supermarket.manager.exception.BusinessException;
import comcom.supermarket.manager.exception.ResourceNotFoundException;
import comcom.supermarket.manager.model.dto.EmployeDTO;
import comcom.supermarket.manager.model.rh.*;
import comcom.supermarket.manager.repository.EmployeRepository;
import comcom.supermarket.manager.service.impl.EmployeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du service Employé")
class EmployeServiceTest {
    
    @Mock
    private EmployeRepository employeRepository;
    
    @InjectMocks
    private EmployeServiceImpl employeService;
    
    private Employe employe;
    private EmployeDTO employeDTO;
    
    @BeforeEach
    void setUp() {
        employe = Employe.builder()
            .id(1L)
            .matricule("EMP001")
            .nom("Dupont")
            .prenom("Sarah")
            .email("sarah.dupont@supermarket.com")
            .telephone("0123456789")
            .dateNaissance(LocalDate.of(1990, 5, 15))
            .adresse("123 Rue Test")
            .dateEmbauche(LocalDate.of(2020, 1, 1))
            .poste(PosteEmploye.CAISSIER)
            .statut(StatutEmploye.ACTIF)
            .typeContrat(TypeContrat.CDI)
            .salaireBase(1800.0)
            .build();
        
        employeDTO = EmployeDTO.builder()
            .matricule("EMP001")
            .nom("Dupont")
            .prenom("Sarah")
            .email("sarah.dupont@supermarket.com")
            .telephone("0123456789")
            .dateNaissance(LocalDate.of(1990, 5, 15))
            .adresse("123 Rue Test")
            .dateEmbauche(LocalDate.of(2020, 1, 1))
            .poste(PosteEmploye.CAISSIER)
            .statut(StatutEmploye.ACTIF)
            .typeContrat(TypeContrat.CDI)
            .salaireBase(1800.0)
            .build();
    }
    
    @Test
    @DisplayName("Créer un employé avec succès")
    void creerEmploye_Success() {
        // Given
        when(employeRepository.findByMatricule(employeDTO.getMatricule())).thenReturn(Optional.empty());
        when(employeRepository.findByEmail(employeDTO.getEmail())).thenReturn(Optional.empty());
        when(employeRepository.save(any(Employe.class))).thenReturn(employe);
        
        // When
        EmployeDTO result = employeService.creerEmploye(employeDTO);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getMatricule()).isEqualTo("EMP001");
        assertThat(result.getNom()).isEqualTo("Dupont");
        verify(employeRepository, times(1)).save(any(Employe.class));
    }
    
    @Test
    @DisplayName("Créer un employé - Matricule déjà existant")
    void creerEmploye_MatriculeExistant_ThrowsException() {
        // Given
        when(employeRepository.findByMatricule(employeDTO.getMatricule())).thenReturn(Optional.of(employe));
        
        // When & Then
        assertThatThrownBy(() -> employeService.creerEmploye(employeDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("matricule existe déjà");
        
        verify(employeRepository, never()).save(any(Employe.class));
    }
    
    @Test
    @DisplayName("Créer un employé - Email déjà existant")
    void creerEmploye_EmailExistant_ThrowsException() {
        // Given
        when(employeRepository.findByMatricule(employeDTO.getMatricule())).thenReturn(Optional.empty());
        when(employeRepository.findByEmail(employeDTO.getEmail())).thenReturn(Optional.of(employe));
        
        // When & Then
        assertThatThrownBy(() -> employeService.creerEmploye(employeDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("email existe déjà");
        
        verify(employeRepository, never()).save(any(Employe.class));
    }
    
    @Test
    @DisplayName("Obtenir un employé par ID avec succès")
    void obtenirEmploye_Success() {
        // Given
        when(employeRepository.findById(1L)).thenReturn(Optional.of(employe));
        
        // When
        EmployeDTO result = employeService.obtenirEmploye(1L);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getMatricule()).isEqualTo("EMP001");
    }
    
    @Test
    @DisplayName("Obtenir un employé par ID - Non trouvé")
    void obtenirEmploye_NotFound_ThrowsException() {
        // Given
        when(employeRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> employeService.obtenirEmploye(999L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Employé non trouvé");
    }
    
    @Test
    @DisplayName("Obtenir tous les employés actifs")
    void obtenirEmployesActifs_Success() {
        // Given
        Employe employe2 = Employe.builder()
            .id(2L)
            .matricule("EMP002")
            .nom("Martin")
            .prenom("Jean")
            .email("jean.martin@supermarket.com")
            .telephone("0987654321")
            .dateNaissance(LocalDate.of(1985, 3, 20))
            .adresse("456 Rue Test")
            .dateEmbauche(LocalDate.of(2019, 6, 1))
            .poste(PosteEmploye.VENDEUR)
            .statut(StatutEmploye.ACTIF)
            .typeContrat(TypeContrat.CDI)
            .salaireBase(2000.0)
            .build();
        
        when(employeRepository.findByStatut(StatutEmploye.ACTIF))
            .thenReturn(Arrays.asList(employe, employe2));
        
        // When
        List<EmployeDTO> result = employeService.obtenirEmployesActifs();
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStatut()).isEqualTo(StatutEmploye.ACTIF);
        assertThat(result.get(1).getStatut()).isEqualTo(StatutEmploye.ACTIF);
    }
    
    @Test
    @DisplayName("Obtenir employés par poste")
    void obtenirEmployesParPoste_Success() {
        // Given
        when(employeRepository.findByPoste(PosteEmploye.CAISSIER))
            .thenReturn(Arrays.asList(employe));
        
        // When
        List<EmployeDTO> result = employeService.obtenirEmployesParPoste(PosteEmploye.CAISSIER);
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPoste()).isEqualTo(PosteEmploye.CAISSIER);
    }
    
    @Test
    @DisplayName("Rechercher des employés")
    void rechercherEmployes_Success() {
        // Given
        when(employeRepository.searchEmployes("Dupont"))
            .thenReturn(Arrays.asList(employe));
        
        // When
        List<EmployeDTO> result = employeService.rechercherEmployes("Dupont");
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Dupont");
    }
    
    @Test
    @DisplayName("Modifier un employé avec succès")
    void modifierEmploye_Success() {
        // Given
        when(employeRepository.findById(1L)).thenReturn(Optional.of(employe));
        when(employeRepository.save(any(Employe.class))).thenReturn(employe);
        
        employeDTO.setId(1L);
        employeDTO.setTelephone("0111111111");
        
        // When
        EmployeDTO result = employeService.modifierEmploye(1L, employeDTO);
        
        // Then
        assertThat(result).isNotNull();
        verify(employeRepository, times(1)).save(any(Employe.class));
    }
    
    @Test
    @DisplayName("Changer le statut d'un employé")
    void changerStatut_Success() {
        // Given
        when(employeRepository.findById(1L)).thenReturn(Optional.of(employe));
        when(employeRepository.save(any(Employe.class))).thenReturn(employe);
        
        // When
        EmployeDTO result = employeService.changerStatut(1L, StatutEmploye.EN_CONGE);
        
        // Then
        assertThat(result).isNotNull();
        verify(employeRepository, times(1)).save(any(Employe.class));
    }
    
    @Test
    @DisplayName("Supprimer un employé avec succès")
    void supprimerEmploye_Success() {
        // Given
        when(employeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(employeRepository).deleteById(1L);
        
        // When
        employeService.supprimerEmploye(1L);
        
        // Then
        verify(employeRepository, times(1)).deleteById(1L);
    }
    
    @Test
    @DisplayName("Supprimer un employé - Non trouvé")
    void supprimerEmploye_NotFound_ThrowsException() {
        // Given
        when(employeRepository.existsById(999L)).thenReturn(false);
        
        // When & Then
        assertThatThrownBy(() -> employeService.supprimerEmploye(999L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Employé non trouvé");
        
        verify(employeRepository, never()).deleteById(any());
    }
}

