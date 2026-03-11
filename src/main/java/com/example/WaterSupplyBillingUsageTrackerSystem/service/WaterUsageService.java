package com.example.WaterSupplyBillingUsageTrackerSystem.service;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.WaterUsage;
import com.example.WaterSupplyBillingUsageTrackerSystem.repository.WaterUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * WaterUsage Service
 * Demonstrates:
 * - Pagination support with Pageable
 * - Sorting functionality
 * - existBy() methods for existence checking
 */
@Service
public class WaterUsageService {
    @Autowired
    private WaterUsageRepository waterUsageRepository;

    /**
     * Get all usages with pagination
     * Requirement: Implementation of Pagination
     */
    public Page<WaterUsage> getAllUsages(Pageable pageable) {
        return waterUsageRepository.findAll(pageable);
    }

    /**
     * Get all usages without pagination (legacy)
     */
    public List<WaterUsage> getAllUsages() {
        return waterUsageRepository.findAll();
    }

    /**
     * Get all usages sorted
     * Requirement: Implementation of Sorting functionality
     */
    public List<WaterUsage> getAllUsagesSorted(Sort sort) {
        return waterUsageRepository.findAll(sort);
    }

    public Optional<WaterUsage> getUsageById(Long id) {
        return waterUsageRepository.findById(id);
    }

    /**
     * Get usages by customer with pagination
     */
    public Page<WaterUsage> getUsagesByCustomerId(Long customerId, Pageable pageable) {
        return waterUsageRepository.findByCustomerId(customerId, pageable);
    }

    /**
     * Get usages by year sorted by month descending
     */
    public Page<WaterUsage> getUsagesByYear(int year, Pageable pageable) {
        return waterUsageRepository.findByYearOrderByMonthDesc(year, pageable);
    }

    /**
     * existBy() method - Check if usage exists for customer, month and year
     * Prevents duplicate entries
     */
    public boolean usageExistsForCustomerMonth(Long customerId, int month, int year) {
        return waterUsageRepository.existsByCustomerIdAndMonthAndYear(customerId, month, year);
    }

    public WaterUsage saveUsage(WaterUsage usage) {
        // Validation: current >= previous
        if (usage.getCurrentReading() < usage.getPreviousReading()) {
            throw new IllegalArgumentException("Current reading must be greater than or equal to previous reading.");
        }
        // Prevent duplicate usage per month/year/customer using existBy()
        if (waterUsageRepository.existsByCustomerIdAndMonthAndYear(
                usage.getCustomer().getId(), usage.getMonth(), usage.getYear())) {
            throw new IllegalArgumentException("Usage for this customer and month/year already exists.");
        }
        usage.setLitersUsed(usage.getCurrentReading() - usage.getPreviousReading());
        return waterUsageRepository.save(usage);
    }

    public void deleteUsage(Long id) {
        waterUsageRepository.deleteById(id);
    }
}
