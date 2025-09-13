'use client';

import React, { useState } from 'react';
import Link from 'next/link';
import { useRouter, useSearchParams } from 'next/navigation';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { createUserWithEmailAndPassword, signInWithPopup, updateProfile } from 'firebase/auth';
import { auth, googleProvider } from '@/lib/firebase';
import Button from '@/components/ui/Button';
import Input from '@/components/ui/Input';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import toast from 'react-hot-toast';

// Validation Schema
const registerSchema = z.object({
  firstName: z.string().min(2, 'Vorname muss mindestens 2 Zeichen haben'),
  lastName: z.string().min(2, 'Nachname muss mindestens 2 Zeichen haben'),
  email: z.string().email('Ungültige E-Mail-Adresse'),
  password: z.string().min(6, 'Passwort muss mindestens 6 Zeichen haben'),
  confirmPassword: z.string(),
  userType: z.enum(['CUSTOMER', 'SELLER'], {
    required_error: 'Bitte wählen Sie einen Kontotyp',
  }),
  agreeToTerms: z.boolean().refine((val) => val === true, {
    message: 'Sie müssen den AGB zustimmen',
  }),
}).refine((data) => data.password === data.confirmPassword, {
  message: 'Passwörter stimmen nicht überein',
  path: ['confirmPassword'],
});

type RegisterForm = z.infer<typeof registerSchema>;

