package com.example.WaterSupplyBillingUsageTrackerSystem.domain;

import jakarta.persistence.*;
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

    // Basic location information (not needed in API responses)
    @JsonIgnore
    private String street;
    @JsonIgnore
    private String city;

    // Location identification
    @Column(nullable = false)
    private String name; // e.g., "Kigali", "Gasabo", "Gisozi"

    // Hierarchical type - COUNTRY, PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE
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
    @Column(unique = true, nullable = false)
    private String code;

    // Backward compatibility fields (legacy data - not needed in API responses)
    @JsonIgnore
    private String province;
    @JsonIgnore
    private String provinceCode; // Legacy: e.g., "KGL" for Kigali
    @JsonIgnore
    private String postalCode;

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

    // Legacy constructor for backward compatibility
    public Location(String street, String city, String province, String provinceCode, String country,
            String locationType) {
        this.street = street;
        this.city = city;
        this.province = province;
        this.provinceCode = provinceCode;
        this.country = country;
        this.name = province != null ? province : locationType;
        // Note: For legacy data, manually set type and code
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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
