package com.example.WaterSupplyBillingUsageTrackerSystem.controller;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Customer;
import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Location;
import com.example.WaterSupplyBillingUsageTrackerSystem.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.LinkedHashMap;

/**
 * Customer Controller - Location-Based Queries
 * Demonstrates:
 * - Code-based location queries (hierarchical)
 * - Location type-based queries (PROVINCE, SECTOR, VILLAGE)
 * - Province-based customer retrieval (legacy)
 * - existBy() method usage for duplicate checking
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    /**
     * Get all customers
     */
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customerOpt = customerService.getCustomerById(id);
        if (!customerOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Customer customer = customerOpt.get();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "Customer retrieved successfully");
        response.put("id", customer.getId());
        response.put("name", customer.getName());
        response.put("email", customer.getEmail());
        response.put("phone", customer.getPhone());
        
        if (customer.getLocation() != null) {
            Location loc = customer.getLocation();
            Map<String, Object> locationMap = new LinkedHashMap<>();
            locationMap.put("id", loc.getId());
            locationMap.put("name", loc.getName());
            locationMap.put("type", loc.getType());
            locationMap.put("code", loc.getCode());
            locationMap.put("country", loc.getCountry());
            response.put("location", locationMap);
            
            // Add full location hierarchy path
            List<Map<String, String>> path = new ArrayList<>();
            Location current = loc;
            while (current != null) {
                Map<String, String> locInfo = new LinkedHashMap<>();
                locInfo.put("name", current.getName());
                locInfo.put("type", current.getType().toString());
                locInfo.put("code", current.getCode());
                path.add(0, locInfo);
                current = current.getParent();
            }
            response.put("locationHierarchy", path);
        }
        
        return ResponseEntity.ok(response);
    }

    // ===== CODE-BASED LOCATION QUERIES =====
    
    /**
     * Get customer(s) by exact location code
     * Example code: RW-KGL-GB-GSZ-MSZ-GV (Gasave village)
     * Usage: GET /api/customers/by-location-code/RW-KGL-GB-GSZ-MSZ-GV
     */
    @GetMapping("/by-location-code/{code}")
    public List<Customer> getCustomersByLocationCode(@PathVariable String code) {
        return customerService.getCustomersByLocationCode(code);
    }



    /**
     * Get all customers under a location hierarchy using code pattern
     * Example: Code pattern "RW-KGL-GB-GSZ" returns all customers in villages under Gisozi sector
     * Example: Code pattern "RW-KGL" returns all customers in villages under Kigali province
     * Usage: GET /api/customers/by-location-code-pattern/RW-KGL-GB-GSZ
     */
    @GetMapping("/by-location-code-pattern/{codePattern}")
    public List<Customer> getCustomersByLocationCodePattern(@PathVariable String codePattern) {
        return customerService.getCustomersByLocationCodePattern(codePattern);
    }



    /**
     * Get all customers in a province by name
     * Example: /api/customers/by-province-name/Kigali
     */
    @GetMapping("/by-province-name/{provinceName}")
    public ResponseEntity<List<Customer>> getCustomersInProvince(@PathVariable String provinceName) {
        return ResponseEntity.ok(customerService.getCustomersInProvince(provinceName));
    }

    /**
     * Get all customers in a sector by name
     * Example: /api/customers/by-sector-name/Gisozi
     */
    @GetMapping("/by-sector-name/{sectorName}")
    public ResponseEntity<List<Customer>> getCustomersInSector(@PathVariable String sectorName) {
        return ResponseEntity.ok(customerService.getCustomersInSector(sectorName));
    }

    /**
     * Get all customers in a village by name
     * Example: /api/customers/by-village-name/Gasave
     */
    @GetMapping("/by-village-name/{villageName}")
    public ResponseEntity<List<Customer>> getCustomersInVillage(@PathVariable String villageName) {
        return ResponseEntity.ok(customerService.getCustomersInVillage(villageName));
    }


    // ===== CRUD OPERATIONS =====
    
    @PostMapping("/save")
    public ResponseEntity<String> saveCustomerWithMessage(@Valid @RequestBody Customer customer) {
        customerService.saveCustomer(customer);
        return ResponseEntity.ok("Customer saved successfully");
    }

    @PostMapping
    public ResponseEntity<String> createCustomer(@Valid @RequestBody Customer customer) {
        customerService.saveCustomer(customer);
        return ResponseEntity.ok("Customer saved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        if (updatedCustomer != null) {
            return ResponseEntity.ok("Customer updated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    // ===== EXISTENCE CHECKS =====
    
    /**
     * Check if email already exists (existBy demonstration)
     */
    @GetMapping("/check/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        return ResponseEntity.ok(customerService.existsByEmail(email));
    }

    /**
     * Check if phone already exists (existBy demonstration)
     */
    @GetMapping("/check/phone/{phone}")
    public ResponseEntity<Boolean> checkPhoneExists(@PathVariable String phone) {
        return ResponseEntity.ok(customerService.existsByPhone(phone));
    }
}


