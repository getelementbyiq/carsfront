// Auto Marketplace TypeScript Types

export interface User {
  id: string;
  firebaseUid: string;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  profileImageUrl?: string;
  userType: UserType;
  status: AccountStatus;
  createdAt: string;
  updatedAt: string;
  lastLoginAt?: string;
  
  // Seller-spezifische Felder
  companyName?: string;
  businessLicense?: string;
  address?: string;
  specializations?: string[];
}

export enum UserType {
  SELLER = 'SELLER',
  CUSTOMER = 'CUSTOMER'
}

export enum AccountStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  SUSPENDED = 'SUSPENDED',
  PENDING_VERIFICATION = 'PENDING_VERIFICATION'
}

export interface Car {
  id: string;
  seller: User;
  
  // Grundlegende Auto-Informationen
  brand: string;
  model: string;
  year: number;
  price: number;
  mileage: number;
  fuelType: string;
  transmission: string;
  
  // Zusätzliche Details
  color?: string;
  doors?: number;
  seats?: number;
  bodyType?: string;
  engineSize?: string;
  horsepower?: number;
  drivetrain?: string;
  
  // Zustand und Historie
  condition: string;
  previousOwners?: number;
  accidentFree: boolean;
  serviceHistory?: string;
  
  // Ausstattung und Features
  features?: string[];
  
  // Bilder
  imageUrls?: string[];
  mainImageUrl?: string;
  
  // Beschreibung und Standort
  description: string;
  location?: string;
  zipCode?: string;
  
  // Status und Timestamps
  status: CarStatus;
  createdAt: string;
  updatedAt: string;
  soldAt?: string;
}

export enum CarStatus {
  ACTIVE = 'ACTIVE',
  SOLD = 'SOLD',
  INACTIVE = 'INACTIVE',
  PENDING_APPROVAL = 'PENDING_APPROVAL',
  REJECTED = 'REJECTED'
}

// API Response Types
export interface ApiResponse<T> {
  data?: T;
  error?: string;
  message?: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

// Form Types
export interface CreateUserRequest {
  firstName: string;
  lastName: string;
  userType: UserType;
}

export interface UpdateUserRequest {
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  profileImageUrl?: string;
}

export interface UpdateSellerRequest {
  companyName?: string;
  businessLicense?: string;
  address?: string;
  specializations?: string[];
}

export interface CreateCarRequest {
  brand: string;
  model: string;
  year: number;
  price: number;
  mileage: number;
  fuelType: string;
  transmission: string;
  color?: string;
  doors?: number;
  seats?: number;
  bodyType?: string;
  engineSize?: string;
  horsepower?: number;
  drivetrain?: string;
  condition: string;
  previousOwners?: number;
  accidentFree: boolean;
  serviceHistory?: string;
  features?: string[];
  imageUrls?: string[];
  mainImageUrl?: string;
  description: string;
  location?: string;
  zipCode?: string;
}

export interface UpdateCarRequest extends Partial<CreateCarRequest> {}

// Search and Filter Types
export interface CarSearchParams {
  brand?: string;
  model?: string;
  minPrice?: number;
  maxPrice?: number;
  minYear?: number;
  maxYear?: number;
  fuelType?: string;
  location?: string;
  page?: number;
  size?: number;
  sort?: string;
}

export interface CarFilters {
  brands: string[];
  fuelTypes: string[];
  transmissions: string[];
  bodyTypes: string[];
  priceRange: {
    min: number;
    max: number;
  };
  yearRange: {
    min: number;
    max: number;
  };
}

// UI Component Types
export interface SelectOption {
  value: string;
  label: string;
}

export interface NavItem {
  name: string;
  href: string;
  icon?: React.ComponentType<{ className?: string }>;
  current?: boolean;
}

// Firebase Auth Types
export interface AuthUser {
  uid: string;
  email: string | null;
  displayName: string | null;
  photoURL: string | null;
  emailVerified: boolean;
}

// Context Types
export interface AuthContextType {
  user: AuthUser | null;
  userProfile: User | null;
  loading: boolean;
  signIn: (email: string, password: string) => Promise<void>;
  signUp: (email: string, password: string, userData: CreateUserRequest) => Promise<void>;
  signOut: () => Promise<void>;
  updateProfile: (data: UpdateUserRequest) => Promise<void>;
  updateSellerInfo: (data: UpdateSellerRequest) => Promise<void>;
}

// Utility Types
export type LoadingState = 'idle' | 'loading' | 'success' | 'error';

export interface AsyncState<T> {
  data: T | null;
  loading: boolean;
  error: string | null;
}

// Constants
export const FUEL_TYPES = [
  'Benzin',
  'Diesel',
  'Elektro',
  'Hybrid',
  'Plug-in Hybrid',
  'Wasserstoff',
  'Autogas (LPG)',
  'Erdgas (CNG)'
] as const;

export const TRANSMISSION_TYPES = [
  'Manuell',
  'Automatik',
  'Halbautomatik',
  'CVT'
] as const;

export const BODY_TYPES = [
  'Limousine',
  'Kombi',
  'SUV',
  'Cabrio',
  'Coupé',
  'Kleinwagen',
  'Van',
  'Pick-up',
  'Sportwagen'
] as const;

export const CAR_CONDITIONS = [
  'Neu',
  'Gebraucht',
  'Jahreswagen',
  'Vorführwagen',
  'Unfallfrei',
  'Repariert'
] as const;

export const DRIVETRAIN_TYPES = [
  'Frontantrieb',
  'Heckantrieb',
  'Allradantrieb'
] as const;
