// Utility Functions für Auto Marketplace

import { type ClassValue, clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';

/**
 * Kombiniert Tailwind CSS Klassen intelligent
 * Verwendet clsx für bedingte Klassen und tailwind-merge für Konflikte
 */
export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

/**
 * Formatiert Preise in Euro
 */
export function formatPrice(price: number): string {
  return new Intl.NumberFormat('de-DE', {
    style: 'currency',
    currency: 'EUR',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(price);
}

/**
 * Formatiert Kilometerstand
 */
export function formatMileage(mileage: number): string {
  return new Intl.NumberFormat('de-DE').format(mileage) + ' km';
}

/**
 * Formatiert Datum
 */
export function formatDate(dateString: string): string {
  return new Intl.DateTimeFormat('de-DE', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  }).format(new Date(dateString));
}

/**
 * Formatiert Datum relativ (z.B. "vor 2 Tagen")
 */
export function formatRelativeDate(dateString: string): string {
  const date = new Date(dateString);
  const now = new Date();
  const diffInSeconds = Math.floor((now.getTime() - date.getTime()) / 1000);
  
  if (diffInSeconds < 60) {
    return 'gerade eben';
  }
  
  const diffInMinutes = Math.floor(diffInSeconds / 60);
  if (diffInMinutes < 60) {
    return `vor ${diffInMinutes} Minute${diffInMinutes !== 1 ? 'n' : ''}`;
  }
  
  const diffInHours = Math.floor(diffInMinutes / 60);
  if (diffInHours < 24) {
    return `vor ${diffInHours} Stunde${diffInHours !== 1 ? 'n' : ''}`;
  }
  
  const diffInDays = Math.floor(diffInHours / 24);
  if (diffInDays < 30) {
    return `vor ${diffInDays} Tag${diffInDays !== 1 ? 'en' : ''}`;
  }
  
  const diffInMonths = Math.floor(diffInDays / 30);
  if (diffInMonths < 12) {
    return `vor ${diffInMonths} Monat${diffInMonths !== 1 ? 'en' : ''}`;
  }
  
  const diffInYears = Math.floor(diffInMonths / 12);
  return `vor ${diffInYears} Jahr${diffInYears !== 1 ? 'en' : ''}`;
}

/**
 * Generiert Initialen aus Namen
 */
export function getInitials(firstName: string, lastName: string): string {
  return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
}

/**
 * Validiert Email-Adresse
 */
export function isValidEmail(email: string): boolean {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

/**
 * Generiert URL-freundlichen Slug
 */
export function generateSlug(text: string): string {
  return text
    .toLowerCase()
    .replace(/[äöüß]/g, (match) => {
      const replacements: { [key: string]: string } = {
        'ä': 'ae',
        'ö': 'oe',
        'ü': 'ue',
        'ß': 'ss'
      };
      return replacements[match] || match;
    })
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/^-+|-+$/g, '');
}

/**
 * Erstellt Auto-Titel für SEO und Anzeige
 */
export function createCarTitle(brand: string, model: string, year: number): string {
  return `${brand} ${model} (${year})`;
}

/**
 * Erstellt Auto-URL
 */
export function createCarUrl(id: string, brand: string, model: string, year: number): string {
  const slug = generateSlug(`${brand} ${model} ${year}`);
  return `/cars/${id}/${slug}`;
}

/**
 * Debounce Funktion für Suche
 */
export function debounce<T extends (...args: any[]) => any>(
  func: T,
  wait: number
): (...args: Parameters<T>) => void {
  let timeout: NodeJS.Timeout;
  
  return (...args: Parameters<T>) => {
    clearTimeout(timeout);
    timeout = setTimeout(() => func(...args), wait);
  };
}

/**
 * Throttle Funktion
 */
export function throttle<T extends (...args: any[]) => any>(
  func: T,
  limit: number
): (...args: Parameters<T>) => void {
  let inThrottle: boolean;
  
  return (...args: Parameters<T>) => {
    if (!inThrottle) {
      func(...args);
      inThrottle = true;
      setTimeout(() => (inThrottle = false), limit);
    }
  };
}

/**
 * Prüft ob Wert leer ist
 */
export function isEmpty(value: any): boolean {
  if (value == null) return true;
  if (typeof value === 'string') return value.trim().length === 0;
  if (Array.isArray(value)) return value.length === 0;
  if (typeof value === 'object') return Object.keys(value).length === 0;
  return false;
}

/**
 * Sicherer JSON Parse
 */
export function safeJsonParse<T>(str: string, fallback: T): T {
  try {
    return JSON.parse(str);
  } catch {
    return fallback;
  }
}

/**
 * Erstellt Query String aus Objekt
 */
export function createQueryString(params: Record<string, any>): string {
  const searchParams = new URLSearchParams();
  
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') {
      searchParams.append(key, String(value));
    }
  });
  
  return searchParams.toString();
}

/**
 * Extrahiert Query Parameter aus URL
 */
export function getQueryParams(url: string): Record<string, string> {
  const params: Record<string, string> = {};
  const searchParams = new URLSearchParams(url.split('?')[1] || '');
  
  searchParams.forEach((value, key) => {
    params[key] = value;
  });
  
  return params;
}

/**
 * Kopiert Text in Zwischenablage
 */
export async function copyToClipboard(text: string): Promise<boolean> {
  try {
    await navigator.clipboard.writeText(text);
    return true;
  } catch {
    // Fallback für ältere Browser
    const textArea = document.createElement('textarea');
    textArea.value = text;
    document.body.appendChild(textArea);
    textArea.select();
    const success = document.execCommand('copy');
    document.body.removeChild(textArea);
    return success;
  }
}

/**
 * Scrollt zu Element
 */
export function scrollToElement(elementId: string, offset: number = 0): void {
  const element = document.getElementById(elementId);
  if (element) {
    const elementPosition = element.getBoundingClientRect().top + window.pageYOffset;
    window.scrollTo({
      top: elementPosition - offset,
      behavior: 'smooth'
    });
  }
}

/**
 * Prüft ob Element im Viewport ist
 */
export function isElementInViewport(element: Element): boolean {
  const rect = element.getBoundingClientRect();
  return (
    rect.top >= 0 &&
    rect.left >= 0 &&
    rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
    rect.right <= (window.innerWidth || document.documentElement.clientWidth)
  );
}

/**
 * Generiert zufällige ID
 */
export function generateId(length: number = 8): string {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  let result = '';
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return result;
}

/**
 * Validiert Telefonnummer (deutsche Formate)
 */
export function isValidPhoneNumber(phone: string): boolean {
  const phoneRegex = /^(\+49|0)[1-9]\d{1,14}$/;
  return phoneRegex.test(phone.replace(/\s+/g, ''));
}

/**
 * Formatiert Telefonnummer
 */
export function formatPhoneNumber(phone: string): string {
  const cleaned = phone.replace(/\D/g, '');
  
  if (cleaned.startsWith('49')) {
    return `+49 ${cleaned.slice(2, 5)} ${cleaned.slice(5)}`;
  } else if (cleaned.startsWith('0')) {
    return `${cleaned.slice(0, 4)} ${cleaned.slice(4)}`;
  }
  
  return phone;
}
