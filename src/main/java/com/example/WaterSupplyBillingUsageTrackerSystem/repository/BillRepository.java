package com.example.WaterSupplyBillingUsageTrackerSystem.repository;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Bill Repository
 * Demonstrates:
 * - Pagination support
 * - existBy() methods for existence checking
 */
@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    
    /**
     * Pagination support - returns a Page with bills
     */
    Page<Bill> findAll(Pageable pageable);
    
    /**
     * existBy() method for checking bill existence
     */
    boolean existsByCustomerIdAndUsageId(Long customerId, Long usageId);
    
    List<Bill> findByCustomerId(Long customerId);
    Page<Bill> findByCustomerId(Long customerId, Pageable pageable);
    
    List<Bill> findByStatus(String status);
    Page<Bill> findByStatus(String status, Pageable pageable);
}
