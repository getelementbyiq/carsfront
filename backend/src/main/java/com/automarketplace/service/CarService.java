package com.automarketplace.service;

import com.automarketplace.model.Car;
import com.automarketplace.model.CarStatus;
import com.automarketplace.model.User;
import com.automarketplace.repository.CarFirestoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service-Klasse für Car-Business-Logic mit Firestore
 */
@Service
public class CarService {
    
    private final CarFirestoreRepository carRepository;
    private final UserService userService;
    
    @Autowired
    public CarService(CarFirestoreRepository carRepository, UserService userService) {
        this.carRepository = carRepository;
        this.userService = userService;
    }
    
    /**
     * Erstelle neues Auto-Inserat
     */
    public Car createCar(Car car, String sellerFirebaseUid) {
        // Verkäufer validieren
        User seller = userService.findByFirebaseUid(sellerFirebaseUid)
            .orElseThrow(() -> new RuntimeException("Verkäufer nicht gefunden: " + sellerFirebaseUid));
        
        // Auto-Daten setzen
        car.setSellerId(seller.getFirebaseUid());
        car.setStatus(CarStatus.DRAFT);
        car.setCreatedAt(LocalDateTime.now());
        car.setUpdatedAt(LocalDateTime.now());
        
        return carRepository.save(car);
    }
    
    /**
     * Auto nach ID finden
     */
    public Optional<Car> findById(String id) {
        return carRepository.findById(id);
    }
    
    /**
     * Alle verfügbaren Autos finden
     */
    public List<Car> findAvailableCars() {
        return carRepository.findAvailableCars();
    }
    
    /**
     * Alle Autos abrufen
     */
    public List<Car> findAllCars() {
        return carRepository.findAll();
    }
    
    /**
     * Autos eines Verkäufers finden
     */
    public List<Car> findCarsBySeller(String sellerFirebaseUid) {
        User seller = userService.findByFirebaseUid(sellerFirebaseUid)
            .orElseThrow(() -> new RuntimeException("Verkäufer nicht gefunden: " + sellerFirebaseUid));
        
        return carRepository.findBySeller(seller);
    }
    
    /**
     * Autos eines Verkäufers nach Status finden
     */
    public List<Car> findCarsBySellerAndStatus(String sellerFirebaseUid, CarStatus status) {
        User seller = userService.findByFirebaseUid(sellerFirebaseUid)
            .orElseThrow(() -> new RuntimeException("Verkäufer nicht gefunden: " + sellerFirebaseUid));
        
        return carRepository.findBySellerAndStatus(seller, status);
    }
    
    /**
     * Auto aktualisieren
     */
    public Car updateCar(String id, Car updatedCar, String sellerFirebaseUid) {
        Car existingCar = carRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Auto nicht gefunden: " + id));
        
        // Prüfen ob der Benutzer der Besitzer ist
        if (!existingCar.getSellerId().equals(sellerFirebaseUid)) {
            throw new RuntimeException("Keine Berechtigung für dieses Auto");
        }
        
        // Aktualisierbare Felder setzen
        existingCar.setBrand(updatedCar.getBrand());
        existingCar.setModel(updatedCar.getModel());
        existingCar.setYear(updatedCar.getYear());
        existingCar.setPrice(updatedCar.getPrice());
        existingCar.setMileage(updatedCar.getMileage());
        existingCar.setFuelType(updatedCar.getFuelType());
        existingCar.setTransmission(updatedCar.getTransmission());
        existingCar.setCondition(updatedCar.getCondition());
        existingCar.setDescription(updatedCar.getDescription());
        existingCar.setImages(updatedCar.getImages());
        existingCar.setUpdatedAt(LocalDateTime.now());
        
        return carRepository.save(existingCar);
    }
    
    /**
     * Auto-Status ändern
     */
    public Car updateCarStatus(String id, CarStatus newStatus, String sellerFirebaseUid) {
        Car car = carRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Auto nicht gefunden: " + id));
        
        // Prüfen ob der Benutzer der Besitzer ist
        if (!car.getSellerId().equals(sellerFirebaseUid)) {
            throw new RuntimeException("Keine Berechtigung für dieses Auto");
        }
        
        car.setStatus(newStatus);
        car.setUpdatedAt(LocalDateTime.now());
        
        if (newStatus == CarStatus.SOLD) {
            car.setSoldAt(LocalDateTime.now());
        }
        
        return carRepository.save(car);
    }
    
    /**
     * Auto löschen
     */
    public void deleteCar(String id, String sellerFirebaseUid) {
        Car car = carRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Auto nicht gefunden: " + id));
        
        // Prüfen ob der Benutzer der Besitzer ist
        if (!car.getSellerId().equals(sellerFirebaseUid)) {
            throw new RuntimeException("Keine Berechtigung für dieses Auto");
        }
        
        carRepository.delete(car);
    }
    
    /**
     * Autos suchen
     */
    public List<Car> searchCars(String brand, String model, BigDecimal minPrice, BigDecimal maxPrice,
                               Integer minYear, Integer maxYear, String fuelType, String transmission) {
        
        List<Car> cars = carRepository.findAvailableCars();
        
        return cars.stream()
            .filter(car -> {
                if (brand != null && !brand.isEmpty() && !car.getBrand().toLowerCase().contains(brand.toLowerCase())) {
                    return false;
                }
                if (model != null && !model.isEmpty() && !car.getModel().toLowerCase().contains(model.toLowerCase())) {
                    return false;
                }
                if (minPrice != null && car.getPrice().compareTo(minPrice) < 0) {
                    return false;
                }
                if (maxPrice != null && car.getPrice().compareTo(maxPrice) > 0) {
                    return false;
                }
                if (minYear != null && car.getYear() < minYear) {
                    return false;
                }
                if (maxYear != null && car.getYear() > maxYear) {
                    return false;
                }
                if (fuelType != null && !fuelType.isEmpty() && !car.getFuelType().equalsIgnoreCase(fuelType)) {
                    return false;
                }
                if (transmission != null && !transmission.isEmpty() && !car.getTransmission().equalsIgnoreCase(transmission)) {
                    return false;
                }
                return true;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Autos nach Marke finden
     */
    public List<Car> findByBrand(String brand) {
        return carRepository.findByBrand(brand);
    }
    
    /**
     * Ähnliche Autos finden
     */
    public List<Car> findSimilarCars(String carId) {
        Car car = carRepository.findById(carId)
            .orElseThrow(() -> new RuntimeException("Auto nicht gefunden: " + carId));
        
        BigDecimal minPrice = car.getPrice().multiply(new BigDecimal("0.8"));
        BigDecimal maxPrice = car.getPrice().multiply(new BigDecimal("1.2"));
        
        return carRepository.findSimilarCars(car.getBrand(), minPrice, maxPrice, carId);
    }
    
    /**
     * Statistiken abrufen
     */
    public long getTotalCarsCount() {
        return carRepository.count();
    }
    
    public long getAvailableCarsCount() {
        return carRepository.countByStatus(CarStatus.AVAILABLE);
    }
    
    public long getSoldCarsCount() {
        return carRepository.countByStatus(CarStatus.SOLD);
    }
    
    public long getSellerCarsCount(String sellerFirebaseUid) {
        User seller = userService.findByFirebaseUid(sellerFirebaseUid)
            .orElseThrow(() -> new RuntimeException("Verkäufer nicht gefunden: " + sellerFirebaseUid));
        
        return carRepository.countBySellerAndStatus(seller, CarStatus.AVAILABLE);
    }
}