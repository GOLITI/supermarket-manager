package com.supermarket.manager.service;

import com.supermarket.manager.model.caisse.Transaction;
import com.supermarket.manager.model.dto.*;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    TransactionDTO creerTransaction(NouvelleTransactionRequest request);
    TransactionDTO obtenirTransaction(Long id);
    TransactionDTO obtenirParNumeroTransaction(String numeroTransaction);
    List<TransactionDTO> obtenirToutesLesTransactions();
    List<TransactionDTO> obtenirTransactionsPeriode(LocalDate debut, LocalDate fin);
    List<TransactionDTO> obtenirTransactionsParCarte(Long carteFideliteId);
    TransactionDTO finaliserTransaction(Long id);
    TransactionDTO annulerTransaction(Long id);
    RapportVentesDTO genererRapportJournalier(LocalDate date);
    RapportVentesDTO genererRapportPeriode(LocalDate debut, LocalDate fin);
    Transaction convertirVersEntite(TransactionDTO dto);
    TransactionDTO convertirVersDTO(Transaction entite);
}
