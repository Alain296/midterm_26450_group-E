package com.example.WaterSupplyBillingUsageTrackerSystem.domain;

/**
 * LocationType ENUM represents the hierarchical levels of administrative divisions.
 * Used for Rwanda's administrative structure:
 * - COUNTRY: Rwanda (root level)
 * - PROVINCE: Kigali, Southern, Western, Northern, Eastern
 * - DISTRICT: Gasabo, Kicukiro, Nyarugenge (under provinces)
 * - SECTOR: Gisozi, Remera, etc. (under districts)
 * - CELL: MUSEZRO, etc. (under sectors)
 * - VILLAGE: Gasave, Kiyovu, etc. (under cells)
 */
public enum LocationType {
    COUNTRY("Country level - Rwanda"),
    PROVINCE("Province level - Kigali, Southern, Western, Northern, Eastern"),
    DISTRICT("District level - Gasabo, Kicukiro, etc."),
    SECTOR("Sector level - Gisozi, Remera, etc."),
    CELL("Cell level - Administrative division under sector"),
    VILLAGE("Village level - Leaf node in hierarchy");

    private final String description;

    LocationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
