package com.example.WaterSupplyBillingUsageTrackerSystem.service;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Bill;
import com.example.WaterSupplyBillingUsageTrackerSystem.domain.WaterUsage;
import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Customer;
import com.example.WaterSupplyBillingUsageTrackerSystem.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Bill Service
 * Demonstrates:
 * - Pagination support with Pageable
 * - Sorting functionality
 * - existBy() methods for existence checking
 */
@Service
public class BillService {
    private static final double RATE_PER_LITER = 0.01; // Example rate

    @Autowired
    private BillRepository billRepository;

    /**
     * Get all bills with pagination
     * Requirement: Implementation of Pagination
     */
    public Page<Bill> getAllBills(Pageable pageable) {
        return billRepository.findAll(pageable);
    }

    /**
     * Get all bills without pagination (legacy)
     */
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    /**
     * Get all bills sorted
     * Requirement: Implementation of Sorting functionality
     */
    public List<Bill> getAllBillsSorted(Sort sort) {
        return billRepository.findAll(sort);
    }

    public Optional<Bill> getBillById(Long id) {
        return billRepository.findById(id);
    }

    /**
     * Get bills by customer with pagination
     */
    public Page<Bill> getBillsByCustomerId(Long customerId, Pageable pageable) {
        return billRepository.findByCustomerId(customerId, pageable);
    }

    /**
     * Get bills by status with pagination
     */
    public Page<Bill> getBillsByStatus(String status, Pageable pageable) {
        return billRepository.findByStatus(status, pageable);
    }

    /**
     * existBy() method - Check if bill exists for customer and usage
     */
    public boolean billExistsForCustomerUsage(Long customerId, Long usageId) {
        return billRepository.existsByCustomerIdAndUsageId(customerId, usageId);
    }

    public Bill generateBill(Customer customer, WaterUsage usage) {
        Bill bill = new Bill();
        bill.setCustomer(customer);
        bill.setUsage(usage);
        bill.setAmount(usage.getLitersUsed() * RATE_PER_LITER);
        bill.setStatus("UNPAID");
        bill.setGeneratedDate(LocalDate.now());
        bill.setDueDate(LocalDate.now().plusDays(30));
        return billRepository.save(bill);
    }

    public Bill saveBill(Bill bill) {
        return billRepository.save(bill);
    }

    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }
}
