package com.example.WaterSupplyBillingUsageTrackerSystem.repository;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Payment Repository
 * Demonstrates:
 * - Pagination support
 * - existBy() methods for existence checking
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    /**
     * Pagination support
     */
    Page<Payment> findAll(Pageable pageable);
    
    /**
     * existBy() method for checking payment existence
     */
    boolean existsByBillId(Long billId);
    
    List<Payment> findByBillId(Long billId);
    Page<Payment> findByBillId(Long billId, Pageable pageable);
    
    Page<Payment> findByPaymentMethod(String paymentMethod, Pageable pageable);
}
