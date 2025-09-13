# Auto Marketplace Frontend

Ein modernes Next.js Frontend für den Auto Marketplace mit TypeScript, Tailwind CSS und Firebase Authentication.

## 🚀 Technologien

- **Next.js 14** (App Router)
- **TypeScript**
- **Tailwind CSS**
- **Firebase Auth**
- **React Hook Form**
- **Axios** (API Client)
- **Lucide React** (Icons)

## 📁 Projektstruktur

```
src/
├── app/                          # Next.js App Router
│   ├── (auth)/                  # Auth Route Group
│   ├── (dashboard)/             # Dashboard Route Group
│   ├── layout.tsx               # Root Layout
│   ├── page.tsx                 # Homepage
│   └── globals.css              # Global Styles
├── components/                   # React Components
│   ├── ui/                      # Reusable UI Components
│   │   ├── Button.tsx
│   │   ├── Input.tsx
│   │   └── Card.tsx
│   ├── layout/                  # Layout Components
│   ├── forms/                   # Form Components
│   └── cars/                    # Auto-spezifische Components
├── lib/                         # Utilities & Configuration
│   ├── firebase.ts              # Firebase Config
│   ├── api.ts                   # API Client
│   ├── auth-context.tsx         # Authentication Context
│   └── utils.ts                 # Utility Functions
├── hooks/                       # Custom React Hooks
├── types/                       # TypeScript Types
│   └── index.ts
└── public/                      # Static Assets
```

## 🔧 Setup & Installation

### Voraussetzungen