export default function RegisterPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [loading, setLoading] = useState(false);
  const [googleLoading, setGoogleLoading] = useState(false);

  // Get user type from URL params
  const defaultUserType = searchParams.get('type') === 'seller' ? 'SELLER' : 'CUSTOMER';

  const {
    register,
    handleSubmit,
    formState: { errors },
    watch,
  } = useForm<RegisterForm>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      userType: defaultUserType,
    },
  });

  const userType = watch('userType');

  // Email/Password Registration
  const onSubmit = async (data: RegisterForm) => {
    setLoading(true);
    try {
      // Create Firebase user
      const userCredential = await createUserWithEmailAndPassword(
        auth,
        data.email,
        data.password
      );

      // Update display name
      await updateProfile(userCredential.user, {
        displayName: `${data.firstName} ${data.lastName}`,
      });

      toast.success('Konto erfolgreich erstellt!');
      router.push('/dashboard');
    } catch (error: any) {
      console.error('Registration error:', error);
      
      const errorMessages: { [key: string]: string } = {
        'auth/email-already-in-use': 'Diese E-Mail-Adresse wird bereits verwendet.',
        'auth/invalid-email': 'Ungültige E-Mail-Adresse.',
        'auth/operation-not-allowed': 'E-Mail/Passwort-Registrierung ist nicht aktiviert.',
        'auth/weak-password': 'Das Passwort ist zu schwach. Verwenden Sie mindestens 6 Zeichen.',
      };
      
      const message = errorMessages[error.code] || 'Registrierung fehlgeschlagen. Versuchen Sie es erneut.';
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  // Google Registration
  const handleGoogleRegister = async () => {
    setGoogleLoading(true);
    try {
      await signInWithPopup(auth, googleProvider);
      toast.success('Erfolgreich mit Google registriert!');
      router.push('/dashboard');
    } catch (error: any) {
      console.error('Google registration error:', error);
      
      if (error.code === 'auth/popup-closed-by-user') {
        toast.error('Registrierung abgebrochen.');
      } else if (error.code === 'auth/account-exists-with-different-credential') {
        toast.error('Ein Konto mit dieser E-Mail existiert bereits mit anderen Anmeldedaten.');
      } else {
        toast.error('Google-Registrierung fehlgeschlagen. Versuchen Sie es erneut.');
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
              Konto erstellen
            </CardTitle>
            <p className="text-gray-600 mt-2">
              Registrieren Sie sich als {userType === 'SELLER' ? 'Verkäufer' : 'Käufer'}
            </p>
          </CardHeader>

          <CardContent className="pt-6">
            {/* User Type Selection */}
            <div className="mb-6">
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Kontotyp
              </label>
              <div className="grid grid-cols-2 gap-3">
                <label className="relative">
                  <input
                    type="radio"
                    value="CUSTOMER"
                    {...register('userType')}
                    className="sr-only"
                  />
                  <div className={`p-4 border-2 rounded-lg cursor-pointer transition-all ${
                    userType === 'CUSTOMER'
                      ? 'border-blue-500 bg-blue-50'
                      : 'border-gray-200 hover:border-gray-300'
                  }`}>
                    <div className="text-center">
                      <div className="text-2xl mb-2">🚗</div>
                      <div className="font-medium text-sm">Käufer</div>
                      <div className="text-xs text-gray-500">Auto suchen</div>
                    </div>
                  </div>
                </label>
                
                <label className="relative">
                  <input
                    type="radio"
                    value="SELLER"
                    {...register('userType')}
                    className="sr-only"
                  />
                  <div className={`p-4 border-2 rounded-lg cursor-pointer transition-all ${
                    userType === 'SELLER'
                      ? 'border-blue-500 bg-blue-50'
                      : 'border-gray-200 hover:border-gray-300'
                  }`}>
                    <div className="text-center">
                      <div className="text-2xl mb-2">🏪</div>
                      <div className="font-medium text-sm">Verkäufer</div>
                      <div className="text-xs text-gray-500">Auto verkaufen</div>
                    </div>
                  </div>
                </label>
              </div>
              {errors.userType && (
                <p className="mt-1 text-sm text-red-600">{errors.userType.message}</p>
              )}
            </div>

            {/* Google Registration Button */}
            <Button
              onClick={handleGoogleRegister}
              loading={googleLoading}
              variant="outline"
              className="w-full mb-6 border-gray-300 hover:bg-gray-50"
              size="lg"
            >
              <span className="mr-3">🔍</span>
              Mit Google registrieren
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

            {/* Registration Form */}
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <Input
                  label="Vorname"
                  placeholder="Max"
                  error={errors.firstName?.message}
                  {...register('firstName')}
                />
                <Input
                  label="Nachname"
                  placeholder="Mustermann"
                  error={errors.lastName?.message}
                  {...register('lastName')}
                />
              </div>

              <Input
                label="E-Mail-Adresse"
                type="email"
                placeholder="max@beispiel.de"
                error={errors.email?.message}
                {...register('email')}
              />

              <Input
                label="Passwort"
                type="password"
                placeholder="Mindestens 6 Zeichen"
                error={errors.password?.message}
                {...register('password')}
              />

              <Input
                label="Passwort bestätigen"
                type="password"
                placeholder="Passwort wiederholen"
                error={errors.confirmPassword?.message}
                {...register('confirmPassword')}
              />

              {/* Terms Agreement */}
              <div className="flex items-start">
                <input
                  type="checkbox"
                  {...register('agreeToTerms')}
                  className="mt-1 rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                />
                <div className="ml-2">
                  <label className="text-sm text-gray-600">
                    Ich stimme den{' '}
                    <Link href="/terms" className="text-blue-600 hover:text-blue-500">
                      Allgemeinen Geschäftsbedingungen
                    </Link>{' '}
                    und der{' '}
                    <Link href="/privacy" className="text-blue-600 hover:text-blue-500">
                      Datenschutzerklärung
                    </Link>{' '}
                    zu.
                  </label>
                  {errors.agreeToTerms && (
                    <p className="mt-1 text-sm text-red-600">{errors.agreeToTerms.message}</p>
                  )}
                </div>
              </div>

              <Button
                type="submit"
                loading={loading}
                className="w-full"
                size="lg"
              >
                Konto erstellen
              </Button>
            </form>

            {/* Login Link */}
            <div className="mt-6 text-center">
              <p className="text-gray-600">
                Bereits ein Konto?{' '}
                <Link
                  href="/auth/login"
                  className="text-blue-600 hover:text-blue-500 font-medium"
                >
                  Jetzt anmelden
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
