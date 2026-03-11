package com.example.WaterSupplyBillingUsageTrackerSystem.service;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Location;
import com.example.WaterSupplyBillingUsageTrackerSystem.domain.LocationType;
import com.example.WaterSupplyBillingUsageTrackerSystem.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Location Service
 * Demonstrates:
 * - Code-based location identification and queries
 * - Hierarchical location management (COUNTRY → PROVINCE → DISTRICT → SECTOR → CELL → VILLAGE)
 * - Saving location data with parent-child relationships
 * - LocationType ENUM for type safety
 */
@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    // ===== CODE-BASED LOCATION OPERATIONS =====
    
    /**
     * Save a new location with code-based identification
     */
    public Location saveLocation(Location location) {
        // Auto-generate code if not provided and parent exists
        if (location.getCode() == null || location.getCode().isEmpty()) {
            location.setCode(generateLocationCode(location));
        }
        return locationRepository.save(location);
    }

    /**
     * Get location by unique code
     * Example codes: "RW", "RW-KGL", "RW-KGL-GB", "RW-KGL-GB-GSZ", "RW-KGL-GB-GSZ-MSZ", "RW-KGL-GB-GSZ-MSZ-GV"
     */
    public Optional<Location> getLocationByCode(String code) {
        return locationRepository.findByCode(code);
    }

    /**
     * Find all locations under a code prefix
     * Example: "RW-KGL%" to find all locations under Kigali province
     */
    public List<Location> getLocationsByCodePattern(String codePattern) {
        return locationRepository.findByCodePattern(codePattern + "%");
    }

    /**
     * Check if location code exists
     */
    public boolean locationCodeExists(String code) {
        return locationRepository.existsByCode(code);
    }

    /**
     * Auto-generate hierarchical code based on parent and location name
     */
    private String generateLocationCode(Location location) {
        if (location.getParent() == null) {
            // Root level (country) - just return country code
            return "RW"; // Rwanda
        }
        
        String parentCode = location.getParent().getCode();
        String namePrefix = location.getName().substring(0, Math.min(3, location.getName().length())).toUpperCase();
        return parentCode + "-" + namePrefix;
    }

    // ===== SAVE CHILD LOCATION =====
    
    /**
     * Save a child location under a parent location with auto-generated code
     */
    public Location saveChildLocation(Location parentLocation, Location childLocation) {
        childLocation.setParent(parentLocation);
        childLocation.setCountry(parentLocation.getCountry());
        
        // Auto-generate code
        if (childLocation.getCode() == null || childLocation.getCode().isEmpty()) {
            childLocation.setCode(generateLocationCode(childLocation));
        }
        
        return locationRepository.save(childLocation);
    }

    // ===== BASIC CRUD OPERATIONS =====
    
    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    // ===== TYPE-BASED QUERIES =====
    
    /**
     * Get all locations of a specific type
     */
    public List<Location> getLocationsByType(LocationType type) {
        return locationRepository.findByType(type);
    }

    /**
     * Get location by name and type
     */
    public Optional<Location> getLocationByNameAndType(String name, LocationType type) {
        return locationRepository.findByNameAndType(name, type);
    }

    // ===== HIERARCHICAL LOCATION QUERIES =====
    
    /**
     * Get all top-level provinces (PROVINCE type with no parent)
     */
    public List<Location> getProvinces() {
        return locationRepository.getProvinces();
    }

    /**
     * Get all districts under a province by parent ID
     */
    public List<Location> getDistrictsByParent(Long provinceId) {
        return locationRepository.getDistrictsByParent(provinceId);
    }

    /**
     * Get all sectors under a district by parent ID
     */
    public List<Location> getSectorsByParent(Long districtId) {
        return locationRepository.getSectorsByParent(districtId);
    }

    /**
     * Get all cells under a sector by parent ID
     */
    public List<Location> getCellsByParent(Long sectorId) {
        return locationRepository.getCellsByParent(sectorId);
    }

    /**
     * Get all villages under a cell by parent ID
     */
    public List<Location> getVillagesByParent(Long cellId) {
        return locationRepository.getVillagesByParent(cellId);
    }

    /**
     * Get all child locations of a specified parent (any type)
     */
    public List<Location> getLocationsByParent(Long parentId) {
        return locationRepository.findByParentId(parentId);
    }

    /**
     * Get all village-level locations for customer assignment
     */
    public List<Location> getAllVillages() {
        return locationRepository.getAllVillages();
    }

    // ===== RECURSIVE HIERARCHY OPERATIONS =====
    
    /**
     * Get all descendant locations recursively for a parent location
     * Useful for "show all customers in this province" queries
     */
    public List<Location> getAllDescendants(Long parentId) {
        List<Location> children = locationRepository.findByParentId(parentId);
        List<Location> allDescendants = new java.util.ArrayList<>(children);
        
        for (Location child : children) {
            allDescendants.addAll(getAllDescendants(child.getId()));
        }
        
        return allDescendants;
    }

    /**
     * Get full path from leaf location to root (village → cell → sector → district → province → country)
     */
    public List<Location> getFullLocationPath(Long locationId) {
        List<Location> path = new java.util.ArrayList<>();
        Optional<Location> current = locationRepository.findById(locationId);
        
        while (current.isPresent()) {
            path.add(0, current.get());
            current = Optional.ofNullable(current.get().getParent());
        }
        
        return path;
    }

    /**
     * Get all leaf-level descendants (villages) under a location
     */
    public List<Location> getAllVillagesUnder(Long parentId) {
        List<Location> descendants = getAllDescendants(parentId);
        return descendants.stream()
                .filter(l -> l.getType() == LocationType.VILLAGE)
                .collect(Collectors.toList());
    }

    // ===== BACKWARD COMPATIBILITY QUERIES =====
    
    public List<Location> getLocationsByProvinceName(String province) {
        return locationRepository.findByProvince(province);
    }

    public List<Location> getLocationsByProvinceCode(String provinceCode) {
        return locationRepository.findByProvinceCode(provinceCode);
    }

    public Optional<Location> getLocationByProvinceName(String province) {
        return locationRepository.findByProvinceIgnoreCase(province);
    }

    public Optional<Location> getLocationByProvinceCode(String provinceCode) {
        return locationRepository.findByProvinceCodeIgnoreCase(provinceCode);
    }

    // ===== EXISTENCE CHECKS =====
    
    public boolean provinceExists(String province) {
        return locationRepository.existsByProvince(province);
    }

    public boolean provinceCodeExists(String provinceCode) {
        return locationRepository.existsByProvinceCode(provinceCode);
    }

    public boolean locationTypeExists(LocationType type) {
        return locationRepository.existsByType(type);
    }

    public boolean locationNameExists(String name) {
        return locationRepository.existsByName(name);
    }

    public boolean locationNameAndTypeExists(String name, LocationType type) {
        return locationRepository.existsByNameAndType(name, type);
    }
}
