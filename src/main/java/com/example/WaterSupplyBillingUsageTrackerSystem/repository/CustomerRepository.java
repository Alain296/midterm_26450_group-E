package com.example.WaterSupplyBillingUsageTrackerSystem.repository;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Customer;
import com.example.WaterSupplyBillingUsageTrackerSystem.domain.LocationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Customer Repository
 * Demonstrates:
 * - Pagination and sorting support
 * - Code-based location queries (hierarchical)
 * - Location type-based queries (PROVINCE, SECTOR, VILLAGE)
 * - Custom query methods for location searches
 * - existBy() methods for existence checking
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // ===== EXISTENCE CHECKS =====
    
    /**
     * existBy() method demonstrates existence checking
     * More efficient than findBy() as it returns boolean without loading full entity
     */
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    
    Optional<Customer> findByEmail(String email);
    
    // ===== PAGINATION SUPPORT =====
    
    /**
     * Pagination support - returns a Page with customers
     * Supports Requirement: Implementation of Pagination
     */
    Page<Customer> findAll(Pageable pageable);
    
    // ===== CODE-BASED LOCATION QUERIES =====
    
    /**
     * Get customer(s) by exact location code (village-level)
     * Example code: RW-KGL-GB-GSZ-MSZ-GV (Gasave village)
     * Since location_id is UNIQUE in Customer table, this returns max 1 customer
     */
    @Query("SELECT c FROM Customer c JOIN c.location l WHERE l.code = :code")
    List<Customer> findCustomersByLocationCode(@Param("code") String code);
    
    /**
     * Get customers by location code pattern (hierarchical search)
     * Example: RW-KGL% returns all customers in all villages under Kigali province
     * This recursively finds all descendants
     */
    @Query("SELECT DISTINCT c FROM Customer c " +
            "JOIN c.location l " +
            "LEFT JOIN l.parent p1 " +
            "LEFT JOIN p1.parent p2 " +
            "LEFT JOIN p2.parent p3 " +
            "LEFT JOIN p3.parent p4 " +
            "WHERE l.code LIKE :codePattern " +
            "OR p1.code LIKE :codePattern " +
            "OR p2.code LIKE :codePattern " +
            "OR p3.code LIKE :codePattern " +
            "OR p4.code LIKE :codePattern")
    List<Customer> findCustomersByLocationCodePattern(@Param("codePattern") String codePattern);
    
    /**
     * Get customers by location type name
     * Traverses the location hierarchy to find all customers under the given location type/name
     * Example: Get all customers in PROVINCE "Kigali" (includes all districts, sectors, cells, villages)
     * Example: Get all customers in SECTOR "Gisozi" (includes all cells and villages)
     * Example: Get all customers in VILLAGE "Gasave" (direct customers in that village)
     */
    @Query("SELECT DISTINCT c FROM Customer c JOIN c.location l " +
            "WHERE (l.type = :type AND l.name = :name) " +
            "OR (l.parent IS NOT NULL AND l.parent.type = :type AND l.parent.name = :name) " +
            "OR (l.parent.parent IS NOT NULL AND l.parent.parent.type = :type AND l.parent.parent.name = :name) " +
            "OR (l.parent.parent.parent IS NOT NULL AND l.parent.parent.parent.type = :type AND l.parent.parent.parent.name = :name) " +
            "OR (l.parent.parent.parent.parent IS NOT NULL AND l.parent.parent.parent.parent.type = :type AND l.parent.parent.parent.parent.name = :name) " +
            "OR (l.parent.parent.parent.parent.parent IS NOT NULL AND l.parent.parent.parent.parent.parent.type = :type AND l.parent.parent.parent.parent.parent.name = :name)")
    List<Customer> findCustomersByLocationType(@Param("type") LocationType type, @Param("name") String name);
    
    // ===== LOCATION-BASED PAGINATED QUERIES =====
    
    @Query("SELECT c FROM Customer c JOIN c.location l WHERE l.code = :code")
    Page<Customer> findCustomersByLocationCodePageable(@Param("code") String code, Pageable pageable);
    
    @Query("SELECT DISTINCT c FROM Customer c " +
            "JOIN c.location l " +
            "LEFT JOIN l.parent p1 " +
            "LEFT JOIN p1.parent p2 " +
            "LEFT JOIN p2.parent p3 " +
            "LEFT JOIN p3.parent p4 " +
            "WHERE l.code LIKE :codePattern " +
            "OR p1.code LIKE :codePattern " +
            "OR p2.code LIKE :codePattern " +
            "OR p3.code LIKE :codePattern " +
            "OR p4.code LIKE :codePattern")
    Page<Customer> findCustomersByLocationCodePatternPageable(@Param("codePattern") String codePattern, Pageable pageable);
    
    @Query("SELECT DISTINCT c FROM Customer c JOIN c.location l " +
            "WHERE (l.type = :type AND l.name = :name) " +
            "OR (l.parent IS NOT NULL AND l.parent.type = :type AND l.parent.name = :name) " +
            "OR (l.parent.parent IS NOT NULL AND l.parent.parent.type = :type AND l.parent.parent.name = :name) " +
            "OR (l.parent.parent.parent IS NOT NULL AND l.parent.parent.parent.type = :type AND l.parent.parent.parent.name = :name) " +
            "OR (l.parent.parent.parent.parent IS NOT NULL AND l.parent.parent.parent.parent.type = :type AND l.parent.parent.parent.parent.name = :name) " +
            "OR (l.parent.parent.parent.parent.parent IS NOT NULL AND l.parent.parent.parent.parent.parent.type = :type AND l.parent.parent.parent.parent.parent.name = :name)")
    Page<Customer> findCustomersByLocationTypePageable(@Param("type") LocationType type, @Param("name") String name, Pageable pageable);
    
    // ===== BACKWARD COMPATIBILITY QUERIES (Legacy Province-Based) =====
    
    /**
     * Retrieve customers from a given province using province name
     * Uses a JOIN query to connect Customer -> Location -> Province name
     * Supports Requirement: Retrieve users from a given province using province name
     */
    @Query("SELECT c FROM Customer c JOIN c.location l WHERE l.province = :provinceName")
    List<Customer> findCustomersByProvinceName(@Param("provinceName") String provinceName);
    
    /**
     * Retrieve customers from a given province using province code
     * Supports Requirement: Retrieve users from a given province using province code
     */
    @Query("SELECT c FROM Customer c JOIN c.location l WHERE l.provinceCode = :provinceCode")
    List<Customer> findCustomersByProvinceCode(@Param("provinceCode") String provinceCode);
    
    /**
     * Paginated province search
     */
    @Query("SELECT c FROM Customer c JOIN c.location l WHERE l.province = :provinceName")
    Page<Customer> findCustomersByProvinceNamePageable(@Param("provinceName") String provinceName, Pageable pageable);
    
    @Query("SELECT c FROM Customer c JOIN c.location l WHERE l.provinceCode = :provinceCode")
    Page<Customer> findCustomersByProvinceCodePageable(@Param("provinceCode") String provinceCode, Pageable pageable);
}
