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
        when(employeRepository.findById(1L)).thenReturn(Optional.of(employe));
        when(absenceRepository.findByEmployeAndPeriode(any(), any(), any())).thenReturn(List.of());
        when(absenceRepository.save(any(Absence.class))).thenReturn(absence);

        AbsenceDTO result = absenceService.creerDemandeAbsence(demandeRequest);

        assertThat(result).isNotNull();
        assertThat(result.getStatut()).isEqualTo(StatutDemande.EN_ATTENTE);
        assertThat(result.getType()).isEqualTo(TypeAbsence.CONGE_PAYE);
        verify(absenceRepository, times(1)).save(any(Absence.class));
    }

    @Test
    @DisplayName("Créer demande - Employé non trouvé")
    void creerDemandeAbsence_EmployeNotFound_ThrowsException() {
        when(employeRepository.findById(999L)).thenReturn(Optional.empty());
        demandeRequest.setEmployeId(999L);

        assertThatThrownBy(() -> absenceService.creerDemandeAbsence(demandeRequest))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Employé non trouvé");

        verify(absenceRepository, never()).save(any());
    }

    @Test
    @DisplayName("Créer demande - Dates invalides")
    void creerDemandeAbsence_DatesInvalides_ThrowsException() {
        when(employeRepository.findById(1L)).thenReturn(Optional.of(employe));
        demandeRequest.setDateFin(LocalDate.now().minusDays(1));

        assertThatThrownBy(() -> absenceService.creerDemandeAbsence(demandeRequest))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("date de fin doit être après");

        verify(absenceRepository, never()).save(any());
    }

    @Test
    @DisplayName("Valider une absence - Approuvée")
    void validerAbsence_Approuvee_Success() {
        ValidationAbsenceRequest validationRequest = ValidationAbsenceRequest.builder()
            .validateurId(2L)
            .approuvee(true)
            .commentaire("Approuvé")
            .build();

        when(absenceRepository.findById(1L)).thenReturn(Optional.of(absence));
        when(employeRepository.findById(2L)).thenReturn(Optional.of(validateur));
        when(absenceRepository.save(any(Absence.class))).thenReturn(absence);

        AbsenceDTO result = absenceService.validerAbsence(1L, validationRequest);

        assertThat(result).isNotNull();
        verify(absenceRepository, times(1)).save(any(Absence.class));
    }
}