1. **Node.js 18 oder höher**
   - Download: [nodejs.org](https://nodejs.org/)

2. **npm oder yarn**
   - Kommt mit Node.js

### Installation

1. **Dependencies installieren**
   ```bash
   cd auto-marketplace/frontend
   npm install
   ```

2. **Umgebungsvariablen konfigurieren**
   ```bash
   cp env.example .env.local
   ```
   
   Bearbeite `.env.local` mit deinen Werten:
   ```env
   NEXT_PUBLIC_API_URL=http://localhost:8080/api
   NEXT_PUBLIC_FIREBASE_API_KEY=dein-firebase-api-key
   NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=dein-projekt.firebaseapp.com
   NEXT_PUBLIC_FIREBASE_PROJECT_ID=dein-firebase-projekt-id
   # ... weitere Firebase Konfiguration
   ```

3. **Development Server starten**
   ```bash
   npm run dev
   ```
   
   Die App läuft dann auf: `http://localhost:3000`

## 🔐 Firebase Setup

1. **Firebase Projekt erstellen**
   - Gehe zu [Firebase Console](https://console.firebase.google.com/)
   - Erstelle ein neues Projekt
   - Aktiviere Authentication (Email/Password)

2. **Web App hinzufügen**
   - Klicke auf "Web App hinzufügen"
   - Kopiere die Konfigurationswerte
   - Füge sie in `.env.local` ein

3. **Authentication konfigurieren**
   - Gehe zu Authentication → Sign-in method
   - Aktiviere "Email/Password"
   - Optional: Aktiviere Google, Facebook, etc.

## 🎨 UI Components

### Button Component
```tsx
import Button from '@/components/ui/Button';

<Button variant="primary" size="lg" loading={isLoading}>
  Klick mich
</Button>
```

**Variants:** `primary`, `secondary`, `outline`, `ghost`, `danger`
**Sizes:** `sm`, `md`, `lg`, `xl`

### Input Component
```tsx
import Input from '@/components/ui/Input';

<Input
  label="Email"
  type="email"
  placeholder="ihre@email.de"
  error={errors.email?.message}
  leftIcon={<Mail className="h-4 w-4" />}
/>
```

### Card Component
```tsx
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';

<Card hover>
  <CardHeader>
    <CardTitle>Titel</CardTitle>
  </CardHeader>
  <CardContent>
    Inhalt hier...
  </CardContent>
</Card>
```

## 🔗 API Integration

### Authentication
```tsx
import { useAuth } from '@/lib/auth-context';

function MyComponent() {
  const { user, signIn, signOut, loading } = useAuth();
  
  const handleLogin = async () => {
    try {
      await signIn(email, password);
    } catch (error) {
      console.error('Login failed:', error);
    }
  };
}
```

### API Calls
```tsx
import { carAPI } from '@/lib/api';

// Alle Autos laden
const cars = await carAPI.getAllCars({ page: 0, size: 20 });

// Auto erstellen (authentifiziert)
const newCar = await carAPI.createCar(carData);

// Auto suchen
const results = await carAPI.searchCars({ brand: 'BMW', minPrice: 20000 });
```

## 🎯 Routing

### App Router Structure
```
app/
├── page.tsx                     # Homepage (/)
├── cars/
│   ├── page.tsx                # Auto-Liste (/cars)
│   ├── [id]/
│   │   └── page.tsx            # Auto-Details (/cars/[id])
│   └── search/
│       └── page.tsx            # Suche (/cars/search)
├── (auth)/
│   ├── login/
│   │   └── page.tsx            # Login (/login)
│   └── register/
│       └── page.tsx            # Registrierung (/register)
└── (dashboard)/
    ├── dashboard/
    │   └── page.tsx            # Dashboard (/dashboard)
    └── sell/
        └── page.tsx            # Auto verkaufen (/sell)
```

### Protected Routes
```tsx
import { useRequireAuth } from '@/lib/auth-context';

function ProtectedPage() {
  const { user, loading } = useRequireAuth();
  
  if (loading) return <div>Loading...</div>;
  
  return <div>Protected content for {user?.email}</div>;
}
```

## 🎨 Styling

### Tailwind CSS Classes
```tsx
// Custom Classes (siehe globals.css)
<button className="btn-primary">Primary Button</button>
<input className="input-primary" />
<div className="card">Card Content</div>

// Utility Classes
<div className="hero-gradient">Hero Background</div>
<div className="car-card">Auto Card</div>
<span className="price-badge">€25.000</span>
<span className="status-badge status-active">Verfügbar</span>
```

### Custom Colors
```css
/* Verfügbar in Tailwind */
bg-primary-600    /* Hauptfarbe */
bg-secondary-100  /* Grau-Töne */
bg-success-600    /* Grün (Erfolg) */
bg-warning-600    /* Orange (Warnung) */
bg-error-600      /* Rot (Fehler) */
```

## 📱 Responsive Design

Alle Komponenten sind responsive designed:
```tsx
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
  {/* Mobile: 1 Spalte, Tablet: 2 Spalten, Desktop: 3 Spalten */}
</div>

<h1 className="text-2xl md:text-4xl lg:text-6xl">
  {/* Responsive Typography */}
</h1>
```

## 🧪 Development

### Scripts
```bash
# Development Server
npm run dev

# Production Build
npm run build

# Start Production Server
npm start

# Linting
npm run lint

# Type Checking
npx tsc --noEmit
```

### Code Style
- **TypeScript** für Type Safety
- **ESLint** für Code Quality
- **Prettier** für Code Formatting
- **Tailwind CSS** für Styling

## 🚀 Deployment

### Vercel (Empfohlen)
```bash
# Installiere Vercel CLI
npm i -g vercel

# Deploy
vercel

# Setze Environment Variables in Vercel Dashboard
```

### Andere Plattformen
```bash
# Build für Production
npm run build

# Statische Files sind in .next/ Ordner
# Upload zu deinem Hosting Provider
```

## 🔧 Konfiguration

### Next.js Config
```javascript
// next.config.js
const nextConfig = {
  images: {
    domains: ['res.cloudinary.com'], // Für Cloudinary Bilder
  },
  env: {
    NEXT_PUBLIC_API_URL: process.env.NEXT_PUBLIC_API_URL,
  },
};
```

### Tailwind Config
```javascript
// tailwind.config.ts
module.exports = {
  content: ['./src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        primary: { /* Custom Colors */ },
      },
    },
  },
};
```

## 🐛 Troubleshooting

### Häufige Probleme

1. **Firebase Fehler**
   ```
   Error: Firebase configuration missing
   ```
   → Prüfe `.env.local` Datei und Firebase Konfiguration

2. **API Verbindungsfehler**
   ```
   Error: Network Error
   ```
   → Prüfe ob Backend läuft auf `http://localhost:8080`

3. **Build Fehler**
   ```
   Type error: Cannot find module
   ```
   → Prüfe TypeScript Imports und Pfade

### Debug Tipps
```bash
# Detaillierte Logs
npm run dev -- --debug

# Type Checking
npx tsc --noEmit

# Bundle Analyzer
npm run build && npx @next/bundle-analyzer
```

## 📞 Support

Bei Problemen:
1. Prüfe die Browser-Konsole
2. Prüfe Network Tab für API Calls
3. Prüfe `.env.local` Konfiguration
4. Prüfe Firebase Console für Auth Errors

## 🔄 Updates

```bash
# Dependencies aktualisieren
npm update

# Next.js aktualisieren
npm install next@latest react@latest react-dom@latest

# Security Updates
npm audit fix
```