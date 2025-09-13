# carsfront

🚗 **AutoMarkt** - Moderner Automotive Marketplace

Ein vollständiger Auto-Marktplatz mit Next.js Frontend und Java Spring Boot Backend.

## 🛠️ Tech Stack

### Frontend
- **Next.js 14** - React Framework
- **Tailwind CSS** - Styling
- **Firebase Auth** - Authentifizierung
- **TypeScript** - Type Safety

### Backend
- **Java Spring Boot** - REST API
- **Firebase Firestore** - NoSQL Database
- **Firebase Admin SDK** - User Verification
- **Cloudinary** - Image Management

## 🚀 Features

- ✅ **Benutzer-Authentifizierung** (Email/Password + Google)
- ✅ **Zwei Benutzertypen**: Verkäufer & Käufer
- 🔄 **Auto CRUD Operations** (Create, Read, Update, Delete)
- 🔍 **Erweiterte Suchfunktionen**
- 📱 **Responsive Design**
- 🔐 **Geschützte Routen**

## 📁 Projektstruktur

```
auto-marketplace/
├── frontend/          # Next.js Frontend
│   ├── src/
│   │   ├── app/       # App Router
│   │   ├── components/# UI Components
│   │   ├── lib/       # Utilities
│   │   └── types/     # TypeScript Types
│   └── package.json
├── backend/           # Spring Boot Backend
│   ├── src/main/java/
│   │   └── com/automarketplace/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       └── model/
│   └── pom.xml
└── README.md
```

## 🔧 Setup & Installation

### Prerequisites
- Node.js 18+
- Java JDK 17+
- Maven 3.8+
- Firebase Project

### Frontend Setup
```bash
cd frontend
npm install
npm run dev
```

### Backend Setup
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

## 🌐 URLs
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api

## 📝 API Endpoints

### Cars
- `GET /api/cars` - Alle verfügbaren Autos
- `POST /api/cars` - Neues Auto erstellen
- `PUT /api/cars/{id}` - Auto aktualisieren
- `DELETE /api/cars/{id}` - Auto löschen
- `GET /api/cars/search` - Autos suchen

### Users
- `POST /api/users/register` - Benutzer registrieren
- `GET /api/users/profile` - Profil abrufen
- `PUT /api/users/profile` - Profil aktualisieren

## 🔐 Authentifizierung

Das System verwendet Firebase Authentication mit:
- Email/Password Login
- Google OAuth
- JWT Token Verification im Backend

## 📱 Screenshots

*Coming soon...*

## 🤝 Contributing

1. Fork das Repository
2. Erstelle einen Feature Branch
3. Committe deine Änderungen
4. Push zum Branch
5. Erstelle einen Pull Request

## 📄 License

MIT License - siehe [LICENSE](LICENSE) für Details.

---

**Entwickelt mit ❤️ für die Auto-Community**
