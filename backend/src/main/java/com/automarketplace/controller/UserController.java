package com.automarketplace.controller;

import com.automarketplace.model.User;
import com.automarketplace.model.UserType;
import com.automarketplace.security.FirebaseUserDetails;
import com.automarketplace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller für User-Management
 * 
 * Dieser Controller behandelt alle HTTP-Requests bezüglich User-Operationen.
 * Alle Endpoints sind authentifiziert (außer explizit als öffentlich markiert).
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Erstelle oder aktualisiere User-Profil
     * 
     * Dieser Endpoint wird nach der Firebase-Authentifizierung aufgerufen,
     * um den User in unserer Datenbank zu registrieren oder zu aktualisieren.
     * 
     * POST /api/users/profile
     * 
     * @param userRequest User-Daten vom Frontend
     * @param authentication Spring Security Authentication (automatisch injiziert)
     * @return ResponseEntity<User>
     */
    @PostMapping("/profile")
    public ResponseEntity<?> createOrUpdateProfile(@Valid @RequestBody CreateUserRequest userRequest, 
                                                  Authentication authentication) {
        try {
            // Extrahiere Firebase UID aus Authentication
            String firebaseUid = (String) authentication.getPrincipal();
            FirebaseUserDetails userDetails = (FirebaseUserDetails) authentication.getDetails();
            
            // Erstelle oder aktualisiere User
            User user = userService.createOrUpdateUser(
                firebaseUid,
                userDetails.getEmail(),
                userRequest.getFirstName(),
                userRequest.getLastName(),
                userRequest.getUserType()
            );
            
            return ResponseEntity.ok(user);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Fehler beim Erstellen des Profils: " + e.getMessage()));
        }
    }
    
    /**
     * Hole aktuelles User-Profil
     * 
     * GET /api/users/me
     * 
     * @param authentication Spring Security Authentication
     * @return ResponseEntity<User>
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            String firebaseUid = (String) authentication.getPrincipal();
            
            Optional<User> user = userService.findByFirebaseUid(firebaseUid);
            
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User-Profil nicht gefunden"));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Fehler beim Laden des Profils: " + e.getMessage()));
        }
    }
    
    /**
     * Aktualisiere User-Profil
     * 
     * PUT /api/users/me
     * 
     * @param updateRequest Update-Daten
     * @param authentication Spring Security Authentication
     * @return ResponseEntity<User>
     */
    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateUserRequest updateRequest,
                                          Authentication authentication) {
        try {
            String firebaseUid = (String) authentication.getPrincipal();
            
            // Finde User
            Optional<User> userOpt = userService.findByFirebaseUid(firebaseUid);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User nicht gefunden"));
            }
            
            // Aktualisiere Profil
            User updatedUser = userService.updateProfile(
                userOpt.get().getId(),
                updateRequest.getFirstName(),
                updateRequest.getLastName(),
                updateRequest.getPhoneNumber(),
                updateRequest.getProfileImageUrl()
            );
            
            return ResponseEntity.ok(updatedUser);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Fehler beim Aktualisieren des Profils: " + e.getMessage()));
        }
    }
    
    /**
     * Aktualisiere Seller-Informationen
     * 
     * PUT /api/users/seller-info
     * 
     * @param sellerRequest Seller-spezifische Daten
     * @param authentication Spring Security Authentication
     * @return ResponseEntity<User>
     */
    @PutMapping("/seller-info")
    public ResponseEntity<?> updateSellerInfo(@Valid @RequestBody UpdateSellerRequest sellerRequest,
                                             Authentication authentication) {
        try {
            String firebaseUid = (String) authentication.getPrincipal();
            
            // Finde User
            Optional<User> userOpt = userService.findByFirebaseUid(firebaseUid);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User nicht gefunden"));
            }
            
            User user = userOpt.get();
            if (!user.isSeller()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Nur Seller können diese Informationen aktualisieren"));
            }
            
            // Aktualisiere Seller-Informationen
            User updatedUser = userService.updateSellerInfo(
                user.getId(),
                sellerRequest.getCompanyName(),
                sellerRequest.getBusinessLicense(),
                sellerRequest.getAddress(),
                sellerRequest.getSpecializations()
            );
            
            return ResponseEntity.ok(updatedUser);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Fehler beim Aktualisieren der Seller-Informationen: " + e.getMessage()));
        }
    }
    
    /**
     * Hole alle aktiven Seller (öffentlich)
     * 
     * GET /api/users/sellers
     * 
     * @return ResponseEntity<List<User>>
     */
    @GetMapping("/sellers")
    public ResponseEntity<?> getActiveSellers() {
        try {
            List<User> sellers = userService.findActiveSellers();
            return ResponseEntity.ok(sellers);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Fehler beim Laden der Seller: " + e.getMessage()));
        }
    }
    
    /**
     * Suche Seller nach Spezialisierung
     * 
     * GET /api/users/sellers/search?specialization=BMW
     * 
     * @param specialization Spezialisierung
     * @return ResponseEntity<List<User>>
     */
    @GetMapping("/sellers/search")
    public ResponseEntity<?> searchSellersBySpecialization(@RequestParam String specialization) {
        try {
            List<User> sellers = userService.findSellersBySpecialization(specialization);
            return ResponseEntity.ok(sellers);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Fehler bei der Seller-Suche: " + e.getMessage()));
        }
    }
    
    /**
     * Lösche User-Account (Soft Delete)
     * 
     * DELETE /api/users/me
     * 
     * @param authentication Spring Security Authentication
     * @return ResponseEntity<?>
     */
    @DeleteMapping("/me")
    public ResponseEntity<?> deactivateAccount(Authentication authentication) {
        try {
            String firebaseUid = (String) authentication.getPrincipal();
            
            // Finde User
            Optional<User> userOpt = userService.findByFirebaseUid(firebaseUid);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User nicht gefunden"));
            }
            
            // Deaktiviere Account
            userService.deactivateUser(userOpt.get().getId());
            
            return ResponseEntity.ok(Map.of("message", "Account erfolgreich deaktiviert"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Fehler beim Deaktivieren des Accounts: " + e.getMessage()));
        }
    }
}

/**
 * Request DTOs (Data Transfer Objects)
 */

class CreateUserRequest {
    private String firstName;
    private String lastName;
    private UserType userType;
    
    // Getter und Setter
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }
}

class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profileImageUrl;
    
    // Getter und Setter
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
}

class UpdateSellerRequest {
    private String companyName;
    private String businessLicense;
    private String address;
    private List<String> specializations;
    
    // Getter und Setter
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    
    public String getBusinessLicense() { return businessLicense; }
    public void setBusinessLicense(String businessLicense) { this.businessLicense = businessLicense; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public List<String> getSpecializations() { return specializations; }
    public void setSpecializations(List<String> specializations) { this.specializations = specializations; }
}
