package com.automarketplace.model;

/**
 * Enum für Car-Status im Marketplace
 */
public enum CarStatus {
    /**
     * Auto ist aktiv und verfügbar
     */
    ACTIVE("Verfügbar"),
    
    /**
     * Auto ist verkauft
     */
    SOLD("Verkauft"),
    
    /**
     * Auto ist inaktiv/pausiert
     */
    INACTIVE("Inaktiv"),
    
    /**
     * Auto wartet auf Genehmigung
     */
    PENDING_APPROVAL("Warte auf Genehmigung"),
    
    /**
     * Auto ist abgelehnt
     */
    REJECTED("Abgelehnt");
    
    private final String displayName;
    
    CarStatus(String displayName) {
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
