# Auto Marketplace Backend

Ein Java Spring Boot Backend für den Auto Marketplace mit Firebase Authentication und MongoDB.

## 🚀 Technologien

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Security** (mit Firebase JWT)
- **Spring Data MongoDB**
- **Firebase Admin SDK**
- **Maven** (Build Tool)

## 📁 Projektstruktur

```
src/main/java/com/automarketplace/
├── AutoMarketplaceApplication.java    # Hauptklasse
├── config/                           # Konfigurationen
│   ├── FirebaseConfig.java          # Firebase Setup
│   └── SecurityConfig.java          # Security & CORS
├── controller/                       # REST Controllers
│   ├── UserController.java          # User Management
│   ├── CarController.java           # Auto Management
│   ├── CarRequestDTOs.java          # Request DTOs
│   └── HealthController.java        # Health Check
├── service/                          # Business Logic
│   ├── UserService.java             # User Services
│   └── CarService.java              # Auto Services
├── repository/                       # Data Access
│   ├── UserRepository.java          # User Repository
│   └── CarRepository.java           # Auto Repository
├── model/                           # Datenmodelle
│   ├── User.java                    # User Entity
│   ├── UserType.java                # User Type Enum
│   ├── AccountStatus.java           # Account Status Enum
│   ├── Car.java                     # Auto Entity
│   └── CarStatus.java               # Auto Status Enum
└── security/                        # Security Components
    ├── FirebaseAuthenticationFilter.java
    └── FirebaseUserDetails.java
```

## 🔧 Setup & Installation

### Voraussetzungen

