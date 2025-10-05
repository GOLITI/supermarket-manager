package comcom.supermarket.manager.service;

import comcom.supermarket.manager.exception.BusinessException;
import comcom.supermarket.manager.exception.ResourceNotFoundException;
import comcom.supermarket.manager.model.commande.Commande;
import comcom.supermarket.manager.model.commande.LigneCommande;
import comcom.supermarket.manager.model.commande.StatutCommande;
import comcom.supermarket.manager.model.dto.CommandeDTO;
import comcom.supermarket.manager.model.dto.StockAlertDTO;
import comcom.supermarket.manager.model.fournisseur.Fournisseur;
import comcom.supermarket.manager.model.fournisseur.TypeFournisseur;
import comcom.supermarket.manager.model.produit.*;
import comcom.supermarket.manager.repository.CommandeRepository;
import comcom.supermarket.manager.repository.FournisseurRepository;
import comcom.supermarket.manager.repository.ProduitRepository;
import comcom.supermarket.manager.service.impl.CommandeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandeServiceTest {

    @Mock
    private CommandeRepository commandeRepository;

    @Mock
    private FournisseurRepository fournisseurRepository;

    @Mock
    private ProduitRepository produitRepository;

    @Mock
    private StockService stockService;

    @InjectMocks
    private CommandeServiceImpl commandeService;

    private Commande commande;
    private Fournisseur fournisseur;
    private Produit produit;
    private LigneCommande ligneCommande;

    @BeforeEach
    void setUp() {
        // Créer un fournisseur
        fournisseur = Fournisseur.builder()
                .id(1L)
                .nom("Meunerie Test")
                .code("FOUR-001")
                .type(TypeFournisseur.PRODUCTEUR_LOCAL)
                .delaiLivraisonJours(3)
                .build();

        // Créer un produit
        Categorie categorie = Categorie.builder()
                .id(1L)
                .nom("Farines")
                .type(TypeProduit.ALIMENTAIRE_SEC)
                .build();

        produit = Produit.builder()
                .id(1L)
                .code("PROD-001")
                .nom("Farine T45")
                .categorie(categorie)
                .fournisseur(fournisseur)
                .prixAchat(new BigDecimal("1500.00"))
                .prixVente(new BigDecimal("2000.00"))
                .build();

        // Créer une ligne de commande
        ligneCommande = LigneCommande.builder()
                .id(1L)
                .produit(produit)
                .quantite(500)
                .quantiteRecue(0)
                .prixUnitaire(new BigDecimal("1500.00"))
                .build();

        // Créer une commande
        commande = Commande.builder()
                .id(1L)
                .numeroCommande("CMD-20250105-0001")
                .fournisseur(fournisseur)
                .statut(StatutCommande.BROUILLON)
                .dateCommande(LocalDate.now())
                .montantTotal(new BigDecimal("750000.00"))
                .build();

        commande.ajouterLigne(ligneCommande);
    }

    @Test
    void testCreerCommande() {
        // Given
        when(commandeRepository.save(any(Commande.class))).thenReturn(commande);

        // When
        Commande result = commandeService.creerCommande(commande);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNumeroCommande()).startsWith("CMD-");
        assertThat(result.getMontantTotal()).isEqualTo(new BigDecimal("750000.00"));
        verify(commandeRepository, times(1)).save(any(Commande.class));
    }

    @Test
    void testGetCommandeById_Success() {
        // Given
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        // When
        Commande result = commandeService.getCommandeById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(commandeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCommandeById_NotFound() {
        // Given
        when(commandeRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> commandeService.getCommandeById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Commande");
    }

    @Test
    void testValiderCommande_Success() {
        // Given
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(commandeRepository.save(any(Commande.class))).thenReturn(commande);

        // When
        Commande result = commandeService.validerCommande(1L);

        // Then
        assertThat(result.getStatut()).isEqualTo(StatutCommande.CONFIRMEE);
        verify(commandeRepository, times(1)).save(any(Commande.class));
    }

    @Test
    void testValiderCommande_SansLignes() {
        // Given
        Commande commandeVide = Commande.builder()
                .id(2L)
                .numeroCommande("CMD-TEST")
                .fournisseur(fournisseur)
                .statut(StatutCommande.BROUILLON)
                .dateCommande(LocalDate.now())
                .build();

        when(commandeRepository.findById(2L)).thenReturn(Optional.of(commandeVide));

        // When & Then
        assertThatThrownBy(() -> commandeService.validerCommande(2L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("sans lignes");
    }

    @Test
    void testAnnulerCommande_Success() {
        // Given
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(commandeRepository.save(any(Commande.class))).thenReturn(commande);

        // When
        Commande result = commandeService.annulerCommande(1L);

        // Then
        assertThat(result.getStatut()).isEqualTo(StatutCommande.ANNULEE);
        verify(commandeRepository, times(1)).save(any(Commande.class));
    }

    @Test
    void testAnnulerCommande_DejaLivree() {
        // Given
        commande.setStatut(StatutCommande.LIVREE);
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        // When & Then
        assertThatThrownBy(() -> commandeService.annulerCommande(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("livrée");
    }

    @Test
    void testRecevoirCommande_Complete() {
        // Given
        commande.setStatut(StatutCommande.CONFIRMEE);
        ligneCommande.setQuantiteRecue(500); // Quantité complète reçue

        Stock stock = Stock.builder()
                .id(1L)
                .produit(produit)
                .quantite(50)
                .seuilReapprovisionnement(100)
                .build();

        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(stockService.getStocksByProduit(1L)).thenReturn(Arrays.asList(stock));
        when(stockService.ajouterQuantite(1L, 500)).thenReturn(stock);
        when(commandeRepository.save(any(Commande.class))).thenReturn(commande);

        // When
        Commande result = commandeService.recevoirCommande(1L, LocalDate.now());

        // Then
        assertThat(result.getStatut()).isEqualTo(StatutCommande.LIVREE);
        assertThat(result.getDateLivraisonEffective()).isNotNull();
        verify(stockService, times(1)).ajouterQuantite(1L, 500);
    }

    @Test
    void testGenererCommandesAutomatiques() {
        // Given
        StockAlertDTO alerte = StockAlertDTO.builder()
                .stockId(1L)
                .produitId(1L)
                .nomProduit("Farine T45")
                .quantiteActuelle(50)
                .seuilReapprovisionnement(100)
                .quantiteRecommandee(500)
                .prixAchat(new BigDecimal("1500.00"))
                .fournisseurId(1L)
                .nomFournisseur("Meunerie Test")
                .delaiLivraisonJours(3)
                .build();

        when(stockService.getStocksEnAlerte()).thenReturn(Arrays.asList(alerte));
        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(fournisseur));
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(commandeRepository.save(any(Commande.class))).thenReturn(commande);

        // When
        List<Commande> result = commandeService.genererCommandesAutomatiques();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCommandeAutomatique()).isTrue();
        assertThat(result.get(0).getStatut()).isEqualTo(StatutCommande.BROUILLON);
        verify(commandeRepository, atLeastOnce()).save(any(Commande.class));
    }

    @Test
    void testChangerStatut_Success() {
        // Given
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(commandeRepository.save(any(Commande.class))).thenReturn(commande);

        // When
        Commande result = commandeService.changerStatut(1L, StatutCommande.CONFIRMEE);

        // Then
        assertThat(result.getStatut()).isEqualTo(StatutCommande.CONFIRMEE);
        verify(commandeRepository, times(1)).save(any(Commande.class));
    }

    @Test
    void testGetCommandesByStatut() {
        // Given
        List<Commande> commandes = Arrays.asList(commande);
        when(commandeRepository.findByStatut(StatutCommande.BROUILLON))
                .thenReturn(commandes);

        // When
        List<Commande> result = commandeService.getCommandesByStatut(StatutCommande.BROUILLON);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatut()).isEqualTo(StatutCommande.BROUILLON);
        verify(commandeRepository, times(1)).findByStatut(StatutCommande.BROUILLON);
    }

    @Test
    void testToDTO() {
        // When
        CommandeDTO result = commandeService.toDTO(commande);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNumeroCommande()).isEqualTo("CMD-20250105-0001");
        assertThat(result.getNomFournisseur()).isEqualTo("Meunerie Test");
        assertThat(result.getStatut()).isEqualTo(StatutCommande.BROUILLON);
        assertThat(result.getLignes()).hasSize(1);
        assertThat(result.getLignes().get(0).getNomProduit()).isEqualTo("Farine T45");
    }

    @Test
    void testUpdateCommande_StatutInvalide() {
        // Given
        commande.setStatut(StatutCommande.LIVREE);
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        // When & Then
        assertThatThrownBy(() -> commandeService.updateCommande(1L, commande))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("modifier");
    }

    @Test
    void testDeleteCommande_StatutInvalide() {
        // Given
        commande.setStatut(StatutCommande.CONFIRMEE);
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        // When & Then
        assertThatThrownBy(() -> commandeService.deleteCommande(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("brouillon");
    }
}

