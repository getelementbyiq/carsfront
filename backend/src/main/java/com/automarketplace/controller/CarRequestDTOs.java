package com.automarketplace.controller;

import com.automarketplace.model.Car;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Objects (DTOs) für Car-Requests
 * 
 * Diese Klassen definieren die Struktur der JSON-Requests vom Frontend.
 */

/**
 * DTO für das Erstellen eines neuen Autos
 */
class CreateCarRequest {
    
    @NotBlank(message = "Marke ist erforderlich")
    private String brand;
    
    @NotBlank(message = "Modell ist erforderlich")
    private String model;
    
    @NotNull(message = "Baujahr ist erforderlich")
    @Min(value = 1900, message = "Baujahr muss nach 1900 sein")
    @Max(value = 2030, message = "Baujahr kann nicht in der Zukunft liegen")
    private Integer year;
    
    @NotNull(message = "Preis ist erforderlich")
    @DecimalMin(value = "0.0", inclusive = false, message = "Preis muss größer als 0 sein")
    private BigDecimal price;
    
    @NotNull(message = "Kilometerstand ist erforderlich")
    @Min(value = 0, message = "Kilometerstand kann nicht negativ sein")
    private Integer mileage;
    
    @NotBlank(message = "Kraftstoffart ist erforderlich")
    private String fuelType;
    
    @NotBlank(message = "Getriebe ist erforderlich")
    private String transmission;
    
    private String color;
    
    @Min(value = 2, message = "Mindestens 2 Türen")
    @Max(value = 6, message = "Maximal 6 Türen")
    private Integer doors;
    
    @Min(value = 1, message = "Mindestens 1 Sitzplatz")
    @Max(value = 9, message = "Maximal 9 Sitzplätze")
    private Integer seats;
    
    private String bodyType;
    private String engineSize;
    
    @Min(value = 1, message = "PS muss positiv sein")
    private Integer horsepower;
    
    private String drivetrain;
    
    @NotBlank(message = "Fahrzeugzustand ist erforderlich")
    private String condition;
    
    @Min(value = 0, message = "Anzahl Vorbesitzer kann nicht negativ sein")
    private Integer previousOwners;
    
    private boolean accidentFree = true;
    private String serviceHistory;
    private List<String> features;
    private List<String> imageUrls;
    private String mainImageUrl;
    
    @NotBlank(message = "Beschreibung ist erforderlich")
    @Size(min = 50, message = "Beschreibung muss mindestens 50 Zeichen lang sein")
    private String description;
    
    private String location;
    private String zipCode;
    
    // Konstruktor
    public CreateCarRequest() {}
    
    // Konvertiere zu Car Entity
    public Car toCar() {
        Car car = new Car();
        car.setBrand(this.brand);
        car.setModel(this.model);
        car.setYear(this.year);
        car.setPrice(this.price);
        car.setMileage(this.mileage);
        car.setFuelType(this.fuelType);
        car.setTransmission(this.transmission);
        car.setColor(this.color);
        car.setDoors(this.doors);
        car.setSeats(this.seats);
        car.setBodyType(this.bodyType);
        car.setEngineSize(this.engineSize);
        car.setHorsepower(this.horsepower);
        car.setDrivetrain(this.drivetrain);
        car.setCondition(this.condition);
        car.setPreviousOwners(this.previousOwners);
        car.setAccidentFree(this.accidentFree);
        car.setServiceHistory(this.serviceHistory);
        car.setFeatures(this.features);
        car.setImageUrls(this.imageUrls);
        car.setMainImageUrl(this.mainImageUrl);
        car.setDescription(this.description);
        car.setLocation(this.location);
        car.setZipCode(this.zipCode);
        return car;
    }
    
    // Getter und Setter
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getMileage() { return mileage; }
    public void setMileage(Integer mileage) { this.mileage = mileage; }
    
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    
    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public Integer getDoors() { return doors; }
    public void setDoors(Integer doors) { this.doors = doors; }
    
    public Integer getSeats() { return seats; }
    public void setSeats(Integer seats) { this.seats = seats; }
    
    public String getBodyType() { return bodyType; }
    public void setBodyType(String bodyType) { this.bodyType = bodyType; }
    
    public String getEngineSize() { return engineSize; }
    public void setEngineSize(String engineSize) { this.engineSize = engineSize; }
    
    public Integer getHorsepower() { return horsepower; }
    public void setHorsepower(Integer horsepower) { this.horsepower = horsepower; }
    
    public String getDrivetrain() { return drivetrain; }
    public void setDrivetrain(String drivetrain) { this.drivetrain = drivetrain; }
    
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    
    public Integer getPreviousOwners() { return previousOwners; }
    public void setPreviousOwners(Integer previousOwners) { this.previousOwners = previousOwners; }
    
    public boolean isAccidentFree() { return accidentFree; }
    public void setAccidentFree(boolean accidentFree) { this.accidentFree = accidentFree; }
    
    public String getServiceHistory() { return serviceHistory; }
    public void setServiceHistory(String serviceHistory) { this.serviceHistory = serviceHistory; }
    
    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }
    
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    
    public String getMainImageUrl() { return mainImageUrl; }
    public void setMainImageUrl(String mainImageUrl) { this.mainImageUrl = mainImageUrl; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
}

