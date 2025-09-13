package com.automarketplace.controller;

import com.automarketplace.model.Car;
import com.automarketplace.model.CarStatus;
import com.automarketplace.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller für Auto-Operationen
 */
@RestController
@RequestMapping("/cars")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class CarController {
    
    @Autowired
    private CarService carService;
    
    /**
     * Alle verfügbaren Autos abrufen
     */
    @GetMapping
    public ResponseEntity<List<Car>> getAllAvailableCars() {
        try {
            List<Car> cars = carService.findAvailableCars();
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Auto nach ID abrufen
     */
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable String id) {
        try {
            Optional<Car> car = carService.findById(id);
            return car.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Neues Auto erstellen
     */
    @PostMapping
    public ResponseEntity<Car> createCar(@Valid @RequestBody CarRequestDTOs.CreateCarRequest request,
                                        Authentication authentication) {
        try {
            String sellerFirebaseUid = authentication.getName();
            
            Car car = new Car();
            car.setBrand(request.getBrand());
            car.setModel(request.getModel());
            car.setYear(request.getYear());
            car.setPrice(request.getPrice());
            car.setMileage(request.getMileage());
            car.setFuelType(request.getFuelType());
            car.setTransmission(request.getTransmission());
            car.setCondition(request.getCondition());
            car.setDescription(request.getDescription());
            car.setImages(request.getImages());
            
            Car createdCar = carService.createCar(car, sellerFirebaseUid);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Auto aktualisieren
     */
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable String id,
                                        @Valid @RequestBody CarRequestDTOs.UpdateCarRequest request,
                                        Authentication authentication) {
        try {
            String sellerFirebaseUid = authentication.getName();
            
            Car updatedCar = new Car();
            updatedCar.setBrand(request.getBrand());
            updatedCar.setModel(request.getModel());
            updatedCar.setYear(request.getYear());
            updatedCar.setPrice(request.getPrice());
            updatedCar.setMileage(request.getMileage());
            updatedCar.setFuelType(request.getFuelType());
            updatedCar.setTransmission(request.getTransmission());
            updatedCar.setCondition(request.getCondition());
            updatedCar.setDescription(request.getDescription());
            updatedCar.setImages(request.getImages());
            
            Car car = carService.updateCar(id, updatedCar, sellerFirebaseUid);
            return ResponseEntity.ok(car);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Auto-Status ändern
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Car> updateCarStatus(@PathVariable String id,
                                              @RequestBody CarRequestDTOs.UpdateStatusRequest request,
                                              Authentication authentication) {
        try {
            String sellerFirebaseUid = authentication.getName();
            Car car = carService.updateCarStatus(id, request.getStatus(), sellerFirebaseUid);
            return ResponseEntity.ok(car);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Auto löschen
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable String id, Authentication authentication) {
        try {
            String sellerFirebaseUid = authentication.getName();
            carService.deleteCar(id, sellerFirebaseUid);
            return ResponseEntity.noContent().build();
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Autos suchen
     */
    @GetMapping("/search")
    public ResponseEntity<List<Car>> searchCars(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(required = false) String fuelType,
            @RequestParam(required = false) String transmission) {
        
        try {
            List<Car> cars = carService.searchCars(brand, model, minPrice, maxPrice, 
                                                  minYear, maxYear, fuelType, transmission);
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Autos nach Marke finden
     */
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Car>> getCarsByBrand(@PathVariable String brand) {
        try {
            List<Car> cars = carService.findByBrand(brand);
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Ähnliche Autos finden
     */
    @GetMapping("/{id}/similar")
    public ResponseEntity<List<Car>> getSimilarCars(@PathVariable String id) {
        try {
            List<Car> cars = carService.findSimilarCars(id);
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Meine Autos abrufen (für Verkäufer)
     */
    @GetMapping("/my")
    public ResponseEntity<List<Car>> getMyCars(Authentication authentication) {
        try {
            String sellerFirebaseUid = authentication.getName();
            List<Car> cars = carService.findCarsBySeller(sellerFirebaseUid);
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Meine Autos nach Status abrufen
     */
    @GetMapping("/my/status/{status}")
    public ResponseEntity<List<Car>> getMyCarsByStatus(@PathVariable CarStatus status,
                                                      Authentication authentication) {
        try {
            String sellerFirebaseUid = authentication.getName();
            List<Car> cars = carService.findCarsBySellerAndStatus(sellerFirebaseUid, status);
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}