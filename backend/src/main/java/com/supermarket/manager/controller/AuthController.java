package com.supermarket.manager.controller;

import com.supermarket.manager.config.JwtUtils;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentification", description = "API pour l'authentification et l'enregistrement")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(summary = "Connexion utilisateur")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateJwtToken(authentication);
            String refreshToken = jwtUtils.generateRefreshToken(
                    loginRequest.getUsername()
            );

            User user = (User) authentication.getPrincipal();

            // Mettre à jour le dernier login
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            List<String> roles = user.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            log.info("Connexion réussie pour: {}", loginRequest.getUsername());

            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    refreshToken,
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    roles
            ));
        } catch (Exception e) {
            log.error("Erreur de connexion: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Identifiants invalides"));
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Enregistrement d'un nouvel utilisateur")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Erreur: Ce nom d'utilisateur existe déjà!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Erreur: Cet email est déjà utilisé!"));
        }

        // Créer le nouvel utilisateur
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .active(true)
                .build();

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            // Par défaut, attribuer le rôle CLIENT
            Role clientRole = roleRepository.findByName(Role.RoleName.ROLE_CLIENT)
                    .orElseThrow(() -> new RuntimeException("Erreur: Rôle non trouvé."));
            roles.add(clientRole);
        } else {
            strRoles.forEach(role -> {
                try {
                    Role.RoleName roleName = Role.RoleName.valueOf(role);
                    Role userRole = roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Erreur: Rôle " + role + " non trouvé."));
                    roles.add(userRole);
                } catch (IllegalArgumentException e) {
                    log.error("Rôle invalide: {}", role);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        log.info("Nouvel utilisateur enregistré: {}", user.getUsername());

        return ResponseEntity.ok(new MessageResponse("Utilisateur enregistré avec succès!"));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Rafraîchir le token JWT")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (jwtUtils.validateJwtToken(refreshToken)) {
            String username = jwtUtils.getUsernameFromJwtToken(refreshToken);
            String newAccessToken = jwtUtils.generateTokenFromUsername(username);

            log.info("Token rafraîchi pour: {}", username);

            return ResponseEntity.ok(new RefreshTokenResponse(
                    newAccessToken,
                    refreshToken
            ));
        }

        return ResponseEntity.badRequest()
                .body(new MessageResponse("Refresh token invalide"));
    }

    @PostMapping("/logout")
    @Operation(summary = "Déconnexion utilisateur")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        log.info("Utilisateur déconnecté");
        return ResponseEntity.ok(new MessageResponse("Déconnexion réussie"));
    }

    // DTOs
    @Data
    public static class LoginRequest {
        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

    @Data
    public static class RegisterRequest {
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

        private Set<String> roles;
    }

    @Data
    public static class RefreshTokenRequest {
        @NotBlank
        private String refreshToken;
    }

    @Data
    @lombok.AllArgsConstructor
    public static class JwtResponse {
        private String token;
        private String refreshToken;
        private String type = "Bearer";
        private Long id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private List<String> roles;

        public JwtResponse(String token, String refreshToken, Long id,
                           String username, String email, String firstName,
                           String lastName, List<String> roles) {
            this.token = token;
            this.refreshToken = refreshToken;
            this.id = id;
            this.username = username;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.roles = roles;
        }
    }

    @Data
    @lombok.AllArgsConstructor
    public static class RefreshTokenResponse {
        private String accessToken;
        private String refreshToken;
        private String type = "Bearer";

        public RefreshTokenResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    @Data
    @lombok.AllArgsConstructor
    public static class MessageResponse {
        private String message;
    }
}