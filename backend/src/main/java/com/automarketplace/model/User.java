package com.automarketplace.model;

import com.google.cloud.firestore.annotation.DocumentId;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User Entity - Basis-Klasse für alle Benutzer
 * 
 * Firestore Collection: "users"
 */
public class User {
    
    @DocumentId
    private String id;
    
    /**
     * Firebase UID - eindeutige Identifikation von Firebase Auth
     * Wird als Document ID verwendet
     */
    @NotBlank(message = "Firebase UID ist erforderlich")
    private String firebaseUid;
    
    @NotBlank(message = "Email ist erforderlich")
    @Email(message = "Ungültige Email-Adresse")
    private String email;
    
    @NotBlank(message = "Vorname ist erforderlich")
    private String firstName;
    
    @NotBlank(message = "Nachname ist erforderlich")
    private String lastName;
    
    private String phoneNumber;
    private String profileImageUrl;
    
    /**
     * User-Typ: SELLER oder CUSTOMER
     */
    @NotNull(message = "User-Typ ist erforderlich")
    private UserType userType;
    
    /**
     * Account Status
     */
    private AccountStatus status = AccountStatus.ACTIVE;
    
    /**
     * Timestamps
     */
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private LocalDateTime lastLoginAt;
    
    /**
     * Zusätzliche Felder für Seller
     */
    private String companyName;
    private String businessLicense;
    private String address;
    private List<String> specializations; // z.B. ["BMW", "Mercedes", "Audi"]
    
    // Konstruktoren
    public User() {}
    
    public User(String firebaseUid, String email, String firstName, String lastName, UserType userType) {
        this.firebaseUid = firebaseUid;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
    }
    
    // Getter und Setter
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getFirebaseUid() {
        return firebaseUid;
    }
    
    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    
    public UserType getUserType() {
        return userType;
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    public AccountStatus getStatus() {
        return status;
    }
    
    public void setStatus(AccountStatus status) {
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
    
    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }
    
    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getBusinessLicense() {
        return businessLicense;
    }
    
    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public List<String> getSpecializations() {
        return specializations;
    }
    
    public void setSpecializations(List<String> specializations) {
        this.specializations = specializations;
    }
    
    /**
     * Hilfsmethoden
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public boolean isSeller() {
        return userType == UserType.SELLER;
    }
    
    public boolean isCustomer() {
        return userType == UserType.CUSTOMER;
    }
    
    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }
}
