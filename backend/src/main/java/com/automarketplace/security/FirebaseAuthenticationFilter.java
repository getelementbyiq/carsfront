package com.automarketplace.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Firebase Authentication Filter
 * 
 * Dieser Filter interceptiert alle HTTP-Requests und prüft Firebase JWT Tokens.
 * Wenn ein gültiger Token vorhanden ist, wird der User authentifiziert.
 */
@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {
    
    private final FirebaseAuth firebaseAuth;
    
    @Autowired
    public FirebaseAuthenticationFilter(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // Extrahiere Authorization Header
        String authorizationHeader = request.getHeader("Authorization");
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extrahiere Token (entferne "Bearer " Prefix)
            String token = authorizationHeader.substring(7);
            
            try {
                // Verifiziere Firebase Token
                FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);
                
                // Extrahiere User-Informationen
                String firebaseUid = decodedToken.getUid();
                String email = decodedToken.getEmail();
                
                // Erstelle Spring Security Authentication
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        firebaseUid, // Principal (User ID)
                        null, // Credentials (nicht benötigt)
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // Authorities
                    );
                
                // Füge zusätzliche User-Informationen hinzu
                authentication.setDetails(new FirebaseUserDetails(firebaseUid, email, decodedToken));
                
                // Setze Authentication im Security Context
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                System.out.println("User erfolgreich authentifiziert: " + firebaseUid + " (" + email + ")");
                
            } catch (FirebaseAuthException e) {
                System.err.println("Firebase Token Validierung fehlgeschlagen: " + e.getMessage());
                // Token ist ungültig - Request wird ohne Authentication fortgesetzt
                // Spring Security wird den Zugriff auf geschützte Endpoints verweigern
            }
        }
        
        // Setze Filter-Chain fort
        filterChain.doFilter(request, response);
    }
    
    /**
     * Prüfe ob Filter für diesen Request angewendet werden soll
     * 
     * @param request HTTP Request
     * @return boolean
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        // Überspringe Filter für öffentliche Endpoints
        return path.startsWith("/api/public/") || 
               path.equals("/api/health") ||
               path.startsWith("/api/cars/search") || // Öffentliche Auto-Suche
               path.startsWith("/api/cars/") && request.getMethod().equals("GET"); // Öffentliche Auto-Details
    }
}
