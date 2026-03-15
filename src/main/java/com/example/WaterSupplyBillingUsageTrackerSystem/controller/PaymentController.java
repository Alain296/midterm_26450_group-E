package com.example.WaterSupplyBillingUsageTrackerSystem.controller;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Payment;
import com.example.WaterSupplyBillingUsageTrackerSystem.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Payment Controller
 * Demonstrates:
 * - Pagination support
 * - Sorting functionality
 * - existBy() method usage
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    /**
     * Get all payments with pagination
     * Example: /api/payments/paginated?page=0&size=10
     */
    @GetMapping("/paginated")
    public Page<Payment> getAllPaymentsPageable(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return paymentService.getAllPayments(pageable);
    }

    /**
     * Get all payments sorted
     */
    @GetMapping("/sorted")
    public List<Payment> getAllPaymentsSorted(
            @RequestParam(value = "sortBy", defaultValue = "paymentDate") String sortBy,
            @RequestParam(value = "order", defaultValue = "desc") String order) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        return paymentService.getAllPaymentsSorted(sort);
    }

    /**
     * Get all payments without pagination (legacy)
     */
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get payments by bill with pagination
     */
    @GetMapping("/bill/{billId}")
    public Page<Payment> getPaymentsByBillId(
            @PathVariable Long billId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return paymentService.getPaymentsByBillId(billId, pageable);
    }

    /**
     * Get payments by method with pagination
     */
    @GetMapping("/method/{paymentMethod}")
    public Page<Payment> getPaymentsByMethod(
            @PathVariable String paymentMethod,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return paymentService.getPaymentsByMethod(paymentMethod, pageable);
    }

    @PostMapping
    public ResponseEntity<String> createPayment(@Valid @RequestBody Payment payment) {
        paymentService.savePayment(payment);
        return ResponseEntity.ok("Payment saved successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if payment exists for bill (existBy demonstration)
     */
    @GetMapping("/check/{billId}")
    public ResponseEntity<Boolean> checkPaymentExists(@PathVariable Long billId) {
        return ResponseEntity.ok(paymentService.paymentExistsForBill(billId));
    }
}
