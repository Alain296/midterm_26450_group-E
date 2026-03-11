package com.example.WaterSupplyBillingUsageTrackerSystem.repository;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.TariffRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * TariffRate Repository
 * Manages pricing tiers and rates
 */
@Repository
public interface TariffRateRepository extends JpaRepository<TariffRate, Long> {
    List<TariffRate> findByCategory(String category);
    List<TariffRate> findByActiveTrue();
    Optional<TariffRate> findByCategoryAndActiveTrue(String category);
    boolean existsByCategory(String category);
}
