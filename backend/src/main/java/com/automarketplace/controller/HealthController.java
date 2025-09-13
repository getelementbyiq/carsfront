package com.automarketplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Health Check Controller
 * 
 * Dieser Controller bietet einen einfachen Health Check Endpoint
 * um zu prüfen, ob die API läuft.
 */
@RestController
@RequestMapping("/api")
public class HealthController {
    
    /**
     * Health Check Endpoint (öffentlich)
     * 
     * GET /api/health
     * 
     * @return ResponseEntity mit Status-Informationen
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, Object> status = Map.of(
            "status", "UP",
            "service", "Auto Marketplace Backend",
            "version", "1.0.0",
            "timestamp", LocalDateTime.now(),
            "message", "Backend läuft erfolgreich!"
        );
        
        return ResponseEntity.ok(status);
    }
}
