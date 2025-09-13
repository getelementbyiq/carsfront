package com.automarketplace.repository;

import com.automarketplace.model.Car;
import com.automarketplace.model.CarStatus;
import com.automarketplace.service.FirestoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository für Car-Operationen mit Firestore
 */
@Repository
public class CarFirestoreRepository {
    
    private static final String COLLECTION_NAME = "cars";
    
    @Autowired
    private FirestoreService firestoreService;
    
    /**
     * Auto speichern
     */
    public Car save(Car car) {
        String documentId = firestoreService.saveDocument(COLLECTION_NAME, car.getId(), car);
        car.setId(documentId);
        return car;
    }
    
    /**
     * Auto nach ID finden
     */
    public Optional<Car> findById(String id) {
        Car car = firestoreService.getDocument(COLLECTION_NAME, id, Car.class);
        return Optional.ofNullable(car);
    }
    
    /**
     * Alle Autos abrufen
     */
    public List<Car> findAll() {
        return firestoreService.getAllDocuments(COLLECTION_NAME, Car.class);
    }
    
    /**
     * Autos nach Verkäufer-ID finden
     */
    public List<Car> findBySellerId(String sellerId) {
        return firestoreService.queryDocuments(COLLECTION_NAME, "sellerId", sellerId, Car.class);
    }
    
    /**
     * Autos nach Status finden
     */
    public List<Car> findByStatus(CarStatus status) {
        return firestoreService.queryDocuments(COLLECTION_NAME, "status", status.toString(), Car.class);
    }
    
    /**
     * Verfügbare Autos finden (Status = AVAILABLE)
     */
    public List<Car> findAvailableCars() {
        return firestoreService.queryDocuments(COLLECTION_NAME, "status", "AVAILABLE", Car.class);
    }
    
    /**
     * Autos nach Marke finden
     */
    public List<Car> findByBrand(String brand) {
        return firestoreService.queryDocuments(COLLECTION_NAME, "brand", brand, Car.class);
    }
    
    /**
     * Autos nach Marke und Modell finden
     */
    public List<Car> findByBrandAndModel(String brand, String model) {
        // Für komplexere Queries müssen wir alle Autos laden und filtern
        // In einer produktiven Anwendung würde man Composite Indexes verwenden
        return findAll().stream()
            .filter(car -> brand.equalsIgnoreCase(car.getBrand()) && 
                          model.equalsIgnoreCase(car.getModel()))
            .collect(Collectors.toList());
    }
    
    /**
     * Autos in Preisbereich finden
     */
    public List<Car> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return findAll().stream()
            .filter(car -> car.getPrice().compareTo(minPrice) >= 0 && 
                          car.getPrice().compareTo(maxPrice) <= 0)
            .collect(Collectors.toList());
    }
    
    /**
     * Autos nach Jahr finden
     */
    public List<Car> findByYear(Integer year) {
        return firestoreService.queryDocuments(COLLECTION_NAME, "year", year, Car.class);
    }
    
    /**
     * Autos in Jahresbereich finden
     */
    public List<Car> findByYearBetween(Integer startYear, Integer endYear) {
        return findAll().stream()
            .filter(car -> car.getYear() >= startYear && car.getYear() <= endYear)
            .collect(Collectors.toList());
    }
    
    /**
     * Auto löschen
     */
    public void delete(Car car) {
        firestoreService.deleteDocument(COLLECTION_NAME, car.getId());
    }
    
    /**
     * Auto nach ID löschen
     */
    public void deleteById(String id) {
        firestoreService.deleteDocument(COLLECTION_NAME, id);
    }
    
    /**
     * Prüfen ob Auto existiert
     */
    public boolean existsById(String id) {
        return firestoreService.documentExists(COLLECTION_NAME, id);
    }
    
    /**
     * Anzahl Autos zählen
     */
    public long count() {
        return findAll().size();
    }
    
    /**
     * Anzahl Autos eines Verkäufers zählen
     */
    public long countBySellerId(String sellerId) {
        return findBySellerId(sellerId).size();
    }
    
    /**
     * Autos nach Verkäufer finden (für User-Objekt)
     */
    public List<Car> findBySeller(com.automarketplace.model.User seller) {
        return findBySellerId(seller.getFirebaseUid());
    }
    
    /**
     * Autos nach Verkäufer und Status finden
     */
    public List<Car> findBySellerAndStatus(com.automarketplace.model.User seller, CarStatus status) {
        return findAll().stream()
            .filter(car -> seller.getFirebaseUid().equals(car.getSellerId()) && 
                          status.toString().equals(car.getStatus().toString()))
            .collect(Collectors.toList());
    }
    
    /**
     * Ähnliche Autos finden
     */
    public List<Car> findSimilarCars(String brand, java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice, String excludeId) {
        return findAll().stream()
            .filter(car -> !car.getId().equals(excludeId) &&
                          brand.equalsIgnoreCase(car.getBrand()) &&
                          car.getPrice().compareTo(minPrice) >= 0 &&
                          car.getPrice().compareTo(maxPrice) <= 0)
            .collect(Collectors.toList());
    }
    
    /**
     * Anzahl Autos nach Status zählen
     */
    public long countByStatus(CarStatus status) {
        return findByStatus(status).size();
    }
    
    /**
     * Anzahl Autos nach Verkäufer und Status zählen
     */
    public long countBySellerAndStatus(com.automarketplace.model.User seller, CarStatus status) {
        return findBySellerAndStatus(seller, status).size();
    }
}