/**
 * DTO für das Aktualisieren eines Autos
 * (ähnlich wie CreateCarRequest, aber alle Felder optional)
 */
class UpdateCarRequest {
    
    private String brand;
    private String model;
    
    @Min(value = 1900, message = "Baujahr muss nach 1900 sein")
    @Max(value = 2030, message = "Baujahr kann nicht in der Zukunft liegen")
    private Integer year;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Preis muss größer als 0 sein")
    private BigDecimal price;
    
    @Min(value = 0, message = "Kilometerstand kann nicht negativ sein")
    private Integer mileage;
    
    private String fuelType;
    private String transmission;
    private String color;
    
    @Min(value = 2, message = "Mindestens 2 Türen")
    @Max(value = 6, message = "Maximal 6 Türen")
    private Integer doors;
    
    @Min(value = 1, message = "Mindestens 1 Sitzplatz")
    @Max(value = 9, message = "Maximal 9 Sitzplätze")
    private Integer seats;
    
    private String bodyType;
    private String engineSize;
    
    @Min(value = 1, message = "PS muss positiv sein")
    private Integer horsepower;
    
    private String drivetrain;
    private String condition;
    
    @Min(value = 0, message = "Anzahl Vorbesitzer kann nicht negativ sein")
    private Integer previousOwners;
    
    private Boolean accidentFree;
    private String serviceHistory;
    private List<String> features;
    private List<String> imageUrls;
    private String mainImageUrl;
    
    @Size(min = 50, message = "Beschreibung muss mindestens 50 Zeichen lang sein")
    private String description;
    
    private String location;
    private String zipCode;
    
    // Konstruktor
    public UpdateCarRequest() {}
    
    // Konvertiere zu Car Entity (nur gesetzte Felder)
    public Car toCar() {
        Car car = new Car();
        if (this.brand != null) car.setBrand(this.brand);
        if (this.model != null) car.setModel(this.model);
        if (this.year != null) car.setYear(this.year);
        if (this.price != null) car.setPrice(this.price);
        if (this.mileage != null) car.setMileage(this.mileage);
        if (this.fuelType != null) car.setFuelType(this.fuelType);
        if (this.transmission != null) car.setTransmission(this.transmission);
        if (this.color != null) car.setColor(this.color);
        if (this.doors != null) car.setDoors(this.doors);
        if (this.seats != null) car.setSeats(this.seats);
        if (this.bodyType != null) car.setBodyType(this.bodyType);
        if (this.engineSize != null) car.setEngineSize(this.engineSize);
        if (this.horsepower != null) car.setHorsepower(this.horsepower);
        if (this.drivetrain != null) car.setDrivetrain(this.drivetrain);
        if (this.condition != null) car.setCondition(this.condition);
        if (this.previousOwners != null) car.setPreviousOwners(this.previousOwners);
        if (this.accidentFree != null) car.setAccidentFree(this.accidentFree);
        if (this.serviceHistory != null) car.setServiceHistory(this.serviceHistory);
        if (this.features != null) car.setFeatures(this.features);
        if (this.imageUrls != null) car.setImageUrls(this.imageUrls);
        if (this.mainImageUrl != null) car.setMainImageUrl(this.mainImageUrl);
        if (this.description != null) car.setDescription(this.description);
        if (this.location != null) car.setLocation(this.location);
        if (this.zipCode != null) car.setZipCode(this.zipCode);
        return car;
    }
    
    // Getter und Setter (gleich wie CreateCarRequest, aber alle optional)
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getMileage() { return mileage; }
    public void setMileage(Integer mileage) { this.mileage = mileage; }
    
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    
    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public Integer getDoors() { return doors; }
    public void setDoors(Integer doors) { this.doors = doors; }
    
    public Integer getSeats() { return seats; }
    public void setSeats(Integer seats) { this.seats = seats; }
    
    public String getBodyType() { return bodyType; }
    public void setBodyType(String bodyType) { this.bodyType = bodyType; }
    
    public String getEngineSize() { return engineSize; }
    public void setEngineSize(String engineSize) { this.engineSize = engineSize; }
    
    public Integer getHorsepower() { return horsepower; }
    public void setHorsepower(Integer horsepower) { this.horsepower = horsepower; }
    
    public String getDrivetrain() { return drivetrain; }
    public void setDrivetrain(String drivetrain) { this.drivetrain = drivetrain; }
    
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    
    public Integer getPreviousOwners() { return previousOwners; }
    public void setPreviousOwners(Integer previousOwners) { this.previousOwners = previousOwners; }
    
    public Boolean getAccidentFree() { return accidentFree; }
    public void setAccidentFree(Boolean accidentFree) { this.accidentFree = accidentFree; }
    
    public String getServiceHistory() { return serviceHistory; }
    public void setServiceHistory(String serviceHistory) { this.serviceHistory = serviceHistory; }
    
    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }
    
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    
    public String getMainImageUrl() { return mainImageUrl; }
    public void setMainImageUrl(String mainImageUrl) { this.mainImageUrl = mainImageUrl; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
}
