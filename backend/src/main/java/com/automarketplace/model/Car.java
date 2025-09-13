package com.automarketplace.model;

import com.google.cloud.firestore.annotation.DocumentId;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Car Entity - Repräsentiert ein Auto im Marketplace
 * Firestore Collection: "cars"
 */
public class Car {
    
    @DocumentId
    private String id;
    
    /**
     * Firebase UID des Verkäufers
     */
    @NotBlank(message = "Verkäufer UID ist erforderlich")
    private String sellerId;
    
    // Grundlegende Auto-Informationen
    @NotBlank(message = "Marke ist erforderlich")
    private String brand; // z.B. "BMW", "Mercedes", "Audi"
    
    @NotBlank(message = "Modell ist erforderlich")
    private String model; // z.B. "3er", "C-Klasse", "A4"
    
    @NotNull(message = "Baujahr ist erforderlich")
    @Min(value = 1900, message = "Baujahr muss nach 1900 sein")
    private Integer year;
    
    @NotNull(message = "Preis ist erforderlich")
    @Positive(message = "Preis muss positiv sein")
    private BigDecimal price;
    
    @NotNull(message = "Kilometerstand ist erforderlich")
    @Min(value = 0, message = "Kilometerstand kann nicht negativ sein")
    private Integer mileage; // Kilometerstand
    
    @NotBlank(message = "Kraftstoffart ist erforderlich")
    private String fuelType; // "Benzin", "Diesel", "Elektro", "Hybrid"
    
    @NotBlank(message = "Getriebe ist erforderlich")
    private String transmission; // "Manuell", "Automatik"
    
    private String color;
    private Integer doors; // Anzahl Türen
    private Integer seats; // Anzahl Sitzplätze
    private String bodyType; // "Limousine", "Kombi", "SUV", "Cabrio", etc.
    
    // Technische Details
    private String engineSize; // z.B. "2.0L"
    private Integer horsepower; // PS
    private String drivetrain; // "Frontantrieb", "Heckantrieb", "Allrad"
    
    // Zustand und Historie
    @NotBlank(message = "Fahrzeugzustand ist erforderlich")
    private String condition; // "Neu", "Gebraucht", "Unfallfrei", etc.
    private Integer previousOwners; // Anzahl Vorbesitzer
    private boolean accidentFree = true;
    private String serviceHistory; // Wartungshistorie
    
    // Ausstattung und Features
    private List<String> features; // z.B. ["Klimaanlage", "Navigation", "Ledersitze"]
    
    // Bilder
    private List<String> imageUrls; // Cloudinary URLs
    private String mainImageUrl; // Hauptbild
    
    // Beschreibung
    @NotBlank(message = "Beschreibung ist erforderlich")
    private String description;
    
    // Standort
    private String location; // Stadt/Region
    private String zipCode;
    
    // Status
    private CarStatus status = CarStatus.ACTIVE;
    
    // Timestamps
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private LocalDateTime soldAt;
    
    // Konstruktoren
    public Car() {}
    
    public Car(String sellerId, String brand, String model, Integer year, BigDecimal price, 
               Integer mileage, String fuelType, String transmission, String condition, String description) {
        this.sellerId = sellerId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.mileage = mileage;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.condition = condition;
        this.description = description;
    }
    
    // Getter und Setter
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getSellerId() {
        return sellerId;
    }
    
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getMileage() {
        return mileage;
    }
    
    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }
    
    public String getFuelType() {
        return fuelType;
    }
    
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
    
    public String getTransmission() {
        return transmission;
    }
    
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public Integer getDoors() {
        return doors;
    }
    
    public void setDoors(Integer doors) {
        this.doors = doors;
    }
    
    public Integer getSeats() {
        return seats;
    }
    
    public void setSeats(Integer seats) {
        this.seats = seats;
    }
    
    public String getBodyType() {
        return bodyType;
    }
    
    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }
    
    public String getEngineSize() {
        return engineSize;
    }
    
    public void setEngineSize(String engineSize) {
        this.engineSize = engineSize;
    }
    
    public Integer getHorsepower() {
        return horsepower;
    }
    
    public void setHorsepower(Integer horsepower) {
        this.horsepower = horsepower;
    }
    
    public String getDrivetrain() {
        return drivetrain;
    }
    
    public void setDrivetrain(String drivetrain) {
        this.drivetrain = drivetrain;
    }
    
    public String getCondition() {
        return condition;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    public Integer getPreviousOwners() {
        return previousOwners;
    }
    
    public void setPreviousOwners(Integer previousOwners) {
        this.previousOwners = previousOwners;
    }
    
    public boolean isAccidentFree() {
        return accidentFree;
    }
    
    public void setAccidentFree(boolean accidentFree) {
        this.accidentFree = accidentFree;
    }
    
    public String getServiceHistory() {
        return serviceHistory;
    }
    
    public void setServiceHistory(String serviceHistory) {
        this.serviceHistory = serviceHistory;
    }
    
    public List<String> getFeatures() {
        return features;
    }
    
    public void setFeatures(List<String> features) {
        this.features = features;
    }
    
    public List<String> getImageUrls() {
        return imageUrls;
    }
    
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
    
    public String getMainImageUrl() {
        return mainImageUrl;
    }
    
    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public CarStatus getStatus() {
        return status;
    }
    
    public void setStatus(CarStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public LocalDateTime getSoldAt() {
        return soldAt;
    }
    
    public void setSoldAt(LocalDateTime soldAt) {
        this.soldAt = soldAt;
    }
    
    // Hilfsmethoden
    public String getFullName() {
        return brand + " " + model + " (" + year + ")";
    }
    
    public boolean isAvailable() {
        return status == CarStatus.ACTIVE;
    }
    
    public boolean isSold() {
        return status == CarStatus.SOLD;
    }
}
