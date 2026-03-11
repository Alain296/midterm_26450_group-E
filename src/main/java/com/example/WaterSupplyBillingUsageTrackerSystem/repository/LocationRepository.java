package com.example.WaterSupplyBillingUsageTrackerSystem.repository;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.Location;
import com.example.WaterSupplyBillingUsageTrackerSystem.domain.LocationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Location Repository
 * Supports:
 * - Code-based location identification and queries
 * - Hierarchical location navigation
 * - Type-based location filtering
 * - Parent-child relationship queries
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    // ===== CODE-BASED QUERIES (Primary approach) =====

    /**
     * Find location by unique code
     * Example codes: "RW", "RW-KGL", "RW-KGL-GB", "RW-KGL-GB-GSZ"
     */
    Optional<Location> findByCode(String code);

    /**
     * Check if location code exists
     */
    boolean existsByCode(String code);

    /**
     * Find all locations matching a code pattern using LIKE
     * Example: "RW-KGL%" to find all locations under Kigali province
     */
    @Query("SELECT l FROM Location l WHERE l.code LIKE :codePattern")
    List<Location> findByCodePattern(@Param("codePattern") String codePattern);

    // ===== TYPE-BASED QUERIES =====

    /**
     * Find all locations of a specific type
     */
    List<Location> findByType(LocationType type);

    /**
     * Find all locations by name and type
     */
    Optional<Location> findByNameAndType(String name, LocationType type);

    // ===== HIERARCHICAL QUERIES =====

    /**
     * Get all top-level Countries (parent_id is NULL)
     */
    @Query("SELECT l FROM Location l WHERE l.type = 'COUNTRY' AND l.parent IS NULL")
    List<Location> getCountries();

    /**
     * Get all provinces (administrative divisions directly under country Rwanda)
     */
    @Query("SELECT l FROM Location l WHERE l.type = 'PROVINCE'")
    List<Location> getProvinces();

    /**
     * Get all districts under a province by parent ID
     */
    @Query("SELECT l FROM Location l WHERE l.type = 'DISTRICT' AND l.parent.id = :parentId")
    List<Location> getDistrictsByParent(@Param("parentId") Long parentId);

    /**
     * Get all sectors under a district by parent ID
     */
    @Query("SELECT l FROM Location l WHERE l.type = 'SECTOR' AND l.parent.id = :parentId")
    List<Location> getSectorsByParent(@Param("parentId") Long parentId);

    /**
     * Get all cells under a sector by parent ID
     */
    @Query("SELECT l FROM Location l WHERE l.type = 'CELL' AND l.parent.id = :parentId")
    List<Location> getCellsByParent(@Param("parentId") Long parentId);

    /**
     * Get all villages under a cell by parent ID
     */
    @Query("SELECT l FROM Location l WHERE l.type = 'VILLAGE' AND l.parent.id = :parentId")
    List<Location> getVillagesByParent(@Param("parentId") Long parentId);

    /**
     * Get all children of a location regardless of type
     */
    List<Location> findByParentId(Long parentId);

    /**
     * Get all leaf-level locations (villages) for customer assignment
     */
    @Query("SELECT l FROM Location l WHERE l.type = 'VILLAGE'")
    List<Location> getAllVillages();

    // ===== BACKWARD COMPATIBILITY QUERIES =====

    @Query("SELECT l FROM Location l WHERE l.type = 'PROVINCE' AND l.province = :province")
    List<Location> findByProvince(@Param("province") String province);

    @Query("SELECT l FROM Location l WHERE l.type = 'PROVINCE' AND l.provinceCode = :provinceCode")
    List<Location> findByProvinceCode(@Param("provinceCode") String provinceCode);

    Optional<Location> findByProvinceCodeIgnoreCase(String provinceCode);

    Optional<Location> findByProvinceIgnoreCase(String province);

    boolean existsByProvince(String province);

    boolean existsByProvinceCode(String provinceCode);

    // ===== EXISTENCE CHECKS =====

    boolean existsByType(LocationType type);

    boolean existsByName(String name);

    boolean existsByNameAndType(String name, LocationType type);
}
