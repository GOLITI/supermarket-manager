package com.supermarket.manager.controller;
import com.supermarket.manager.model.dto.*;
import com.supermarket.manager.model.client.SegmentClient;
import com.supermarket.manager.model.client.StatutClient;
import com.supermarket.manager.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ClientController {
    private final ClientService clientService;
    @PostMapping
    public ResponseEntity<ClientDTO> creerClient(@Valid @RequestBody ClientRequest request) {
        ClientDTO client = clientService.creerClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        ClientDTO client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }
    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        List<ClientDTO> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }
    @GetMapping("/{id}/fidelite")
    public ResponseEntity<FideliteInfoDTO> getFideliteInfo(@PathVariable Long id) {
        FideliteInfoDTO info = clientService.getFideliteInfo(id);
        return ResponseEntity.ok(info);
    }
    @PostMapping("/{id}/points/ajouter")
    public ResponseEntity<ClientDTO> ajouterPoints(@PathVariable Long id, @RequestParam Integer points) {
        ClientDTO client = clientService.ajouterPoints(id, points, null, null);
        return ResponseEntity.ok(client);
    }
    @GetMapping("/{id}/achats")
    public ResponseEntity<List<HistoriqueAchatDTO>> getHistoriqueAchats(@PathVariable Long id) {
        List<HistoriqueAchatDTO> historique = clientService.getHistoriqueAchats(id);
        return ResponseEntity.ok(historique);
    }
}
