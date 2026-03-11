package com.example.WaterSupplyBillingUsageTrackerSystem.controller;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.User;
import com.example.WaterSupplyBillingUsageTrackerSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User Controller
 * Demonstrates:
 * - User management with roles (Many-to-Many relationship)
 * - existBy() methods for duplicate checking
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            userService.saveUser(user);
            return ResponseEntity.ok("User saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage() + " | Caused by: " + (e.getCause() != null ? e.getCause().getMessage() : "unknown"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser != null) {
            return ResponseEntity.ok("User updated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if username already exists (existBy demonstration)
     */
    @GetMapping("/check/username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        return ResponseEntity.ok(userService.userExistsByUsername(username));
    }

    /**
     * Check if email already exists (existBy demonstration)
     */
    @GetMapping("/check/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        return ResponseEntity.ok(userService.userExistsByEmail(email));
    }

    /**
     * Requirement 8: Retrieve all users from a given province using province name
     */
    @GetMapping("/province/name/{provinceName}")
    public List<User> getUsersByProvinceName(@PathVariable String provinceName) {
        return userService.getUsersByProvinceName(provinceName);
    }

    /**
     * Requirement 8: Retrieve all users from a given province using province code
     */
    @GetMapping("/province/code/{provinceCode}")
    public List<User> getUsersByProvinceCode(@PathVariable String provinceCode) {
        return userService.getUsersByProvinceCode(provinceCode);
    }
}
