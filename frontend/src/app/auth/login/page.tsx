'use client';

import React, { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { signInWithEmailAndPassword, signInWithPopup } from 'firebase/auth';
import { auth, googleProvider } from '@/lib/firebase';
import Button from '@/components/ui/Button';
import Input from '@/components/ui/Input';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import toast from 'react-hot-toast';

// Validation Schema
const loginSchema = z.object({
  email: z.string().email('Ungültige E-Mail-Adresse'),
  password: z.string().min(6, 'Passwort muss mindestens 6 Zeichen haben'),
});

type LoginForm = z.infer<typeof loginSchema>;

export default function LoginPage() {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [googleLoading, setGoogleLoading] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginForm>({
    resolver: zodResolver(loginSchema),
  });

  // Email/Password Login
  const onSubmit = async (data: LoginForm) => {
    setLoading(true);
    try {
      await signInWithEmailAndPassword(auth, data.email, data.password);
      toast.success('Erfolgreich angemeldet!');
      router.push('/dashboard');
    } catch (error: any) {
      console.error('Login error:', error);
      
      const errorMessages: { [key: string]: string } = {
        'auth/user-not-found': 'Kein Benutzer mit dieser E-Mail gefunden.',
        'auth/wrong-password': 'Falsches Passwort.',
        'auth/invalid-email': 'Ungültige E-Mail-Adresse.',
        'auth/user-disabled': 'Dieser Benutzer wurde deaktiviert.',
        'auth/too-many-requests': 'Zu viele Anmeldeversuche. Versuchen Sie es später erneut.',
        'auth/invalid-credential': 'Ungültige Anmeldedaten.',
      };
      
      const message = errorMessages[error.code] || 'Anmeldung fehlgeschlagen. Versuchen Sie es erneut.';
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  // Google Login
  const handleGoogleLogin = async () => {
    setGoogleLoading(true);
    try {
      await signInWithPopup(auth, googleProvider);
      toast.success('Erfolgreich mit Google angemeldet!');
      router.push('/dashboard');
    } catch (error: any) {
      console.error('Google login error:', error);
      
      if (error.code === 'auth/popup-closed-by-user') {
        toast.error('Anmeldung abgebrochen.');
      } else {
        toast.error('Google-Anmeldung fehlgeschlagen. Versuchen Sie es erneut.');
      }
    } finally {
      setGoogleLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-purple-50 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        {/* Logo */}
        <div className="text-center mb-8">
          <Link href="/" className="inline-flex items-center space-x-2">
            <div className="w-12 h-12 bg-gradient-to-r from-blue-600 to-purple-600 rounded-xl flex items-center justify-center">
              <span className="text-white font-bold text-xl">🚗</span>
            </div>
            <span className="text-2xl font-bold text-gray-900">AutoMarkt</span>
          </Link>
        </div>

        <Card className="shadow-2xl border-0">
          <CardHeader className="text-center pb-2">
            <CardTitle className="text-2xl font-bold text-gray-900">
              Willkommen zurück
            </CardTitle>
            <p className="text-gray-600 mt-2">
              Melden Sie sich in Ihrem Konto an
            </p>
          </CardHeader>

          <CardContent className="pt-6">
            {/* Google Login Button */}
            <Button
              onClick={handleGoogleLogin}
              loading={googleLoading}
              variant="outline"
              className="w-full mb-6 border-gray-300 hover:bg-gray-50"
              size="lg"
            >
              <span className="mr-3">🔍</span>
              Mit Google anmelden
            </Button>

            {/* Divider */}
            <div className="relative mb-6">
              <div className="absolute inset-0 flex items-center">
                <div className="w-full border-t border-gray-300" />
              </div>
              <div className="relative flex justify-center text-sm">
                <span className="px-2 bg-white text-gray-500">oder</span>
              </div>
            </div>

            {/* Email/Password Form */}
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
              <Input
                label="E-Mail-Adresse"
                type="email"
                placeholder="ihre@email.de"
                error={errors.email?.message}
                {...register('email')}
              />

              <Input
                label="Passwort"
                type="password"
                placeholder="Ihr Passwort"
                error={errors.password?.message}
                {...register('password')}
              />

              <div className="flex items-center justify-between">
                <label className="flex items-center">
                  <input
                    type="checkbox"
                    className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                  />
                  <span className="ml-2 text-sm text-gray-600">Angemeldet bleiben</span>
                </label>
                <Link
                  href="/auth/forgot-password"
                  className="text-sm text-blue-600 hover:text-blue-500"
                >
                  Passwort vergessen?
                </Link>
              </div>

              <Button
                type="submit"
                loading={loading}
                className="w-full"
                size="lg"
              >
                Anmelden
              </Button>
            </form>

            {/* Register Link */}
            <div className="mt-6 text-center">
              <p className="text-gray-600">
                Noch kein Konto?{' '}
                <Link
                  href="/auth/register"
                  className="text-blue-600 hover:text-blue-500 font-medium"
                >
                  Jetzt registrieren
                </Link>
              </p>
            </div>
          </CardContent>
        </Card>

        {/* Back to Home */}
        <div className="text-center mt-6">
          <Link
            href="/"
            className="text-gray-500 hover:text-gray-700 text-sm"
          >
            ← Zurück zur Startseite
          </Link>
        </div>
      </div>
    </div>
  );
}
