package com.supermarket.manager.config;

import com.supermarket.manager.model.auth.Role;
import com.supermarket.manager.model.auth.User;
import com.supermarket.manager.repository.RoleRepository;
import com.supermarket.manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createRoles();
        createAdminUser();
    }

    private void createRoles() {
        for (Role.RoleName roleName : Role.RoleName.values()) {
            if (!roleRepository.existsByName(roleName)) {
                Role role = new Role(roleName);
                roleRepository.save(role);
                System.out.println("✅ Rôle créé: " + roleName);
            }
        }
    }

    private void createAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            Role adminRole = roleRepository.findByName(Role.RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Rôle ADMIN non trouvé"));

            User admin = User.builder()
                    .username("admin")
                    .email("admin@supermarket.com")
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("Super")
                    .lastName("Admin")
                    .phoneNumber("+2250102030405")
                    .active(true)
                    .roles(Set.of(adminRole))
                    .build();

            userRepository.save(admin);
            System.out.println("✅ Admin créé: admin / admin123");
        }

        // Créer aussi un manager de test
        if (!userRepository.existsByUsername("manager")) {
            Role managerRole = roleRepository.findByName(Role.RoleName.ROLE_MANAGER)
                    .orElseThrow(() -> new RuntimeException("Rôle MANAGER non trouvé"));

            User manager = User.builder()
                    .username("manager")
                    .email("manager@supermarket.com")
                    .password(passwordEncoder.encode("manager123"))
                    .firstName("John")
                    .lastName("Manager")
                    .phoneNumber("+2250506070809")
                    .active(true)
                    .roles(Set.of(managerRole))
                    .build();

            userRepository.save(manager);
            System.out.println("✅ Manager créé: manager / manager123");
        }
    }
}