package com.automarketplace.security;

import com.google.firebase.auth.FirebaseToken;

/**
 * Firebase User Details
 * 
 * Diese Klasse enthält zusätzliche User-Informationen aus dem Firebase Token.
 * Sie wird im Authentication Object gespeichert und kann in Controllern verwendet werden.
 */
public class FirebaseUserDetails {
    
    private final String firebaseUid;
    private final String email;
    private final FirebaseToken firebaseToken;
    
    public FirebaseUserDetails(String firebaseUid, String email, FirebaseToken firebaseToken) {
        this.firebaseUid = firebaseUid;
        this.email = email;
        this.firebaseToken = firebaseToken;
    }
    
    public String getFirebaseUid() {
        return firebaseUid;
    }
    
    public String getEmail() {
        return email;
    }
    
    public FirebaseToken getFirebaseToken() {
        return firebaseToken;
    }
    
    public String getName() {
        return firebaseToken.getName();
    }
    
    public String getPicture() {
        return firebaseToken.getPicture();
    }
    
    public boolean isEmailVerified() {
        return firebaseToken.isEmailVerified();
    }
    
    @Override
    public String toString() {
        return "FirebaseUserDetails{" +
                "firebaseUid='" + firebaseUid + '\'' +
                ", email='" + email + '\'' +
                ", name='" + getName() + '\'' +
                ", emailVerified=" + isEmailVerified() +
                '}';
    }
}
