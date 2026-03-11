package com.example.WaterSupplyBillingUsageTrackerSystem.service;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Payment;
import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Bill;
import com.example.WaterSupplyBillingUsageTrackerSystem.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Payment Service
 * Demonstrates:
 * - Pagination support with Pageable
 * - Sorting functionality
 * - existBy() methods for existence checking
 */
@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Get all payments with pagination
     * Requirement: Implementation of Pagination
     */
    public Page<Payment> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }

    /**
     * Get all payments without pagination (legacy)
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    /**
     * Get all payments sorted
     * Requirement: Implementation of Sorting functionality
     */
    public List<Payment> getAllPaymentsSorted(Sort sort) {
        return paymentRepository.findAll(sort);
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    /**
     * Get payments by bill with pagination
     */
    public Page<Payment> getPaymentsByBillId(Long billId, Pageable pageable) {
        return paymentRepository.findByBillId(billId, pageable);
    }

    /**
     * Get payments by method with pagination
     */
    public Page<Payment> getPaymentsByMethod(String paymentMethod, Pageable pageable) {
        return paymentRepository.findByPaymentMethod(paymentMethod, pageable);
    }

    /**
     * existBy() method - Check if payment exists for bill
     */
    public boolean paymentExistsForBill(Long billId) {
        return paymentRepository.existsByBillId(billId);
    }

    public Payment savePayment(Payment payment) {
        // Validate payment amount
        if (payment.getAmountPaid() <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive.");
        }
        Bill bill = payment.getBill();
        if (bill == null) {
            throw new IllegalArgumentException("Payment must be linked to a bill.");
        }
        if (payment.getAmountPaid() > bill.getAmount()) {
            throw new IllegalArgumentException("Payment amount cannot exceed bill amount.");
        }
        // Update bill status if fully paid
        if (payment.getAmountPaid() == bill.getAmount()) {
            bill.setStatus("PAID");
        }
        return paymentRepository.save(payment);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}
