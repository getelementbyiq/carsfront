// Firebase Configuration für Auto Marketplace Frontend

import { initializeApp } from 'firebase/app';
import { getAuth, GoogleAuthProvider } from 'firebase/auth';

// Firebase Konfiguration aus Umgebungsvariablen
const firebaseConfig = {
  apiKey: process.env.NEXT_PUBLIC_FIREBASE_API_KEY,
  authDomain: process.env.NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN,
  projectId: process.env.NEXT_PUBLIC_FIREBASE_PROJECT_ID,
  storageBucket: process.env.NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET,
  messagingSenderId: process.env.NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID,
  appId: process.env.NEXT_PUBLIC_FIREBASE_APP_ID,
};

// Validiere dass alle erforderlichen Konfigurationswerte vorhanden sind
const requiredConfig = [
  'NEXT_PUBLIC_FIREBASE_API_KEY',
  'NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN',
  'NEXT_PUBLIC_FIREBASE_PROJECT_ID',
  'NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET',
  'NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID',
  'NEXT_PUBLIC_FIREBASE_APP_ID',
];

const missingConfig = requiredConfig.filter(key => !process.env[key]);

if (missingConfig.length > 0) {
  console.error('Fehlende Firebase Konfiguration:', missingConfig);
  // In development, zeige Warnung aber breche nicht ab
  if (process.env.NODE_ENV === 'production') {
    throw new Error(`Fehlende Firebase Umgebungsvariablen: ${missingConfig.join(', ')}`);
  }
}

// Initialisiere Firebase App
const app = initializeApp(firebaseConfig);

// Exportiere Firebase Services
export const auth = getAuth(app);

// Google Auth Provider
export const googleProvider = new GoogleAuthProvider();
googleProvider.setCustomParameters({
  prompt: 'select_account'
});

// Default Export der App
export default app;