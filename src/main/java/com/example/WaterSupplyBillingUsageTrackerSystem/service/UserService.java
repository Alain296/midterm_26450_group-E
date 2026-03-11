package com.example.WaterSupplyBillingUsageTrackerSystem.service;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Role;
import com.example.WaterSupplyBillingUsageTrackerSystem.domain.User;
import com.example.WaterSupplyBillingUsageTrackerSystem.repository.RoleRepository;
import com.example.WaterSupplyBillingUsageTrackerSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

/**
 * User Service
 * Demonstrates:
 * - Many-to-Many relationship with Role
 * - existBy() methods for existence checking
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public User saveUser(User user) {
        // Resolve roles by ID or name if provided in the request
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            List<Role> managedRoles = new ArrayList<>();
            for (Role roleReq : user.getRoles()) {
                if (roleReq.getId() != null) {
                    roleRepository.findById(roleReq.getId()).ifPresent(managedRoles::add);
                } else if (roleReq.getName() != null) {
                    roleRepository.findByName(roleReq.getName()).ifPresent(managedRoles::add);
                }
            }
            user.setRoles(managedRoles);
        }
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * existBy() method demonstration
     * These methods are more efficient than findBy() for existence checking
     * as they return just a boolean without loading the entire entity
     */
    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setActive(userDetails.isActive());
            user.setRoles(userDetails.getRoles());
            return userRepository.save(user);
        }).orElse(null);
    }

    /**
     * Requirement 8: Retrieve all users from a given province using province name
     */
    public List<User> getUsersByProvinceName(String provinceName) {
        return userRepository.findUsersByProvinceName(provinceName);
    }

    /**
     * Requirement 8: Retrieve all users from a given province using province code
     */
    public List<User> getUsersByProvinceCode(String provinceCode) {
        return userRepository.findUsersByProvinceCode(provinceCode);
    }
}
