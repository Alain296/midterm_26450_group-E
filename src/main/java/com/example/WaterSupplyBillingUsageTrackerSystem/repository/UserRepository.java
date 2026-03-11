package com.example.WaterSupplyBillingUsageTrackerSystem.repository;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * User Repository
 * Demonstrates:
 * - Custom query methods (existsby)
 * - Optional return types
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * existBy() method demonstrates existence checking in Spring Data JPA
     * This is an efficient way to check if a user exists without loading the entire
     * entity
     */
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    /**
     * Requirement 8: Retrieve all users from a given province using province code
     * OR province name
     * This query navigates from User -> Customer -> Location (Village -> Cell ->
     * Sector -> District -> Province)
     */
    @Query("SELECT u FROM User u " +
            "JOIN u.customer c " +
            "JOIN c.location v " +
            "JOIN v.parent ce " +
            "JOIN ce.parent s " +
            "JOIN s.parent d " +
            "JOIN d.parent p " +
            "WHERE p.name = :provinceName AND p.type = 'PROVINCE'")
    List<User> findUsersByProvinceName(@Param("provinceName") String provinceName);

    @Query("SELECT u FROM User u " +
            "JOIN u.customer c " +
            "JOIN c.location v " +
            "JOIN v.parent ce " +
            "JOIN ce.parent s " +
            "JOIN s.parent d " +
            "JOIN d.parent p " +
            "WHERE p.code = :provinceCode AND p.type = 'PROVINCE'")
    List<User> findUsersByProvinceCode(@Param("provinceCode") String provinceCode);
}
