package com.automarketplace.model;

/**
 * Enum für Account-Status
 */
public enum AccountStatus {
    /**
     * Aktiver Account
     */
    ACTIVE("Aktiv"),
    
    /**
     * Inaktiver Account
     */
    INACTIVE("Inaktiv"),
    
    /**
     * Gesperrter Account
     */
    SUSPENDED("Gesperrt"),
    
    /**
     * Account wartet auf Verifizierung
     */
    PENDING_VERIFICATION("Warte auf Verifizierung");
    
    private final String displayName;
    
    AccountStatus(String displayName) {
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
