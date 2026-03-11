package com.example.WaterSupplyBillingUsageTrackerSystem.service;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Role;

import com.example.WaterSupplyBillingUsageTrackerSystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Role Service
 * NOTE: Roles are predefined (ADMIN, MANAGER, CUSTOMER) and cannot be created
 * or deleted.
 * Only read operations are allowed.
 */
@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Get all available roles
     */
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Get role by ID
     */
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    /**
     * Get role by name
     */
    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    /**
     * Check if role exists by name
     */
    public boolean roleExists(String name) {
        return roleRepository.existsByName(name);
    }

    /**
     * Get available RoleEnum values
     * Returns the predefined roles: ADMIN, MANAGER, CUSTOMER
     */
    public List<String> getAvailableRoles() {
        return Arrays.asList("ADMIN", "MANAGER", "CUSTOMER");
    }

    /**
     * Create role - RESTRICTED
     * Roles are predefined and cannot be created at runtime
     */
    public Role saveRole(Role role) {
        throw new UnsupportedOperationException(
                "Roles are predefined in the system and cannot be created. Available roles: ADMIN, MANAGER, CUSTOMER");
    }

    /**
     * Delete role - RESTRICTED
     * Roles are predefined and cannot be deleted at runtime
     */
    public void deleteRole(Long id) {
        throw new UnsupportedOperationException("Roles are predefined in the system and cannot be deleted.");
    }
}
