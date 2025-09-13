package com.automarketplace.repository;

import com.automarketplace.model.User;
import com.automarketplace.service.FirestoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository für User-Operationen mit Firestore
 */
@Repository
public class UserFirestoreRepository {
    
    private static final String COLLECTION_NAME = "users";
    
    @Autowired
    private FirestoreService firestoreService;
    
    /**
     * Benutzer speichern
     * Verwendet Firebase UID als Document ID
     */
    public User save(User user) {
        String documentId = user.getFirebaseUid();
        firestoreService.saveDocument(COLLECTION_NAME, documentId, user);
        user.setId(documentId);
        return user;
    }
    
    /**
     * Benutzer nach Firebase UID finden
     */
    public Optional<User> findByFirebaseUid(String firebaseUid) {
        User user = firestoreService.getDocument(COLLECTION_NAME, firebaseUid, User.class);
        return Optional.ofNullable(user);
    }
    
    /**
     * Benutzer nach ID finden
     */
    public Optional<User> findById(String id) {
        return findByFirebaseUid(id);
    }
    
    /**
     * Alle Benutzer abrufen
     */
    public List<User> findAll() {
        return firestoreService.getAllDocuments(COLLECTION_NAME, User.class);
    }
    
    /**
     * Benutzer nach E-Mail finden
     */
    public Optional<User> findByEmail(String email) {
        List<User> users = firestoreService.queryDocuments(COLLECTION_NAME, "email", email, User.class);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
    
    /**
     * Benutzer löschen
     */
    public void delete(User user) {
        firestoreService.deleteDocument(COLLECTION_NAME, user.getFirebaseUid());
    }
    
    /**
     * Benutzer nach Firebase UID löschen
     */
    public void deleteByFirebaseUid(String firebaseUid) {
        firestoreService.deleteDocument(COLLECTION_NAME, firebaseUid);
    }
    
    /**
     * Prüfen ob Benutzer existiert
     */
    public boolean existsByFirebaseUid(String firebaseUid) {
        return firestoreService.documentExists(COLLECTION_NAME, firebaseUid);
    }
    
    /**
     * Prüfen ob E-Mail bereits verwendet wird
     */
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
    
    /**
     * Aktive Verkäufer finden
     */
    public List<User> findActiveSellers() {
        return findAll().stream()
            .filter(user -> "SELLER".equals(user.getUserType().toString()) && 
                           "ACTIVE".equals(user.getAccountStatus().toString()))
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Aktive Kunden finden
     */
    public List<User> findActiveCustomers() {
        return findAll().stream()
            .filter(user -> "CUSTOMER".equals(user.getUserType().toString()) && 
                           "ACTIVE".equals(user.getAccountStatus().toString()))
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Benutzer nach Namen suchen
     */
    public List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName) {
        return findAll().stream()
            .filter(user -> user.getFirstName().toLowerCase().contains(firstName.toLowerCase()) ||
                           user.getLastName().toLowerCase().contains(lastName.toLowerCase()))
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Verkäufer nach Spezialisierung finden
     */
    public List<User> findSellersBySpecialization(String specialization) {
        // Für eine einfache Implementierung - in der Realität würde man ein separates Feld haben
        return findActiveSellers();
    }
}
