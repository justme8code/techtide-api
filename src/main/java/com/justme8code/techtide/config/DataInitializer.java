package com.justme8code.techtide.config;

import com.justme8code.techtide.models.Role;
import com.justme8code.techtide.models.User;
import com.justme8code.techtide.repositories.RoleRepository;
import com.justme8code.techtide.repositories.UserRepository;
import com.justme8code.techtide.util.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_NAME = "techtider";

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void run(String... args) {
        // Ensure ADMIN role exists
        Role adminRole = roleRepository.findRoleByUserRole(UserRole.ROLE_ADMIN)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setUserRole(UserRole.ROLE_ADMIN);
                    return roleRepository.save(newRole);
                });

        // Check if the admin user exists
        Optional<User> existingAdmin = userRepository.findByUsername( ADMIN_NAME);

        if (existingAdmin.isPresent()) {
            User admin = existingAdmin.get();

            // Check if the user already has the ADMIN role
            if (!admin.getRoles().contains(adminRole)) {
                admin.getRoles().add(adminRole);
                userRepository.save(admin); // Save only if roles were modified
            }

        } else {
            // Create new admin user if not exists
            User admin = new User();
            admin.setUsername(ADMIN_NAME);
            admin.setPassword(passwordEncoder.encode(ADMIN_NAME)); // Secure the password
            admin.setRoles(new HashSet<>()); // Initialize roles set
            admin.getRoles().add(adminRole);
            userRepository.save(admin);
        }
    }
}
