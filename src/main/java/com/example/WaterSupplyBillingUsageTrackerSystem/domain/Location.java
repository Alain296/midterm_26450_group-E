package com.example.WaterSupplyBillingUsageTrackerSystem.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

/**
 * Location entity represents geographical areas with hierarchical
 * administrative structure.
 * This demonstrates:
 * - Self-referential Many-to-One/One-to-Many for hierarchy (parent-child
 * locations)
 * - One-to-One relationship with Customer
 * - Hierarchical levels: COUNTRY → PROVINCE → DISTRICT → SECTOR → CELL →
 * VILLAGE
 * - Unique code-based identification (e.g., RW, RW-KGL, RW-KGL-GB,
 * RW-KGL-GB-GSZ)
 */
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Location identification
    @NotBlank(message = "Location name is required")
    @Column(nullable = false)
    private String name; // e.g., "Kigali", "Gasabo", "Gisozi"

    // Hierarchical type - COUNTRY, PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE
    @NotNull(message = "Location type is required")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LocationType type;

    // Unique hierarchical code for identification
    // Example patterns:
    // RW (country code)
    // RW-KGL (Rwanda-Kigali province)
    // RW-KGL-GB (Rwanda-Kigali-Gasabo district)
    // RW-KGL-GB-GSZ (Rwanda-Kigali-Gasabo-Gisozi sector)
    // RW-KGL-GB-GSZ-MSZ (Rwanda-Kigali-Gasabo-Gisozi-MUSEZRO cell)
    // RW-KGL-GB-GSZ-MSZ-GV (Rwanda-Kigali-Gasabo-Gisozi-MUSEZRO-Gasave village)
    @NotBlank(message = "Location code is required")
    @Column(unique = true, nullable = false)
    private String code;


    @Column(nullable = false)
    private String country; // "Rwanda"

    // Hierarchical relationship - Parent Location (self-referential)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Location parent;

    // Hierarchical relationship - Child Locations (self-referential)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Location> children;

    // One-to-Many relationship with Customer (Many Customers can live in one
    // Location/Village)
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Customer> customers;

    // Constructors
    public Location() {
    }

    public Location(String name, LocationType type, String code) {
        this.name = name;
        this.type = type;
        this.code = code;
        this.country = "Rwanda";
    }

    public Location(String name, LocationType type, String code, Location parent) {
        this.name = name;
        this.type = type;
        this.code = code;
        this.parent = parent;
        this.country = "Rwanda";
    }


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public Location getParent() {
        return parent;
    }

    public void setParent(Location parent) {
        this.parent = parent;
    }

    public List<Location> getChildren() {
        return children;
    }

    public void setChildren(List<Location> children) {
        this.children = children;
    }
}
