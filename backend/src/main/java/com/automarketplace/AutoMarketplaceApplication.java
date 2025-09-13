package com.automarketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hauptklasse für die Auto Marketplace Spring Boot Anwendung
 * 
 * @SpringBootApplication kombiniert:
 * - @Configuration: Markiert die Klasse als Konfigurationsquelle
 * - @EnableAutoConfiguration: Aktiviert Spring Boot Auto-Konfiguration
 * - @ComponentScan: Scannt nach Spring Komponenten in diesem Paket
 */
@SpringBootApplication
public class AutoMarketplaceApplication {

    /**
     * Main-Methode - Einstiegspunkt der Anwendung
     * 
     * @param args Kommandozeilen-Argumente
     */
    public static void main(String[] args) {
        SpringApplication.run(AutoMarketplaceApplication.class, args);
    }
}
