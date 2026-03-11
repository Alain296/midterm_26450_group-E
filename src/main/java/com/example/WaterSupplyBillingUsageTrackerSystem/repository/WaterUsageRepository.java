package com.example.WaterSupplyBillingUsageTrackerSystem.repository;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.WaterUsage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WaterUsage Repository
 * Demonstrates:
 * - Pagination support with Pageable
 * - existBy() methods for existence checking
 */
@Repository
public interface WaterUsageRepository extends JpaRepository<WaterUsage, Long> {
    
    /**
     * existBy() method demonstrates existence checking
     * More efficient than findBy() as it returns boolean without loading full entity
     */
    boolean existsByCustomerIdAndMonthAndYear(Long customerId, int month, int year);
    
    /**
     * Pagination support
     */
    Page<WaterUsage> findAll(Pageable pageable);
    
    List<WaterUsage> findByCustomerId(Long customerId);
    Page<WaterUsage> findByCustomerId(Long customerId, Pageable pageable);
    
    Page<WaterUsage> findByYearOrderByMonthDesc(int year, Pageable pageable);
}
