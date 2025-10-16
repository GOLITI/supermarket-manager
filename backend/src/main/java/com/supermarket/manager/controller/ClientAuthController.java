package com.supermarket.manager.controller;

import com.supermarket.manager.model.auth.Role;
import com.supermarket.manager.model.auth.User;
import com.supermarket.manager.repository.RoleRepository;
import com.supermarket.manager.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Inscription Client", description = "API pour l'inscription des clients")
public class ClientAuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Inscription d'un nouveau client")
    public ResponseEntity<?> registerClient(@Valid @RequestBody ClientRegisterRequest signUpRequest) {
        try {
            // Vérifications
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Erreur: Ce nom d'utilisateur existe déjà!"));
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Erreur: Cet email est déjà utilisé!"));
            }

            // Créer le client
            User client = User.builder()
                    .username(signUpRequest.getUsername())
                    .email(signUpRequest.getEmail())
                    .password(passwordEncoder.encode(signUpRequest.getPassword()))
                    .firstName(signUpRequest.getFirstName())
                    .lastName(signUpRequest.getLastName())
                    .phoneNumber(signUpRequest.getPhoneNumber())
                    .active(true)
                    .build();

            // Attribuer automatiquement le rôle CLIENT
            Role clientRole = roleRepository.findByName(Role.RoleName.ROLE_CLIENT)
                    .orElseThrow(() -> new RuntimeException("Erreur: Rôle CLIENT non trouvé."));

            Set<Role> roles = new HashSet<>();
            roles.add(clientRole);
            client.setRoles(roles);

            userRepository.save(client);

            log.info("Nouveau client enregistré: {}", client.getUsername());

            return ResponseEntity.ok(new MessageResponse("Client enregistré avec succès! Vous pouvez maintenant vous connecter."));

        } catch (Exception e) {
            log.error("Erreur lors de l'inscription client: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Erreur lors de l'inscription: " + e.getMessage()));
        }
    }

    // DTO pour l'inscription client
    @Data
    public static class ClientRegisterRequest {
        @NotBlank
        @Size(min = 3, max = 50)
        private String username;

        @NotBlank
        @Email
        @Size(max = 100)
        private String email;

        @NotBlank
        @Size(min = 6, max = 100)
        private String password;

        @NotBlank
        @Size(max = 100)
        private String firstName;

        @NotBlank
        @Size(max = 100)
        private String lastName;

        @Size(max = 20)
        private String phoneNumber;

        private String address;
        private String city;
        private String postalCode;
    }

    @Data
    @lombok.AllArgsConstructor
    public static class MessageResponse {
        private String message;
    }
}