package com.example.WaterSupplyBillingUsageTrackerSystem.component;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Role;
import com.example.WaterSupplyBillingUsageTrackerSystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Data Seeder
 * Runs automatically on application startup to insert predefined data.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        seedRoles();
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            List<Role> roles = Arrays.asList(
                    new Role("ADMIN", "Administrator with full access"),
                    new Role("MANAGER", "Manager with administrative privileges"),
                    new Role("CUSTOMER", "Standard customer role")
            );
            roleRepository.saveAll(roles);
            System.out.println("✅ Predefined roles seeded into the database.");
        }
    }
}
