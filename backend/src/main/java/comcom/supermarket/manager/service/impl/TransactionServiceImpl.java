package comcom.supermarket.manager.service.impl;
import comcom.supermarket.manager.exception.BusinessException;
import comcom.supermarket.manager.exception.ResourceNotFoundException;
import comcom.supermarket.manager.exception.StockInsuffisantException;
import comcom.supermarket.manager.model.caisse.*;
import comcom.supermarket.manager.model.dto.*;
import comcom.supermarket.manager.model.produit.Produit;
import comcom.supermarket.manager.model.produit.Stock;
import comcom.supermarket.manager.repository.*;
import comcom.supermarket.manager.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final LigneTransactionRepository ligneTransactionRepository;
    private final CarteFideliteRepository carteFideliteRepository;
    private final PromotionRepository promotionRepository;
    private final ProduitRepository produitRepository;
    private final StockRepository stockRepository;
    @Override
    public TransactionDTO creerTransaction(NouvelleTransactionRequest request) {
        log.info("Création transaction");
        Transaction transaction = new Transaction();
        transaction.setNumeroTransaction(genererNumeroTransaction());
        transaction.setDateHeure(LocalDateTime.now());
        transaction.setMethodePaiement(request.getMethodePaiement());
        transaction.setMontantPaye(request.getMontantPaye());
        transaction.setCaissierId(request.getCaissierId());
        transaction.setRemarques(request.getRemarques());
        transaction.setStatut(StatutTransaction.EN_COURS);
        if (request.getNumeroCarteFidelite() != null && !request.getNumeroCarteFidelite().isEmpty()) {
            CarteFidelite carte = carteFideliteRepository.findByNumeroCarte(request.getNumeroCarteFidelite())
                .orElseThrow(() -> new ResourceNotFoundException("Carte non trouvée"));
            if (!carte.isValide()) {
                throw new BusinessException("Carte invalide ou expirée");
            }
            transaction.setCarteFidelite(carte);
        }
        for (ArticleTransactionRequest article : request.getArticles()) {
            Produit produit = produitRepository.findById(article.getProduitId())
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé"));
            List<Stock> stocks = stockRepository.findByProduitId(article.getProduitId());
            if (stocks.isEmpty()) {
                throw new ResourceNotFoundException("Stock non trouvé pour le produit ID: " + article.getProduitId());
            }
            Stock stock = stocks.get(0);
            if (stock.getQuantite() < article.getQuantite()) {
                throw new StockInsuffisantException("Stock insuffisant");
            }
            LigneTransaction ligne = new LigneTransaction();
            ligne.setProduit(produit);
            ligne.setQuantite(article.getQuantite());
            ligne.setPrixUnitaire(produit.getPrix());
            ligne.setRemiseLigne(BigDecimal.ZERO);
            List<Promotion> promosProduit = promotionRepository.findPromotionsValidesParProduit(produit.getId(), LocalDate.now());
            if (!promosProduit.isEmpty()) {
                Promotion meilleur = promosProduit.stream()
                    .max(Comparator.comparing(p -> p.calculerRemise(ligne.getPrixUnitaire().multiply(new BigDecimal(ligne.getQuantite())))))
                    .orElse(null);
                if (meilleur != null) {
                    ligne.appliquerPromotion(meilleur);
                }
            }
            transaction.ajouterLigne(ligne);
            stock.diminuerQuantite(article.getQuantite());
            stockRepository.save(stock);
        }
        if (request.getCodesPromotion() != null && !request.getCodesPromotion().isEmpty()) {
            for (String code : request.getCodesPromotion()) {
                Promotion promo = promotionRepository.findByCode(code)
                    .orElseThrow(() -> new ResourceNotFoundException("Promotion non trouvée"));
                if (!promo.isValide()) {
                    throw new BusinessException("Promotion invalide");
                }
                transaction.appliquerPromotion(promo);
            }
        }
        if (transaction.getCarteFidelite() != null) {
            BigDecimal remiseFidelite = transaction.getCarteFidelite().getNiveau().calculerRemise(transaction.getMontantBrut());
            transaction.setMontantRemises(transaction.getMontantRemises().add(remiseFidelite));
        }
        transaction.recalculerMontants();
        transaction.finaliser();
        return convertirVersDTO(transactionRepository.save(transaction));
    }
    @Override
    @Transactional(readOnly = true)
    public TransactionDTO obtenirTransaction(Long id) {
        return convertirVersDTO(transactionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction non trouvée")));
    }
    @Override
    @Transactional(readOnly = true)
    public TransactionDTO obtenirParNumeroTransaction(String numeroTransaction) {
        return convertirVersDTO(transactionRepository.findByNumeroTransaction(numeroTransaction)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction non trouvée")));
    }
    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> obtenirToutesLesTransactions() {
        return transactionRepository.findAll().stream().map(this::convertirVersDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> obtenirTransactionsPeriode(LocalDate debut, LocalDate fin) {
        return transactionRepository.findTransactionsPeriode(debut.atStartOfDay(), fin.atTime(23,59,59), StatutTransaction.COMPLETEE)
            .stream().map(this::convertirVersDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> obtenirTransactionsParCarte(Long carteFideliteId) {
        return transactionRepository.findByCarteFideliteId(carteFideliteId).stream()
            .map(this::convertirVersDTO).collect(Collectors.toList());
    }
    @Override
    public TransactionDTO finaliserTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction non trouvée"));
        if (transaction.getStatut() != StatutTransaction.EN_COURS) {
            throw new BusinessException("Transaction ne peut pas être finalisée");
        }
        transaction.finaliser();
        return convertirVersDTO(transactionRepository.save(transaction));
    }
    @Override
    public TransactionDTO annulerTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction non trouvée"));
        if (transaction.getStatut() == StatutTransaction.ANNULEE) {
            throw new BusinessException("Transaction déjà annulée");
        }
        for (LigneTransaction ligne : transaction.getLignes()) {
            List<Stock> stocks = stockRepository.findByProduitId(ligne.getProduit().getId());
            if (!stocks.isEmpty()) {
                Stock stock = stocks.get(0);
                stock.augmenterQuantite(ligne.getQuantite());
                stockRepository.save(stock);
            }
        }
        transaction.annuler();
        return convertirVersDTO(transactionRepository.save(transaction));
    }
    @Override
    @Transactional(readOnly = true)
    public RapportVentesDTO genererRapportJournalier(LocalDate date) {
        return genererRapportPeriode(date, date);
    }
    @Override
    @Transactional(readOnly = true)
    public RapportVentesDTO genererRapportPeriode(LocalDate debut, LocalDate fin) {
        LocalDateTime dateDebut = debut.atStartOfDay();
        LocalDateTime dateFin = fin.atTime(23,59,59);
        RapportVentesDTO rapport = new RapportVentesDTO();
        rapport.setDate(debut);
        Long nbTransactions = transactionRepository.countTransactionsCompletees(dateDebut, dateFin);
        rapport.setNombreTransactions(nbTransactions != null ? nbTransactions.intValue() : 0);
        BigDecimal montantTotal = transactionRepository.sumMontantNetPeriode(dateDebut, dateFin);
        BigDecimal montantRemises = transactionRepository.sumMontantRemisesPeriode(dateDebut, dateFin);
        rapport.setMontantNetVentes(montantTotal != null ? montantTotal : BigDecimal.ZERO);
        rapport.setMontantTotalRemises(montantRemises != null ? montantRemises : BigDecimal.ZERO);
        rapport.setMontantTotalVentes(rapport.getMontantNetVentes().add(rapport.getMontantTotalRemises()));
        if (rapport.getNombreTransactions() > 0) {
            rapport.setMontantMoyen(rapport.getMontantNetVentes().divide(new BigDecimal(rapport.getNombreTransactions()), 2, RoundingMode.HALF_UP));
        } else {
            rapport.setMontantMoyen(BigDecimal.ZERO);
        }
        if (rapport.getMontantTotalVentes().compareTo(BigDecimal.ZERO) > 0) {
            rapport.setTauxRemise(rapport.getMontantTotalRemises().divide(rapport.getMontantTotalVentes(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
        } else {
            rapport.setTauxRemise(BigDecimal.ZERO);
        }
        Long totalArticles = ligneTransactionRepository.totalArticlesVendus(dateDebut, dateFin);
        rapport.setNombreArticlesVendus(totalArticles != null ? totalArticles.intValue() : 0);
        List<Object[]> produitsVendus = ligneTransactionRepository.produitsLesPlusVendus(dateDebut, dateFin);
        List<ProduitVenduDTO> produitsDTO = new ArrayList<>();
        for (Object[] row : produitsVendus) {
            ProduitVenduDTO dto = new ProduitVenduDTO();
            dto.setProduitId(((Number) row[0]).longValue());
            dto.setNomProduit((String) row[1]);
            dto.setCodeBarre((String) row[2]);
            dto.setQuantiteVendue(((Number) row[3]).intValue());
            dto.setMontantTotal((BigDecimal) row[4]);
            dto.setNombreTransactions(((Number) row[5]).intValue());
            produitsDTO.add(dto);
        }
        rapport.setProduitsLesPlusVendus(produitsDTO);
        Long cartesUtilisees = transactionRepository.countCartesUtilisees(dateDebut, dateFin);
        rapport.setNombreCartesUtilisees(cartesUtilisees != null ? cartesUtilisees.intValue() : 0);
        return rapport;
    }
    @Override
    public Transaction convertirVersEntite(TransactionDTO dto) {
        if (dto == null) return null;
        Transaction entite = new Transaction();
        entite.setId(dto.getId());
        entite.setNumeroTransaction(dto.getNumeroTransaction());
        entite.setDateHeure(dto.getDateHeure());
        entite.setMontantBrut(dto.getMontantBrut());
        entite.setMontantRemises(dto.getMontantRemises());
        entite.setMontantNet(dto.getMontantNet());
        entite.setMontantPaye(dto.getMontantPaye());
        entite.setMontantRendu(dto.getMontantRendu());
        entite.setMethodePaiement(dto.getMethodePaiement());
        entite.setStatut(dto.getStatut());
        return entite;
    }
    @Override
    public TransactionDTO convertirVersDTO(Transaction entite) {
        if (entite == null) return null;
        TransactionDTO dto = new TransactionDTO();
        dto.setId(entite.getId());
        dto.setNumeroTransaction(entite.getNumeroTransaction());
        dto.setDateHeure(entite.getDateHeure());
        dto.setCarteFideliteId(entite.getCarteFidelite() != null ? entite.getCarteFidelite().getId() : null);
        dto.setNomClient(entite.getCarteFidelite() != null ? entite.getCarteFidelite().getPrenomClient() + " " + entite.getCarteFidelite().getNomClient() : null);
        dto.setMontantBrut(entite.getMontantBrut());
        dto.setMontantRemises(entite.getMontantRemises());
        dto.setMontantNet(entite.getMontantNet());
        dto.setMontantPaye(entite.getMontantPaye());
        dto.setMontantRendu(entite.getMontantRendu());
        dto.setMethodePaiement(entite.getMethodePaiement());
        dto.setStatut(entite.getStatut());
        dto.setPointsGagnes(entite.getPointsGagnes());
        dto.setCaissierId(entite.getCaissierId());
        dto.setRemarques(entite.getRemarques());
        List<LigneTransactionDTO> lignesDTO = entite.getLignes().stream().map(this::convertirLigneVersDTO).collect(Collectors.toList());
        dto.setLignes(lignesDTO);
        List<Long> promotionIds = entite.getPromotionsAppliquees().stream().map(Promotion::getId).collect(Collectors.toList());
        dto.setPromotionIds(promotionIds);
        return dto;
    }
    private LigneTransactionDTO convertirLigneVersDTO(LigneTransaction ligne) {
        LigneTransactionDTO dto = new LigneTransactionDTO();
        dto.setId(ligne.getId());
        dto.setProduitId(ligne.getProduit().getId());
        dto.setNomProduit(ligne.getProduit().getNom());
        dto.setCodeBarre(ligne.getProduit().getCodeBarre());
        dto.setQuantite(ligne.getQuantite());
        dto.setPrixUnitaire(ligne.getPrixUnitaire());
        dto.setRemiseLigne(ligne.getRemiseLigne());
        dto.setPromotionId(ligne.getPromotionAppliquee() != null ? ligne.getPromotionAppliquee().getId() : null);
        dto.setMontantTotal(ligne.getMontantTotal());
        return dto;
    }
    private String genererNumeroTransaction() {
        return "TRX-" + System.currentTimeMillis() + "-" + String.format("%04d", (int)(Math.random() * 10000));
    }
}
