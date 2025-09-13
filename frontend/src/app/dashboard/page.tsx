'use client';

import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { onAuthStateChanged, signOut } from 'firebase/auth';
import { auth } from '@/lib/firebase';
import Button from '@/components/ui/Button';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import toast from 'react-hot-toast';

export default function DashboardPage() {
  const router = useRouter();
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (firebaseUser) => {
      if (firebaseUser) {
        setUser(firebaseUser);
      } else {
        router.push('/auth/login');
      }
      setLoading(false);
    });

    return () => unsubscribe();
  }, [router]);

  const handleSignOut = async () => {
    try {
      await signOut(auth);
      toast.success('Erfolgreich abgemeldet!');
      router.push('/');
    } catch (error) {
      toast.error('Fehler beim Abmelden');
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  if (!user) {
    return null;
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <h1 className="text-2xl font-bold text-gray-900">Dashboard</h1>
            <div className="flex items-center space-x-4">
              <span className="text-gray-700">
                Willkommen, {user.displayName || user.email}
              </span>
              <Button onClick={handleSignOut} variant="outline" size="sm">
                Abmelden
              </Button>
            </div>
          </div>
        </div>
      </header>

      {/* Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {/* Welcome Card */}
          <Card className="md:col-span-2 lg:col-span-3">
            <CardHeader>
              <CardTitle>🎉 Willkommen bei AutoMarkt!</CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-gray-600 mb-4">
                Ihr Konto wurde erfolgreich erstellt. Sie können jetzt alle Funktionen nutzen.
              </p>
              <div className="flex flex-wrap gap-4">
                <Button>Auto suchen</Button>
                <Button variant="outline">Profil vervollständigen</Button>
                <Button variant="ghost">Hilfe & Support</Button>
              </div>
            </CardContent>
          </Card>

          {/* User Info */}
          <Card>
            <CardHeader>
              <CardTitle>Ihre Daten</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-2">
                <p><strong>E-Mail:</strong> {user.email}</p>
                <p><strong>Name:</strong> {user.displayName || 'Nicht angegeben'}</p>
                <p><strong>Verifiziert:</strong> {user.emailVerified ? '✅ Ja' : '❌ Nein'}</p>
                <p><strong>Angemeldet seit:</strong> {new Date(user.metadata.creationTime).toLocaleDateString('de-DE')}</p>
              </div>
            </CardContent>
          </Card>

          {/* Quick Actions */}
          <Card>
            <CardHeader>
              <CardTitle>Schnellaktionen</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                <Button className="w-full" variant="outline">
                  🚗 Auto inserieren
                </Button>
                <Button className="w-full" variant="outline">
                  🔍 Autos durchsuchen
                </Button>
                <Button className="w-full" variant="outline">
                  ❤️ Favoriten ansehen
                </Button>
              </div>
            </CardContent>
          </Card>

          {/* Statistics */}
          <Card>
            <CardHeader>
              <CardTitle>Statistiken</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                <div className="flex justify-between">
                  <span>Gespeicherte Suchen:</span>
                  <span className="font-semibold">0</span>
                </div>
                <div className="flex justify-between">
                  <span>Favoriten:</span>
                  <span className="font-semibold">0</span>
                </div>
                <div className="flex justify-between">
                  <span>Nachrichten:</span>
                  <span className="font-semibold">0</span>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Firebase Info (Development) */}
        <Card className="mt-8">
          <CardHeader>
            <CardTitle>🔧 Entwickler-Informationen</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="bg-gray-100 p-4 rounded-lg">
              <p className="text-sm text-gray-600 mb-2">Firebase User Object:</p>
              <pre className="text-xs overflow-auto">
                {JSON.stringify({
                  uid: user.uid,
                  email: user.email,
                  displayName: user.displayName,
                  emailVerified: user.emailVerified,
                  photoURL: user.photoURL,
                  providerData: user.providerData,
                }, null, 2)}
              </pre>
            </div>
          </CardContent>
        </Card>
      </main>
    </div>
  );
}
