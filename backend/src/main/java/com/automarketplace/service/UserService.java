package com.automarketplace.service;

import com.automarketplace.model.User;
import com.automarketplace.model.UserType;
import com.automarketplace.repository.UserFirestoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service-Klasse für User-Business-Logic
 * 
 * Diese Klasse enthält die Geschäftslogik für User-Operationen.
 * Services sind die Schicht zwischen Controllern und Repositories.
 */
@Service
public class UserService {
    
    private final UserFirestoreRepository userRepository;
    
    /**
     * Constructor Injection - Spring injiziert automatisch UserFirestoreRepository
     * 
     * @param userRepository User Firestore Repository
     */
    @Autowired
    public UserService(UserFirestoreRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Erstelle oder aktualisiere User nach Firebase Authentication
     * 
     * Diese Methode wird aufgerufen, wenn sich ein User zum ersten Mal anmeldet
     * oder wenn User-Daten aktualisiert werden müssen.
     * 
     * @param firebaseUid Firebase UID
     * @param email Email-Adresse
     * @param firstName Vorname
     * @param lastName Nachname
     * @param userType User-Typ (SELLER oder CUSTOMER)
     * @return User
     */
    public User createOrUpdateUser(String firebaseUid, String email, String firstName, 
                                 String lastName, UserType userType) {
        
        // Prüfe ob User bereits existiert
        Optional<User> existingUser = userRepository.findByFirebaseUid(firebaseUid);
        
        if (existingUser.isPresent()) {
            // User existiert - aktualisiere Daten
            User user = existingUser.get();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setLastLoginAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            
            return userRepository.save(user);
        } else {
            // Neuer User - erstelle neuen Eintrag
            User newUser = new User(firebaseUid, email, firstName, lastName, userType);
            newUser.setLastLoginAt(LocalDateTime.now());
            
            return userRepository.save(newUser);
        }
    }
    
    /**
     * Finde User anhand Firebase UID
     * 
     * @param firebaseUid Firebase UID
     * @return Optional<User>
     */
    public Optional<User> findByFirebaseUid(String firebaseUid) {
        return userRepository.findByFirebaseUid(firebaseUid);
    }
    
    /**
     * Finde User anhand ID
     * 
     * @param userId User ID
     * @return Optional<User>
     */
    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }
    
    /**
     * Finde User anhand Email
     * 
     * @param email Email-Adresse
     * @return Optional<User>
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Aktualisiere User-Profil
     * 
     * @param userId User ID
     * @param firstName Vorname
     * @param lastName Nachname
     * @param phoneNumber Telefonnummer
     * @param profileImageUrl Profilbild URL
     * @return User
     * @throws RuntimeException wenn User nicht gefunden
     */
    public User updateProfile(String userId, String firstName, String lastName, 
                            String phoneNumber, String profileImageUrl) {
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User nicht gefunden: " + userId));
        
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setProfileImageUrl(profileImageUrl);
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }
    
    /**
     * Aktualisiere Seller-spezifische Informationen
     * 
     * @param userId User ID
     * @param companyName Firmenname
     * @param businessLicense Gewerbeschein
     * @param address Adresse
     * @param specializations Spezialisierungen
     * @return User
     * @throws RuntimeException wenn User nicht gefunden oder kein Seller
     */
    public User updateSellerInfo(String userId, String companyName, String businessLicense, 
                               String address, List<String> specializations) {
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User nicht gefunden: " + userId));
        
        if (!user.isSeller()) {
            throw new RuntimeException("User ist kein Seller: " + userId);
        }
        
        user.setCompanyName(companyName);
        user.setBusinessLicense(businessLicense);
        user.setAddress(address);
        user.setSpecializations(specializations);
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }
    
    /**
     * Finde alle aktiven Seller
     * 
     * @return List<User>
     */
    public List<User> findActiveSellers() {
        return userRepository.findActiveSellers();
    }
    
    /**
     * Finde alle aktiven Customer
     * 
     * @return List<User>
     */
    public List<User> findActiveCustomers() {
        return userRepository.findActiveCustomers();
    }
    
    /**
     * Suche User nach Namen
     * 
     * @param searchTerm Suchbegriff
     * @return List<User>
     */
    public List<User> searchUsersByName(String searchTerm) {
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            searchTerm, searchTerm);
    }
    
    /**
     * Finde Seller nach Spezialisierung
     * 
     * @param specialization Spezialisierung
     * @return List<User>
     */
    public List<User> findSellersBySpecialization(String specialization) {
        return userRepository.findSellersBySpecialization(specialization);
    }
    
    /**
     * Prüfe ob Email bereits verwendet wird
     * 
     * @param email Email-Adresse
     * @return boolean
     */
    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * Prüfe ob Firebase UID bereits registriert ist
     * 
     * @param firebaseUid Firebase UID
     * @return boolean
     */
    public boolean isFirebaseUidRegistered(String firebaseUid) {
        return userRepository.existsByFirebaseUid(firebaseUid);
    }
    
    /**
     * Lösche User (Soft Delete - setze Status auf INACTIVE)
     * 
     * @param userId User ID
     * @throws RuntimeException wenn User nicht gefunden
     */
    public void deactivateUser(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User nicht gefunden: " + userId));
        
        user.setStatus(com.automarketplace.model.AccountStatus.INACTIVE);
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
    }
}
