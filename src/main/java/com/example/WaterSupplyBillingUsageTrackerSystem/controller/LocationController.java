package com.example.WaterSupplyBillingUsageTrackerSystem.controller;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Location;
import com.example.WaterSupplyBillingUsageTrackerSystem.domain.LocationType;
import com.example.WaterSupplyBillingUsageTrackerSystem.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Location Controller - Code-Based Hierarchical Location Management
 * Supports:
 * - Code-based location identification (RW-KGL-GB-GSZ-MSZ-GV)
 * - Hierarchical location navigation and queries
 * - LocationType ENUM for type safety
 * - Parent-child relationships
 * - Query customers by location hierarchy
 */
@RestController
@RequestMapping("/api/locations")
public class LocationController {
    @Autowired
    private LocationService locationService;

    // ===== BASIC CRUD OPERATIONS =====

    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createLocation(@RequestBody Location location) {
        try {
            locationService.saveLocation(location);
            return ResponseEntity.ok("Location saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage() + " | Caused by: "
                    + (e.getCause() != null ? e.getCause().getMessage() : "unknown"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateLocation(@PathVariable Long id, @RequestBody Location location) {
        return locationService.getLocationById(id)
                .map(existing -> {
                    location.setId(id);
                    locationService.saveLocation(location);
                    return ResponseEntity.ok("Location updated successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

    // ===== CODE-BASED LOCATION OPERATIONS =====

    /**
     * Get location by unique hierarchical code
     * Example codes:
     * - RW (Rwanda country)
     * - RW-KGL (Kigali province)
     * - RW-KGL-GB (Gasabo district)
     * - RW-KGL-GB-GSZ (Gisozi sector)
     * - RW-KGL-GB-GSZ-MSZ (MUSEZRO cell)
     * - RW-KGL-GB-GSZ-MSZ-GV (Gasave village)
     *
     * Usage: GET /api/locations/code/RW-KGL-GB-GSZ-MSZ-GV
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Location> getLocationByCode(@PathVariable String code) {
        return locationService.getLocationByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Check if location code exists
     * Usage: GET /api/locations/check-code/RW-KGL-GB
     */
    @GetMapping("/check-code/{code}")
    public ResponseEntity<Boolean> checkLocationCodeExists(@PathVariable String code) {
        return ResponseEntity.ok(locationService.locationCodeExists(code));
    }

    /**
     * Get all locations under a code pattern (hierarchical search)
     * Example: GET /api/locations/code-pattern/RW-KGL - returns all locations in
     * Kigali
     * Example: GET /api/locations/code-pattern/RW-KGL-GB-GSZ - returns all under
     * Gisozi
     *
     * Usage: GET /api/locations/code-pattern/RW-KGL-GB-GSZ
     */
    @GetMapping("/code-pattern/{codePattern}")
    public List<Location> getLocationsByCodePattern(@PathVariable String codePattern) {
        return locationService.getLocationsByCodePattern(codePattern);
    }

    // ===== TYPE-BASED LOCATION QUERIES =====

    /**
     * Get all locations of a specific type
     * Usage: GET /api/locations/by-type/PROVINCE
     * Usage: GET /api/locations/by-type/SECTOR
     * Usage: GET /api/locations/by-type/VILLAGE
     */
    @GetMapping("/by-type/{type}")
    public List<Location> getLocationsByType(@PathVariable String type) {
        try {
            LocationType locationType = LocationType.valueOf(type.toUpperCase());
            return locationService.getLocationsByType(locationType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid location type: " + type);
        }
    }

    /**
     * Get location by name and type
     * Usage: GET /api/locations/by-name-type/Gasabo/DISTRICT
     */
    @GetMapping("/by-name-type/{name}/{type}")
    public ResponseEntity<Location> getLocationByNameAndType(@PathVariable String name, @PathVariable String type) {
        try {
            LocationType locationType = LocationType.valueOf(type.toUpperCase());
            return locationService.getLocationByNameAndType(name, locationType)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ===== HIERARCHICAL NAVIGATION =====

    /**
     * Get all top-level provinces (PROVINCE type with no parent)
     * Usage: GET /api/locations/provinces
     */
    @GetMapping("/provinces")
    public List<Location> getProvinces() {
        return locationService.getProvinces();
    }

    /**
     * Get all districts under a specific province
     * Usage: GET /api/locations/provinces/{provinceId}/districts
     */
    @GetMapping("/provinces/{provinceId}/districts")
    public List<Location> getDistrictsByProvince(@PathVariable Long provinceId) {
        return locationService.getDistrictsByParent(provinceId);
    }

    /**
     * Get all sectors under a specific district
     * Usage: GET /api/locations/districts/{districtId}/sectors
     */
    @GetMapping("/districts/{districtId}/sectors")
    public List<Location> getSectorsByDistrict(@PathVariable Long districtId) {
        return locationService.getSectorsByParent(districtId);
    }

    /**
     * Get all cells under a specific sector
     * Usage: GET /api/locations/sectors/{sectorId}/cells
     */
    @GetMapping("/sectors/{sectorId}/cells")
    public List<Location> getCellsBySector(@PathVariable Long sectorId) {
        return locationService.getCellsByParent(sectorId);
    }

    /**
     * Get all villages under a specific cell
     * Usage: GET /api/locations/cells/{cellId}/villages
     */
    @GetMapping("/cells/{cellId}/villages")
    public List<Location> getVillagesByCell(@PathVariable Long cellId) {
        return locationService.getVillagesByParent(cellId);
    }

    /**
     * Get all child locations of any parent (generic)
     * Usage: GET /api/locations/{parentId}/children
     */
    @GetMapping("/{parentId}/children")
    public List<Location> getLocationChildren(@PathVariable Long parentId) {
        return locationService.getLocationsByParent(parentId);
    }

    /**
     * Create a child location under a parent location
     * Usage: POST /api/locations/{parentId}/children
     * This automatically sets the parent and country
     */
    @PostMapping("/{parentId}/children")
    public ResponseEntity<String> createChildLocation(@PathVariable Long parentId, @RequestBody Location child) {
        try {
            return locationService.getLocationById(parentId)
                    .map(parent -> {
                        Location saved = locationService.saveChildLocation(parent, child);
                        return ResponseEntity.ok("Child location created with id: " + saved.getId());
                    })
                    .orElse(ResponseEntity.status(404).body("Parent location not found with id: " + parentId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all village-level locations for customer assignment
     * Usage: GET /api/locations/villages/all
     */
    @GetMapping("/villages/all")
    public List<Location> getAllVillages() {
        return locationService.getAllVillages();
    }

    // ===== RECURSIVE HIERARCHY OPERATIONS =====

    /**
     * Get all descendants of a location recursively
     * Useful for: "Show all customers in province Kigali" - gets all villages under
     * Kigali
     * Usage: GET /api/locations/{parentId}/descendants
     */
    @GetMapping("/{parentId}/descendants")
    public List<Location> getAllDescendants(@PathVariable Long parentId) {
        return locationService.getAllDescendants(parentId);
    }

    /**
     * Get full path from a location to root (village → cell → sector → district →
     * province → country)
     * Usage: GET /api/locations/{villageId}/full-path
     */
    @GetMapping("/{locationId}/full-path")
    public ResponseEntity<List<Location>> getFullLocationPath(@PathVariable Long locationId) {
        List<Location> path = locationService.getFullLocationPath(locationId);
        return path.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(path);
    }

    /**
     * Get all villages (leaf locations) under a parent location
     * Useful for customer queries: Get all customers in sector Gisozi
     * Usage: GET /api/locations/{parentId}/villages
     */
    @GetMapping("/{parentId}/villages")
    public List<Location> getAllVillagesUnder(@PathVariable Long parentId) {
        return locationService.getAllVillagesUnder(parentId);
    }

}
