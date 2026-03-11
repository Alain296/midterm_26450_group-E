package com.example.WaterSupplyBillingUsageTrackerSystem.controller;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.WaterUsage;
import com.example.WaterSupplyBillingUsageTrackerSystem.service.WaterUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Water Usage Controller
 * Demonstrates:
 * - Pagination support
 * - Sorting functionality
 * - existBy() method usage
 */
@RestController
@RequestMapping("/api/usage")
public class WaterUsageController {
    @Autowired
    private WaterUsageService waterUsageService;

    /**
     * Get all usages with pagination
     * Requirement: Pagination implementation
     * Example: /api/usage/paginated?page=0&size=10&sort=recordedDate,desc
     */
    @GetMapping("/paginated")
    public Page<WaterUsage> getAllUsagesPageable(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "recordedDate") String sort) {
        Sort sortOrder = Sort.by(sort).descending();
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return waterUsageService.getAllUsages(pageable);
    }

    /**
     * Get all usages sorted
     * Requirement: Sorting implementation
     * Example: /api/usage/sorted?sortBy=litersUsed&order=desc
     */
    @GetMapping("/sorted")
    public List<WaterUsage> getAllUsagesSorted(
            @RequestParam(value = "sortBy", defaultValue = "recordedDate") String sortBy,
            @RequestParam(value = "order", defaultValue = "desc") String order) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        return waterUsageService.getAllUsagesSorted(sort);
    }

    /**
     * Get all usages without pagination (legacy)
     */
    @GetMapping
    public List<WaterUsage> getAllUsages() {
        return waterUsageService.getAllUsages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaterUsage> getUsageById(@PathVariable Long id) {
        return waterUsageService.getUsageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get usages by customer with pagination
     */
    @GetMapping("/customer/{customerId}")
    public Page<WaterUsage> getUsagesByCustomerId(
            @PathVariable Long customerId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return waterUsageService.getUsagesByCustomerId(customerId, pageable);
    }

    /**
     * Get usages by year with pagination
     */
    @GetMapping("/year/{year}")
    public Page<WaterUsage> getUsagesByYear(
            @PathVariable int year,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return waterUsageService.getUsagesByYear(year, pageable);
    }

    @PostMapping
    public ResponseEntity<String> createUsage(@RequestBody WaterUsage usage) {
        try {
            waterUsageService.saveUsage(usage);
            return ResponseEntity.ok("Water usage recorded successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsage(@PathVariable Long id) {
        waterUsageService.deleteUsage(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if usage exists for customer, month and year (existBy demonstration)
     */
    @GetMapping("/check/{customerId}/{month}/{year}")
    public ResponseEntity<Boolean> checkUsageExists(
            @PathVariable Long customerId,
            @PathVariable int month,
            @PathVariable int year) {
        return ResponseEntity.ok(waterUsageService.usageExistsForCustomerMonth(customerId, month, year));
    }
}