1. **Java JDK 17 oder höher**
   - Download: [OpenJDK](https://adoptium.net/) oder [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)

2. **Apache Maven**
   - Download: [maven.apache.org](https://maven.apache.org/download.cgi)

3. **MongoDB**
   - Lokal: [MongoDB Community](https://www.mongodb.com/try/download/community)
   - Cloud: [MongoDB Atlas](https://www.mongodb.com/atlas)

### Installation

1. **Klone das Repository**
   ```bash
   git clone <repository-url>
   cd auto-marketplace/backend
   ```

2. **Firebase Setup**
   - Erstelle ein Firebase Projekt: [Firebase Console](https://console.firebase.google.com/)
   - Aktiviere Authentication (Email/Password)
   - Generiere Service Account Key:
     - Gehe zu Projekteinstellungen → Service Accounts
     - Klicke "Neuen privaten Schlüssel generieren"
     - Speichere die JSON-Datei als `src/main/resources/firebase-service-account.json`

3. **Konfiguration anpassen**
   
   Bearbeite `src/main/resources/application.yml`:
   ```yaml
   firebase:
     project-id: dein-firebase-projekt-id
     credentials-path: src/main/resources/firebase-service-account.json
   
   spring:
     data:
       mongodb:
         uri: mongodb://localhost:27017/auto_marketplace
         # Für MongoDB Atlas:
         # uri: mongodb+srv://username:password@cluster.mongodb.net/auto_marketplace
   ```

4. **Dependencies installieren**
   ```bash
   mvn clean install
   ```

5. **Anwendung starten**
   ```bash
   mvn spring-boot:run
   ```

   Die API läuft dann auf: `http://localhost:8080/api`

## 🔐 Authentifizierung

Das Backend verwendet Firebase JWT Tokens für die Authentifizierung:

1. **Frontend** authentifiziert User mit Firebase Auth
2. **Frontend** sendet JWT Token im `Authorization` Header: `Bearer <token>`
3. **Backend** validiert Token mit Firebase Admin SDK
4. **Backend** extrahiert User-Informationen und gewährt Zugriff

### Geschützte Endpoints

- `POST /api/users/profile` - User-Profil erstellen
- `GET /api/users/me` - Aktuelles Profil abrufen
- `PUT /api/users/me` - Profil aktualisieren
- `POST /api/cars` - Auto erstellen (nur Seller)
- `PUT /api/cars/{id}` - Auto bearbeiten (nur Owner)
- `DELETE /api/cars/{id}` - Auto löschen (nur Owner)

### Öffentliche Endpoints

- `GET /api/health` - Health Check
- `GET /api/cars` - Auto-Liste
- `GET /api/cars/{id}` - Auto-Details
- `GET /api/cars/search` - Auto-Suche

## 📊 API Dokumentation

### User Endpoints

#### Profil erstellen/aktualisieren
```http
POST /api/users/profile
Authorization: Bearer <firebase-jwt>
Content-Type: application/json

{
  "firstName": "Max",
  "lastName": "Mustermann",
  "userType": "SELLER"
}
```

#### Aktuelles Profil abrufen
```http
GET /api/users/me
Authorization: Bearer <firebase-jwt>
```

### Auto Endpoints

#### Auto erstellen
```http
POST /api/cars
Authorization: Bearer <firebase-jwt>
Content-Type: application/json

{
  "brand": "BMW",
  "model": "3er",
  "year": 2020,
  "price": 25000,
  "mileage": 50000,
  "fuelType": "Benzin",
  "transmission": "Automatik",
  "condition": "Gebraucht",
  "description": "Sehr gut erhaltener BMW 3er..."
}
```

#### Autos suchen
```http
GET /api/cars/search?brand=BMW&minPrice=20000&maxPrice=50000&page=0&size=20
```

## 🗄️ Datenmodelle

### User
- `firebaseUid` - Firebase UID (eindeutig)
- `email` - Email-Adresse
- `firstName`, `lastName` - Name
- `userType` - SELLER oder CUSTOMER
- `phoneNumber`, `profileImageUrl` - Optionale Felder
- Seller-spezifisch: `companyName`, `businessLicense`, `address`, `specializations`

### Car
- `seller` - Referenz zum Verkäufer
- `brand`, `model`, `year` - Grunddaten
- `price`, `mileage` - Preis und Kilometerstand
- `fuelType`, `transmission` - Technische Daten
- `condition`, `description` - Zustand und Beschreibung
- `imageUrls` - Cloudinary Bild-URLs
- `status` - ACTIVE, SOLD, INACTIVE

## 🧪 Testing

```bash
# Unit Tests ausführen
mvn test

# Integration Tests
mvn verify

# Test Coverage Report
mvn jacoco:report
```

## 🚀 Deployment

### Lokale Entwicklung
```bash
mvn spring-boot:run
```

### Production Build
```bash
mvn clean package
java -jar target/auto-marketplace-backend-0.0.1-SNAPSHOT.jar
```

### Docker (optional)
```dockerfile
FROM openjdk:17-jre-slim
COPY target/auto-marketplace-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🔧 Konfiguration

### Umgebungsvariablen

```bash
# Firebase
FIREBASE_PROJECT_ID=dein-projekt-id
FIREBASE_CREDENTIALS_PATH=/path/to/service-account.json

# MongoDB
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/auto_marketplace

# Cloudinary (optional)
CLOUDINARY_CLOUD_NAME=dein-cloud-name
CLOUDINARY_API_KEY=dein-api-key
CLOUDINARY_API_SECRET=dein-api-secret
```

## 📝 Nächste Schritte

1. **Frontend Integration** - Next.js Frontend erstellen
2. **Cloudinary Integration** - Bild-Upload implementieren
3. **Email Notifications** - Benachrichtigungen bei neuen Angeboten
4. **Advanced Search** - Elasticsearch für bessere Suche
5. **Caching** - Redis für Performance
6. **Monitoring** - Logging und Metriken

## 🐛 Troubleshooting

### Häufige Probleme

1. **Firebase Fehler**
   - Prüfe `firebase-service-account.json` Pfad
   - Verifiziere Firebase Projekt-ID

2. **MongoDB Verbindung**
   - Prüfe MongoDB läuft: `mongosh`
   - Verifiziere Connection String

3. **CORS Probleme**
   - Prüfe `SecurityConfig.java` CORS-Einstellungen
   - Füge Frontend-URL zu `allowed-origins` hinzu

### Logs anzeigen
```bash
# Application Logs
tail -f logs/application.log

# Spring Boot Debug
mvn spring-boot:run -Dspring-boot.run.arguments="--logging.level.com.automarketplace=DEBUG"
```

## 📞 Support

Bei Problemen oder Fragen:
1. Prüfe die Logs
2. Verifiziere Konfiguration
3. Teste mit Health Check: `GET /api/health`
