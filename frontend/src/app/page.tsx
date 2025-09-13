'use client';

import React from 'react';
import Link from 'next/link';
import Button from '@/components/ui/Button';

export default function HomePage() {
  return (
    <div className="min-h-screen">
      {/* Navigation */}
      <nav className="bg-white shadow-sm border-b border-secondary-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <Link href="/" className="flex items-center space-x-2">
                <span className="text-2xl">🚗</span>
                <span className="text-xl font-bold text-secondary-900">AutoMarkt</span>
              </Link>
            </div>
            
            <div className="hidden md:flex items-center space-x-8">
              <Link href="/cars" className="nav-link">Alle Autos</Link>
              <Link href="/sellers" className="nav-link">Verkäufer</Link>
            </div>
            
            <div className="flex items-center space-x-4">
              <Link href="/auth/login">
                <Button variant="ghost" size="sm">Anmelden</Button>
              </Link>
              <Link href="/auth/register">
                <Button size="sm">Registrieren</Button>
              </Link>
            </div>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="hero-gradient text-white py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h1 className="text-4xl md:text-6xl font-bold mb-6">
            Finden Sie Ihr <span className="text-yellow-300">Traumauto</span>
          </h1>
          <p className="text-xl md:text-2xl mb-8 text-blue-100 max-w-3xl mx-auto">
            Deutschlands modernster Marktplatz für Gebrauchtwagen. 
            Einfach, sicher und transparent.
          </p>
          
          <div className="max-w-lg mx-auto">
            <div className="relative mb-4">
              <span className="absolute left-3 top-1/2 transform -translate-y-1/2 text-lg">🔍</span>
              <input
                type="text"
                placeholder="Marke, Modell oder Stichwort eingeben..."
                className="w-full pl-10 pr-4 py-3 border border-secondary-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500 text-secondary-900"
              />
            </div>
            <Button size="lg" className="bg-yellow-500 hover:bg-yellow-600 text-secondary-900">
              Jetzt suchen
            </Button>
          </div>
        </div>
      </section>

      {/* Features */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold text-secondary-900 mb-4">
              Warum AutoMarkt?
            </h2>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 text-center">
            <div>
              <div className="bg-primary-100 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-6">
                <span className="text-3xl">🚗</span>
              </div>
              <h3 className="text-xl font-semibold text-secondary-900 mb-4">
                Große Auswahl
              </h3>
              <p className="text-secondary-600">
                Tausende von Fahrzeugen von verifizierten Verkäufern.
              </p>
            </div>
            
            <div>
              <div className="bg-success-100 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-6">
                <span className="text-3xl">🔍</span>
              </div>
              <h3 className="text-xl font-semibold text-secondary-900 mb-4">
                Einfache Suche
              </h3>
              <p className="text-secondary-600">
                Finden Sie mit intelligenten Filtern schnell Ihr Traumauto.
              </p>
            </div>
            
            <div>
              <div className="bg-warning-100 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-6">
                <span className="text-3xl">🛡️</span>
              </div>
              <h3 className="text-xl font-semibold text-secondary-900 mb-4">
                Sicher & Transparent
              </h3>
              <p className="text-secondary-600">
                Alle Verkäufer werden verifiziert. Transparente Preise.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="py-16 bg-primary-600 text-white text-center">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">
            Bereit für Ihr nächstes Auto?
          </h2>
          <p className="text-xl mb-8 text-primary-100 max-w-2xl mx-auto">
            Registrieren Sie sich kostenlos und starten Sie noch heute
          </p>
          
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link href="/auth/register?type=customer">
              <Button size="lg" className="bg-white text-primary-600 hover:bg-secondary-50">
                Als Käufer registrieren
              </Button>
            </Link>
            <Link href="/auth/register?type=seller">
              <Button size="lg" variant="outline" className="border-white text-white hover:bg-white hover:text-primary-600">
                Als Verkäufer registrieren
              </Button>
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
}