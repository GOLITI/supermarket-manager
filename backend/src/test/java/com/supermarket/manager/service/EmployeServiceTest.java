package com.supermarket.manager.service;

import com.supermarket.manager.exception.BusinessException;
import com.supermarket.manager.exception.ResourceNotFoundException;
import com.supermarket.manager.model.dto.EmployeDTO;
import com.supermarket.manager.model.rh.*;
import com.supermarket.manager.repository.EmployeRepository;
import com.supermarket.manager.service.impl.EmployeServiceImpl;
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
        when(employeRepository.findByMatricule(employeDTO.getMatricule())).thenReturn(Optional.empty());
        when(employeRepository.findByEmail(employeDTO.getEmail())).thenReturn(Optional.empty());
        when(employeRepository.save(any(Employe.class))).thenReturn(employe);

        EmployeDTO result = employeService.creerEmploye(employeDTO);

        assertThat(result).isNotNull();
        assertThat(result.getMatricule()).isEqualTo("EMP001");
        assertThat(result.getNom()).isEqualTo("Dupont");
        verify(employeRepository, times(1)).save(any(Employe.class));
    }

    @Test
    @DisplayName("Créer un employé - Matricule déjà existant")
    void creerEmploye_MatriculeExistant_ThrowsException() {
        when(employeRepository.findByMatricule(employeDTO.getMatricule())).thenReturn(Optional.of(employe));

        assertThatThrownBy(() -> employeService.creerEmploye(employeDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("matricule existe déjà");

        verify(employeRepository, never()).save(any(Employe.class));
    }

    @Test
    @DisplayName("Obtenir un employé par ID avec succès")
    void obtenirEmploye_Success() {
        when(employeRepository.findById(1L)).thenReturn(Optional.of(employe));

        EmployeDTO result = employeService.obtenirEmploye(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getMatricule()).isEqualTo("EMP001");
    }

    @Test
    @DisplayName("Obtenir un employé par ID - Non trouvé")
    void obtenirEmploye_NotFound_ThrowsException() {
        when(employeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeService.obtenirEmploye(999L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Employé non trouvé");
    }

    @Test
    @DisplayName("Obtenir tous les employés actifs")
    void obtenirEmployesActifs_Success() {
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

        List<EmployeDTO> result = employeService.obtenirEmployesActifs();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStatut()).isEqualTo(StatutEmploye.ACTIF);
        assertThat(result.get(1).getStatut()).isEqualTo(StatutEmploye.ACTIF);
    }

    @Test
    @DisplayName("Supprimer un employé avec succès")
    void supprimerEmploye_Success() {
        when(employeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(employeRepository).deleteById(1L);

        employeService.supprimerEmploye(1L);

        verify(employeRepository, times(1)).deleteById(1L);
    }
}

