package com.example.WaterSupplyBillingUsageTrackerSystem.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * TariffRate entity represents water pricing information.
 * Used to calculate bills based on water usage tiers.
 */
@Entity
public class TariffRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category; // Residential, Commercial, Industrial

    @Column(nullable = false)
    private double minUsage; // Liters minimum

    @Column(nullable = false)
    private double maxUsage; // Liters maximum

    @Column(nullable = false)
    private double ratePerLiter; // Price per liter

    @Column(nullable = false)
    private double baseCharge; // Fixed monthly charge

    @Column(nullable = false)
    private LocalDate effectiveDate;

    private LocalDate endDate;

    private boolean active = true;

    // Constructors
    public TariffRate() {}

    public TariffRate(String category, double minUsage, double maxUsage, 
                      double ratePerLiter, double baseCharge, LocalDate effectiveDate) {
        this.category = category;
        this.minUsage = minUsage;
        this.maxUsage = maxUsage;
        this.ratePerLiter = ratePerLiter;
        this.baseCharge = baseCharge;
        this.effectiveDate = effectiveDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getMinUsage() { return minUsage; }
    public void setMinUsage(double minUsage) { this.minUsage = minUsage; }

    public double getMaxUsage() { return maxUsage; }
    public void setMaxUsage(double maxUsage) { this.maxUsage = maxUsage; }

    public double getRatePerLiter() { return ratePerLiter; }
    public void setRatePerLiter(double ratePerLiter) { this.ratePerLiter = ratePerLiter; }

    public double getBaseCharge() { return baseCharge; }
    public void setBaseCharge(double baseCharge) { this.baseCharge = baseCharge; }

    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
