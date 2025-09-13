'use client';

// Authentication Context für Auto Marketplace

import React, { createContext, useContext, useEffect, useState } from 'react';
import {
  signInWithEmailAndPassword,
  createUserWithEmailAndPassword,
  signOut as firebaseSignOut,
  onAuthStateChanged,
  User as FirebaseUser,
} from 'firebase/auth';
import { auth } from './firebase';
import { userAPI } from './api';
import { AuthContextType, AuthUser, User, CreateUserRequest, UpdateUserRequest, UpdateSellerRequest } from '@/types';
import toast from 'react-hot-toast';

// Erstelle Context
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Auth Provider Component
export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [userProfile, setUserProfile] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  // Firebase Auth State Listener
  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, async (firebaseUser: FirebaseUser | null) => {
      if (firebaseUser) {
        // User ist eingeloggt
        const authUser: AuthUser = {
          uid: firebaseUser.uid,
          email: firebaseUser.email,
          displayName: firebaseUser.displayName,
          photoURL: firebaseUser.photoURL,
          emailVerified: firebaseUser.emailVerified,
        };
        
        setUser(authUser);
        
        // Lade User-Profil vom Backend
        try {
          const response = await userAPI.getCurrentUser();
          setUserProfile(response.data);
        } catch (error) {
          console.log('User-Profil noch nicht erstellt oder Fehler beim Laden');
          setUserProfile(null);
        }
      } else {
        // User ist ausgeloggt
        setUser(null);
        setUserProfile(null);
      }
      
      setLoading(false);
    });

    return () => unsubscribe();
  }, []);

  // Sign In Funktion
  const signIn = async (email: string, password: string): Promise<void> => {
    try {
      setLoading(true);
      await signInWithEmailAndPassword(auth, email, password);
      toast.success('Erfolgreich angemeldet!');
    } catch (error: any) {
      console.error('Sign in error:', error);
      
      // Firebase Error Codes zu deutschen Nachrichten
      const errorMessages: { [key: string]: string } = {
        'auth/user-not-found': 'Kein Benutzer mit dieser E-Mail-Adresse gefunden.',
        'auth/wrong-password': 'Falsches Passwort.',
        'auth/invalid-email': 'Ungültige E-Mail-Adresse.',
        'auth/user-disabled': 'Dieser Benutzer wurde deaktiviert.',
        'auth/too-many-requests': 'Zu viele fehlgeschlagene Anmeldeversuche. Versuchen Sie es später erneut.',
        'auth/network-request-failed': 'Netzwerkfehler. Prüfen Sie Ihre Internetverbindung.',
      };
      
      const message = errorMessages[error.code] || 'Anmeldung fehlgeschlagen. Versuchen Sie es erneut.';
      toast.error(message);
      throw new Error(message);
    } finally {
      setLoading(false);
    }
  };

  // Sign Up Funktion
  const signUp = async (email: string, password: string, userData: CreateUserRequest): Promise<void> => {
    try {
      setLoading(true);
      
      // Erstelle Firebase User
      const userCredential = await createUserWithEmailAndPassword(auth, email, password);
      
      // Warte kurz bis Firebase User verfügbar ist
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // Erstelle User-Profil im Backend
      try {
        const response = await userAPI.createProfile(userData);
        setUserProfile(response.data);
        toast.success('Konto erfolgreich erstellt!');
      } catch (profileError) {
        console.error('Fehler beim Erstellen des Profils:', profileError);
        // Lösche Firebase User wenn Profil-Erstellung fehlschlägt
        await userCredential.user.delete();
        throw new Error('Fehler beim Erstellen des Profils. Versuchen Sie es erneut.');
      }
    } catch (error: any) {
      console.error('Sign up error:', error);
      
      const errorMessages: { [key: string]: string } = {
        'auth/email-already-in-use': 'Diese E-Mail-Adresse wird bereits verwendet.',
        'auth/invalid-email': 'Ungültige E-Mail-Adresse.',
        'auth/operation-not-allowed': 'E-Mail/Passwort-Anmeldung ist nicht aktiviert.',
        'auth/weak-password': 'Das Passwort ist zu schwach. Verwenden Sie mindestens 6 Zeichen.',
        'auth/network-request-failed': 'Netzwerkfehler. Prüfen Sie Ihre Internetverbindung.',
      };
      
      const message = errorMessages[error.code] || error.message || 'Registrierung fehlgeschlagen. Versuchen Sie es erneut.';
      toast.error(message);
      throw new Error(message);
    } finally {
      setLoading(false);
    }
  };

  // Sign Out Funktion
  const signOut = async (): Promise<void> => {
    try {
      await firebaseSignOut(auth);
      setUser(null);
      setUserProfile(null);
      toast.success('Erfolgreich abgemeldet!');
    } catch (error) {
      console.error('Sign out error:', error);
      toast.error('Fehler beim Abmelden.');
      throw error;
    }
  };

  // Update Profile Funktion
  const updateProfile = async (data: UpdateUserRequest): Promise<void> => {
    try {
      const response = await userAPI.updateProfile(data);
      setUserProfile(response.data);
      toast.success('Profil erfolgreich aktualisiert!');
    } catch (error) {
      console.error('Update profile error:', error);
      toast.error('Fehler beim Aktualisieren des Profils.');
      throw error;
    }
  };

  // Update Seller Info Funktion
  const updateSellerInfo = async (data: UpdateSellerRequest): Promise<void> => {
    try {
      const response = await userAPI.updateSellerInfo(data);
      setUserProfile(response.data);
      toast.success('Verkäufer-Informationen erfolgreich aktualisiert!');
    } catch (error) {
      console.error('Update seller info error:', error);
      toast.error('Fehler beim Aktualisieren der Verkäufer-Informationen.');
      throw error;
    }
  };

  // Context Value
  const value: AuthContextType = {
    user,
    userProfile,
    loading,
    signIn,
    signUp,
    signOut,
    updateProfile,
    updateSellerInfo,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

// Hook zum Verwenden des Auth Context
export function useAuth(): AuthContextType {
  const context = useContext(AuthContext);
  
  if (context === undefined) {
    throw new Error('useAuth muss innerhalb eines AuthProvider verwendet werden');
  }
  
  return context;
}

// Hook für geschützte Routen
export function useRequireAuth() {
  const { user, loading } = useAuth();
  
  useEffect(() => {
    if (!loading && !user) {
      // Redirect zu Login wenn nicht authentifiziert
      window.location.href = '/auth/login';
    }
  }, [user, loading]);
  
  return { user, loading };
}

// Hook für Seller-spezifische Funktionen
export function useRequireSeller() {
  const { user, userProfile, loading } = useAuth();
  
  useEffect(() => {
    if (!loading) {
      if (!user) {
        window.location.href = '/auth/login';
      } else if (userProfile && userProfile.userType !== 'SELLER') {
        toast.error('Diese Funktion ist nur für Verkäufer verfügbar.');
        window.location.href = '/';
      }
    }
  }, [user, userProfile, loading]);
  
  return { user, userProfile, loading };
}
