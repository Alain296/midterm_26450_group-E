package com.example.WaterSupplyBillingUsageTrackerSystem.service;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Customer;
import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Location;
import com.example.WaterSupplyBillingUsageTrackerSystem.domain.LocationType;
import com.example.WaterSupplyBillingUsageTrackerSystem.domain.User;
import com.example.WaterSupplyBillingUsageTrackerSystem.repository.CustomerRepository;
import com.example.WaterSupplyBillingUsageTrackerSystem.repository.LocationRepository;
import com.example.WaterSupplyBillingUsageTrackerSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Customer Service
 * Demonstrates:
 * - Pagination and sorting support
 * - Code-based location queries (hierarchical)
 * - Location type-based queries (PROVINCE, SECTOR, VILLAGE)
 * - existBy() methods for existence checking
 */
@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    // ===== PAGINATION & SORTING =====
    
    /**
     * Get all customers with pagination
     * Requirement: Implementation of Pagination
     * Example: page=0, size=10 gets first 10 customers
     */
    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    /**
     * Get all customers without pagination (legacy method)
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Get all customers sorted by a specific field
     * Requirement: Implementation of Sorting functionality
     * Example: Sort.by("name").ascending() or Sort.by("registrationDate").descending()
     */
    public List<Customer> getAllCustomersSorted(Sort sort) {
        return customerRepository.findAll(sort);
    }

    // ===== BASIC CRUD =====
    
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    /**
     * Get customer by email
     */
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer saveCustomer(Customer customer) {
        // If locationId is provided, load the Location from database
        if (customer.getLocation() != null && customer.getLocation().getId() != null) {
            Optional<Location> location = locationRepository.findById(customer.getLocation().getId());
            if (location.isPresent()) {
                customer.setLocation(location.get());
            }
        }
        
        // Link User to Customer if provided
        if (customer.getUser() != null && customer.getUser().getId() != null) {
            Optional<User> user = userRepository.findById(customer.getUser().getId());
            user.ifPresent(customer::setUser);
        }
        
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id).map(customer -> {
            if (customerDetails.getName() != null) {
                customer.setName(customerDetails.getName());
            }
            if (customerDetails.getAddress() != null) {
                customer.setAddress(customerDetails.getAddress());
            }
            if (customerDetails.getPhone() != null) {
                customer.setPhone(customerDetails.getPhone());
            }
            if (customerDetails.getEmail() != null) {
                customer.setEmail(customerDetails.getEmail());
            }
            // Load the full Location object from database by ID to avoid null field issues
            if (customerDetails.getLocation() != null && customerDetails.getLocation().getId() != null) {
                Optional<Location> location = locationRepository.findById(customerDetails.getLocation().getId());
                if (location.isPresent()) {
                    customer.setLocation(location.get());
                }
            }
            // Link User to Customer (sets user_id FK in customers table)
            if (customerDetails.getUser() != null && customerDetails.getUser().getId() != null) {
                Optional<User> user = userRepository.findById(customerDetails.getUser().getId());
                user.ifPresent(customer::setUser);
            }
            return customerRepository.save(customer);
        }).orElse(null);
    }

    // ===== EXISTENCE CHECKS =====
    
    /**
     * Check if customer exists by email
     * More efficient than findByEmail() when only checking existence
     */
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    /**
     * Check if customer exists by phone
     */
    public boolean existsByPhone(String phone) {
        return customerRepository.existsByPhone(phone);
    }

    // ===== CODE-BASED LOCATION QUERIES =====
    
    /**
     * Get customer(s) by exact location code
     * Example code: RW-KGL-GB-GSZ-MSZ-GV (Gasave village)
     * Since location_id is UNIQUE, typically returns 0 or 1 customer
     */
    public List<Customer> getCustomersByLocationCode(String code) {
        return customerRepository.findCustomersByLocationCode(code);
    }

    /**
     * Get customer(s) by location code with pagination
     */
    public Page<Customer> getCustomersByLocationCodePageable(String code, Pageable pageable) {
        return customerRepository.findCustomersByLocationCodePageable(code, pageable);
    }

    /**
     * Get all customers under a location hierarchy using code pattern
     * Example: Code pattern "RW-KGL-GB-GSZ%" returns all customers in all villages under Gisozi sector
     * Example: Code pattern "RW-KGL%" returns all customers in all villages under Kigali province
     */
    public List<Customer> getCustomersByLocationCodePattern(String codePattern) {
        return customerRepository.findCustomersByLocationCodePattern(codePattern + "%");
    }

    /**
     * Get customers by location code pattern with pagination
     */
    public Page<Customer> getCustomersByLocationCodePatternPageable(String codePattern, Pageable pageable) {
        return customerRepository.findCustomersByLocationCodePatternPageable(codePattern + "%", pageable);
    }

    // ===== LOCATION TYPE-BASED QUERIES =====
    
    /**
     * Get customers by location type and name
     * Example: type=SECTOR, name=Gisozi -> All customers in villages under Gisozi sector
     * Example: type=PROVINCE, name=Kigali -> All customers in villages under Kigali province
     * Example: type=VILLAGE, name=Gasave -> All customers in Gasave village
     */
    public List<Customer> getCustomersByLocationType(LocationType type, String name) {
        return customerRepository.findCustomersByLocationType(type, name);
    }

    /**
     * Get customers by location type with pagination
     */
    public Page<Customer> getCustomersByLocationTypePageable(LocationType type, String name, Pageable pageable) {
        return customerRepository.findCustomersByLocationTypePageable(type, name, pageable);
    }

    // ===== CONVENIENCE METHODS FOR SPECIFIC LOCATION TYPES =====
    
    /**
     * Get all customers in a province by name
     * Convenience method: GetCustomers by PROVINCE LocationType
     * Example: getCustomersInProvince("Kigali") -> All customers in villages under Kigali province
     */
    public List<Customer> getCustomersInProvince(String provinceName) {
        return getCustomersByLocationType(LocationType.PROVINCE, provinceName);
    }

    /**
     * Get all customers in a province with pagination
     */
    public Page<Customer> getCustomersInProvincePageable(String provinceName, Pageable pageable) {
        return getCustomersByLocationTypePageable(LocationType.PROVINCE, provinceName, pageable);
    }

    /**
     * Get all customers in a sector by name
     * Convenience method: GetCustomers by SECTOR LocationType
     * Example: getCustomersInSector("Gisozi") -> All customers in villages under Gisozi sector
     */
    public List<Customer> getCustomersInSector(String sectorName) {
        return getCustomersByLocationType(LocationType.SECTOR, sectorName);
    }

    /**
     * Get all customers in a sector with pagination
     */
    public Page<Customer> getCustomersInSectorPageable(String sectorName, Pageable pageable) {
        return getCustomersByLocationTypePageable(LocationType.SECTOR, sectorName, pageable);
    }

    /**
     * Get all customers in a village by name
     * Convenience method: GetCustomers by VILLAGE LocationType
     * Example: getCustomersInVillage("Gasave") -> All customers in Gasave village
     */
    public List<Customer> getCustomersInVillage(String villageName) {
        return getCustomersByLocationType(LocationType.VILLAGE, villageName);
    }

    /**
     * Get all customers in a village with pagination
     */
    public Page<Customer> getCustomersInVillagePageable(String villageName, Pageable pageable) {
        return getCustomersByLocationTypePageable(LocationType.VILLAGE, villageName, pageable);
    }

    // ===== BACKWARD COMPATIBILITY METHODS (Legacy Province-Based) =====
    
    /**
     * Retrieve all customers from a given province using province name
     * Requirement: Retrieve users from a given province using province name
     * DEPRECATED: Use getCustomersInProvince() or getCustomersByLocationType() instead
     */
    public List<Customer> getCustomersByProvinceName(String provinceName) {
        return customerRepository.findCustomersByProvinceName(provinceName);
    }

    /**
     * Retrieve all customers from a given province using province code
     * Requirement: Retrieve users from a given province using province code
     * DEPRECATED: Use getCustomersInProvince() or getCustomersByLocationType() instead
     */
    public List<Customer> getCustomersByProvinceCode(String provinceCode) {
        return customerRepository.findCustomersByProvinceCode(provinceCode);
    }

    /**
     * Get customers from province with pagination
     * Supports pagination + province filtering
     * DEPRECATED: Use getCustomersInProvincePageable() instead
     */
    public Page<Customer> getCustomersByProvinceNamePageable(String provinceName, Pageable pageable) {
        return customerRepository.findCustomersByProvinceNamePageable(provinceName, pageable);
    }

    public Page<Customer> getCustomersByProvinceCodePageable(String provinceCode, Pageable pageable) {
        return customerRepository.findCustomersByProvinceCodePageable(provinceCode, pageable);
    }
}
