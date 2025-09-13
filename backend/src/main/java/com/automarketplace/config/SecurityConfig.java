package com.automarketplace.config;

import com.automarketplace.security.FirebaseAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security Konfiguration
 * 
 * Diese Klasse konfiguriert die Sicherheitseinstellungen für die API.
 * Sie definiert, welche Endpoints öffentlich sind und welche Authentifizierung benötigen.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final FirebaseAuthenticationFilter firebaseAuthenticationFilter;
    
    @Autowired
    public SecurityConfig(FirebaseAuthenticationFilter firebaseAuthenticationFilter) {
        this.firebaseAuthenticationFilter = firebaseAuthenticationFilter;
    }
    
    /**
     * Hauptkonfiguration der Security Filter Chain
     * 
     * @param http HttpSecurity Objekt
     * @return SecurityFilterChain
     * @throws Exception bei Konfigurationsfehlern
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF deaktivieren (da wir eine REST API sind)
            .csrf(csrf -> csrf.disable())
            
            // CORS konfigurieren
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Session Management - Stateless (keine Sessions)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Authorization Rules
            .authorizeHttpRequests(authz -> authz
                // Öffentliche Endpoints (keine Authentifizierung erforderlich)
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/health").permitAll()
                
                // Auto-Endpoints (teilweise öffentlich)
                .requestMatchers("GET", "/api/cars/search/**").permitAll() // Öffentliche Suche
                .requestMatchers("GET", "/api/cars/{id}").permitAll() // Öffentliche Auto-Details
                .requestMatchers("GET", "/api/cars").permitAll() // Öffentliche Auto-Liste
                
                // User-Endpoints (authentifiziert)
                .requestMatchers("/api/users/**").authenticated()
                
                // Seller-spezifische Endpoints
                .requestMatchers("POST", "/api/cars").authenticated() // Nur Seller können Autos erstellen
                .requestMatchers("PUT", "/api/cars/**").authenticated() // Nur Owner kann bearbeiten
                .requestMatchers("DELETE", "/api/cars/**").authenticated() // Nur Owner kann löschen
                
                // Alle anderen Requests benötigen Authentifizierung
                .anyRequest().authenticated()
            )
            
            // Firebase Authentication Filter hinzufügen
            .addFilterBefore(firebaseAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    /**
     * CORS Konfiguration
     * 
     * Erlaubt Cross-Origin Requests vom Frontend (Next.js)
     * 
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Erlaubte Origins (Frontend URLs)
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:3000", // Next.js Development
            "http://localhost:3001",
            "https://your-domain.com" // Production Domain
        ));
        
        // Erlaubte HTTP Methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        
        // Erlaubte Headers
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization", 
            "Content-Type", 
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // Erlaube Credentials (Cookies, Authorization Headers)
        configuration.setAllowCredentials(true);
        
        // Exposed Headers (für Frontend verfügbar)
        configuration.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));
        
        // Cache Preflight Requests für 1 Stunde
        configuration.setMaxAge(3600L);
        
        // Registriere Konfiguration für alle Pfade
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
