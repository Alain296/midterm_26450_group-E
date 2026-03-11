package com.example.WaterSupplyBillingUsageTrackerSystem.controller;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Role;
import com.example.WaterSupplyBillingUsageTrackerSystem.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Role Controller - Read-Only Access
 * NOTE: Roles are fixed in the system (ADMIN, MANAGER, CUSTOMER) and cannot be modified.
 * Only GET operations are allowed.
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Role> getRoleByName(@PathVariable String name) {
        return roleService.getRoleByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createRole(@RequestBody Role role) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Roles are predefined in the system and cannot be created. Available roles: ADMIN, MANAGER, CUSTOMER");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Roles are predefined in the system and cannot be deleted.");
    }

    @GetMapping("/check/{name}")
    public ResponseEntity<Boolean> checkRoleExists(@PathVariable String name) {
        return ResponseEntity.ok(roleService.roleExists(name));
    }
}
