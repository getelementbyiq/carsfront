package com.automarketplace.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Firebase Konfiguration
 * 
 * Diese Klasse konfiguriert Firebase Admin SDK für die Backend-Authentifizierung.
 * Firebase Admin SDK wird verwendet, um JWT Tokens zu verifizieren, die vom Frontend gesendet werden.
 */
@Configuration
public class FirebaseConfig {
    
    @Value("${firebase.project-id}")
    private String projectId;
    
    @Value("${firebase.credentials-path}")
    private String credentialsPath;
    
    /**
     * Initialisiere Firebase App beim Start der Anwendung
     * 
     * @PostConstruct wird nach der Dependency Injection ausgeführt
     */
    @PostConstruct
    public void initializeFirebase() {
        try {
            // Prüfe ob Firebase App bereits initialisiert ist
            if (FirebaseApp.getApps().isEmpty()) {
                
                // Lade Service Account Credentials
                FileInputStream serviceAccount = new FileInputStream(credentialsPath);
                
                // Erstelle Firebase Options
                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId(projectId)
                    .build();
                
                // Initialisiere Firebase App
                FirebaseApp.initializeApp(options);
                
                System.out.println("Firebase Admin SDK erfolgreich initialisiert für Projekt: " + projectId);
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Initialisieren von Firebase Admin SDK: " + e.getMessage());
            throw new RuntimeException("Firebase Initialisierung fehlgeschlagen", e);
        }
    }
    
    /**
     * Firebase Auth Bean für Dependency Injection
     * 
     * @return FirebaseAuth Instanz
     */
    @Bean
    public FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }
}
