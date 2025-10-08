package comcom.supermarket.manager.controller;
import comcom.supermarket.manager.model.dto.*;
import comcom.supermarket.manager.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CaisseController {
    private final TransactionService transactionService;
    @PostMapping
    public ResponseEntity<TransactionDTO> creerTransaction(@Valid @RequestBody NouvelleTransactionRequest request) {
        log.info("POST /api/transactions");
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.creerTransaction(request));
    }
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> obtenirTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.obtenirTransaction(id));
    }
    @GetMapping("/numero/{numeroTransaction}")
    public ResponseEntity<TransactionDTO> obtenirParNumero(@PathVariable String numeroTransaction) {
        return ResponseEntity.ok(transactionService.obtenirParNumeroTransaction(numeroTransaction));
    }
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> obtenirToutesLesTransactions() {
        return ResponseEntity.ok(transactionService.obtenirToutesLesTransactions());
    }
    @GetMapping("/periode")
    public ResponseEntity<List<TransactionDTO>> obtenirTransactionsPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(transactionService.obtenirTransactionsPeriode(debut, fin));
    }
    @GetMapping("/carte/{carteFideliteId}")
    public ResponseEntity<List<TransactionDTO>> obtenirTransactionsParCarte(@PathVariable Long carteFideliteId) {
        return ResponseEntity.ok(transactionService.obtenirTransactionsParCarte(carteFideliteId));
    }
    @PatchMapping("/{id}/annuler")
    public ResponseEntity<TransactionDTO> annulerTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.annulerTransaction(id));
    }
    @GetMapping("/rapports/journalier")
    public ResponseEntity<RapportVentesDTO> genererRapportJournalier(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(transactionService.genererRapportJournalier(date));
    }
    @GetMapping("/rapports/periode")
    public ResponseEntity<RapportVentesDTO> genererRapportPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(transactionService.genererRapportPeriode(debut, fin));
    }
}
