package com.example.WaterSupplyBillingUsageTrackerSystem.controller;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Bill;
import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Customer;
import com.example.WaterSupplyBillingUsageTrackerSystem.domain.WaterUsage;
import com.example.WaterSupplyBillingUsageTrackerSystem.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Bill Controller
 * Demonstrates:
 * - Pagination support
 * - Sorting functionality
 * - existBy() method usage
 */
@RestController
@RequestMapping("/api/bills")
public class BillController {
    @Autowired
    private BillService billService;

    /**
     * Get all bills with pagination
     * Requirement: Pagination implementation
     * Example: /api/bills/paginated?page=0&size=10&sort=generatedDate,desc
     */
    @GetMapping("/paginated")
    public Page<Bill> getAllBillsPageable(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "generatedDate") String sort) {
        Sort sortOrder = Sort.by(sort).descending();
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return billService.getAllBills(pageable);
    }

    /**
     * Get all bills sorted
     * Requirement: Sorting implementation
     * Example: /api/bills/sorted?sortBy=amount&order=desc
     */
    @GetMapping("/sorted")
    public List<Bill> getAllBillsSorted(
            @RequestParam(value = "sortBy", defaultValue = "generatedDate") String sortBy,
            @RequestParam(value = "order", defaultValue = "desc") String order) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        return billService.getAllBillsSorted(sort);
    }

    /**
     * Get all bills without pagination (legacy)
     */
    @GetMapping
    public List<Bill> getAllBills() {
        return billService.getAllBills();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        return billService.getBillById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get bills by customer with pagination
     */
    @GetMapping("/customer/{customerId}")
    public Page<Bill> getBillsByCustomerId(
            @PathVariable Long customerId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return billService.getBillsByCustomerId(customerId, pageable);
    }

    /**
     * Get bills by status with pagination
     */
    @GetMapping("/status/{status}")
    public Page<Bill> getBillsByStatus(
            @PathVariable String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return billService.getBillsByStatus(status, pageable);
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateBill(@RequestBody BillRequest request) {
        Bill bill = billService.generateBill(request.getCustomer(), request.getUsage());
        return ResponseEntity.ok("Bill generated successfully");
    }

    @PostMapping
    public ResponseEntity<String> createBill(@RequestBody Bill bill) {
        Bill savedBill = billService.saveBill(bill);
        return ResponseEntity.ok("Bill saved successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if bill exists for customer and usage (existBy demonstration)
     */
    @GetMapping("/check/{customerId}/{usageId}")
    public ResponseEntity<Boolean> checkBillExists(
            @PathVariable Long customerId,
            @PathVariable Long usageId) {
        return ResponseEntity.ok(billService.billExistsForCustomerUsage(customerId, usageId));
    }

    // DTO for bill generation
    public static class BillRequest {
        private Customer customer;
        private WaterUsage usage;
        public Customer getCustomer() { return customer; }
        public void setCustomer(Customer customer) { this.customer = customer; }
        public WaterUsage getUsage() { return usage; }
        public void setUsage(WaterUsage usage) { this.usage = usage; }
    }
}
