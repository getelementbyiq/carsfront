package com.automarketplace.model;

/**
 * Enum für User-Typen im Auto Marketplace
 */
public enum UserType {
    /**
     * Verkäufer - kann Autos verkaufen
     */
    SELLER("Verkäufer"),
    
    /**
     * Kunde - kann Autos kaufen/suchen
     */
    CUSTOMER("Kunde");
    
    private final String displayName;
    
    UserType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
