# 🚀 Auto Marketplace - Vollständiger Setup Guide

Herzlichen Glückwunsch! Ihr Auto Marketplace Projekt ist vollständig erstellt. Hier ist eine Schritt-für-Schritt Anleitung zum Starten.

## 📋 Was wurde erstellt?

### ✅ Backend (Java Spring Boot)
- **Vollständige REST API** mit Spring Boot 3.1.5
- **Firebase Authentication** Integration
- **MongoDB** Datenbankanbindung
- **User Management** (Seller/Customer)
- **Auto Management** mit CRUD Operations
- **Erweiterte Suchfunktionen**
- **Security Configuration** mit JWT
- **Reusable Services & Components**

### ✅ Frontend (Next.js)
- **Next.js 14** mit App Router
- **TypeScript** für Type Safety
- **Tailwind CSS** für modernes Design
- **Firebase Client** Integration
- **Authentication Context**
- **Reusable UI Components**
- **Responsive Design**
- **API Client** mit Axios

## 🛠️ Erforderliche Software

Bevor Sie beginnen, installieren Sie:

1. **Java JDK 17+** - [Download](https://adoptium.net/)
2. **Apache Maven** - [Download](https://maven.apache.org/download.cgi)
3. **Node.js 18+** - [Download](https://nodejs.org/)
4. **MongoDB** - [Download](https://www.mongodb.com/try/download/community) oder [MongoDB Atlas](https://www.mongodb.com/atlas)

## 🔥 Firebase Setup (Wichtig!)

### 1. Firebase Projekt erstellen
1. Gehe zu [Firebase Console](https://console.firebase.google.com/)
2. Klicke "Projekt hinzufügen"
3. Folge dem Setup-Assistenten

### 2. Authentication aktivieren
1. Gehe zu **Authentication** → **Sign-in method**
2. Aktiviere **Email/Password**
3. Optional: Aktiviere Google, Facebook, etc.

### 3. Service Account Key generieren
1. Gehe zu **Projekteinstellungen** → **Service Accounts**
2. Klicke **"Neuen privaten Schlüssel generieren"**
3. Speichere die JSON-Datei als:
   ```
   auto-marketplace/backend/src/main/resources/firebase-service-account.json
   ```

### 4. Web App hinzufügen
1. Klicke auf **Web-Symbol** (</>)
2. Registriere deine App
3. Kopiere die Konfigurationswerte

## 🚀 Backend starten

### 1. Konfiguration anpassen
Bearbeite `auto-marketplace/backend/src/main/resources/application.yml`:

```yaml
firebase:
  project-id: DEIN-FIREBASE-PROJEKT-ID
  credentials-path: src/main/resources/firebase-service-account.json

spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/auto_marketplace
      # Für MongoDB Atlas:
      # uri: mongodb+srv://username:password@cluster.mongodb.net/auto_marketplace
```

### 2. Backend starten
```bash
cd auto-marketplace/backend
mvn spring-boot:run
```

✅ **Backend läuft auf:** `http://localhost:8080/api`
✅ **Health Check:** `http://localhost:8080/api/health`

## 🎨 Frontend starten

### 1. Dependencies installieren
```bash
cd auto-marketplace/frontend
npm install
```

### 2. Environment Variables
Erstelle `.env.local` mit deinen Firebase Werten:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api
NEXT_PUBLIC_FIREBASE_API_KEY=dein-api-key
NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=dein-projekt.firebaseapp.com
NEXT_PUBLIC_FIREBASE_PROJECT_ID=dein-projekt-id
NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET=dein-projekt.appspot.com
NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=dein-sender-id
NEXT_PUBLIC_FIREBASE_APP_ID=dein-app-id
```

### 3. Frontend starten
```bash
npm run dev
```

✅ **Frontend läuft auf:** `http://localhost:3000`

## 🧪 Testen der Anwendung

### 1. Backend testen
```bash
# Health Check
curl http://localhost:8080/api/health

# Autos laden (öffentlich)
curl http://localhost:8080/api/cars
```

### 2. Frontend testen
1. Öffne `http://localhost:3000`
2. Klicke "Registrieren"
3. Erstelle einen Test-Account
4. Teste die Navigation

## 📊 API Endpoints

### Öffentliche Endpoints
- `GET /api/health` - Health Check
- `GET /api/cars` - Alle Autos
- `GET /api/cars/{id}` - Auto Details
- `GET /api/cars/search` - Auto Suche

### Authentifizierte Endpoints
- `POST /api/users/profile` - Profil erstellen
- `GET /api/users/me` - Aktuelles Profil
- `POST /api/cars` - Auto erstellen (Seller)
- `PUT /api/cars/{id}` - Auto bearbeiten (Owner)

## 🔧 Nächste Schritte

### Sofort verfügbar:
1. ✅ User Registration/Login
2. ✅ Auto CRUD Operations
3. ✅ Auto Suche und Filter
4. ✅ Seller/Customer Rollen
5. ✅ Responsive Design

### Erweiterungen (optional):
1. **Cloudinary Integration** - Bild-Upload
2. **Email Notifications** - Benachrichtigungen
3. **Payment Integration** - Stripe/PayPal
4. **Advanced Search** - Elasticsearch
5. **Real-time Chat** - Socket.io
6. **Mobile App** - React Native

## 🐛 Troubleshooting

### Backend Probleme
```bash
# Java Version prüfen
java -version

# Maven Version prüfen
mvn -version

# MongoDB läuft?
mongosh # oder mongo

# Logs anzeigen
tail -f logs/application.log
```

### Frontend Probleme
```bash
# Node Version prüfen
node --version

# Dependencies neu installieren
rm -rf node_modules package-lock.json
npm install

# Build testen
npm run build
```

### Firebase Probleme
1. **Prüfe Firebase Konfiguration** in Console
2. **Prüfe Service Account Key** Pfad
3. **Prüfe Environment Variables**
4. **Prüfe Browser Network Tab** für Errors

## 📁 Projektstruktur

```
auto-marketplace/
├── backend/                     # Java Spring Boot
│   ├── src/main/java/com/automarketplace/
│   │   ├── config/             # Konfigurationen
│   │   ├── controller/         # REST Controllers
│   │   ├── service/            # Business Logic
│   │   ├── repository/         # Data Access
│   │   ├── model/              # Datenmodelle
│   │   └── security/           # Security & Auth
│   ├── src/main/resources/
│   │   ├── application.yml     # Konfiguration
│   │   └── firebase-service-account.json
│   └── pom.xml                 # Maven Dependencies
├── frontend/                   # Next.js React
│   ├── src/
│   │   ├── app/               # App Router Pages
│   │   ├── components/        # React Components
│   │   ├── lib/              # Utils & Config
│   │   ├── hooks/            # Custom Hooks
│   │   └── types/            # TypeScript Types
│   ├── package.json          # NPM Dependencies
│   └── .env.local            # Environment Variables
├── docs/                     # Dokumentation
├── project.md               # Projektplanung
└── SETUP-GUIDE.md          # Diese Datei
```

## 🎯 Wichtige Features

### User Management
- ✅ Firebase Authentication
- ✅ User Profile Management
- ✅ Seller vs Customer Rollen
- ✅ JWT Token Validation

### Auto Management
- ✅ CRUD Operations
- ✅ Bild-Upload (Cloudinary ready)
- ✅ Erweiterte Suche & Filter
- ✅ Pagination
- ✅ Status Management (Active/Sold/Inactive)

### Security
- ✅ Firebase JWT Validation
- ✅ Role-based Access Control
- ✅ CORS Configuration
- ✅ Input Validation

### Frontend
- ✅ Responsive Design
- ✅ Modern UI Components
- ✅ Type-safe API Client
- ✅ Authentication Context
- ✅ Error Handling

## 🚀 Deployment

### Backend (Spring Boot)
```bash
# Production Build
mvn clean package

# Run JAR
java -jar target/auto-marketplace-backend-0.0.1-SNAPSHOT.jar

# Docker (optional)
docker build -t auto-marketplace-backend .
docker run -p 8080:8080 auto-marketplace-backend
```

### Frontend (Next.js)
```bash
# Vercel (empfohlen)
npm i -g vercel
vercel

# Oder andere Plattform
npm run build
# Upload .next/ folder
```

## 📞 Support

Das Projekt ist vollständig funktionsfähig! Bei Fragen:

1. **Prüfe die READMEs** in backend/ und frontend/
2. **Prüfe die Logs** für Error Messages
3. **Teste die API** mit curl oder Postman
4. **Prüfe Firebase Console** für Auth Issues

---

**Viel Erfolg mit Ihrem Auto Marketplace! 🚗✨**
