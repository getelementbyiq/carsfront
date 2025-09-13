// API Client für Auto Marketplace Backend

import axios, { AxiosInstance, AxiosResponse } from 'axios';
import { auth } from './firebase';
import toast from 'react-hot-toast';

// API Base URL aus Umgebungsvariablen
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

// Erstelle Axios Instanz
const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000, // 10 Sekunden Timeout
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request Interceptor - Füge Firebase JWT Token hinzu
apiClient.interceptors.request.use(
  async (config) => {
    try {
      // Hole aktuellen User
      const currentUser = auth.currentUser;
      
      if (currentUser) {
        // Hole frischen JWT Token
        const token = await currentUser.getIdToken();
        
        // Füge Authorization Header hinzu
        config.headers.Authorization = `Bearer ${token}`;
      }
      
      return config;
    } catch (error) {
      console.error('Fehler beim Hinzufügen des Auth Tokens:', error);
      return config;
    }
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response Interceptor - Handle Errors
apiClient.interceptors.response.use(
  (response: AxiosResponse) => {
    return response;
  },
  (error) => {
    // Handle verschiedene Error-Typen
    if (error.response) {
      // Server hat mit Error-Status geantwortet
      const { status, data } = error.response;
      
      switch (status) {
        case 401:
          // Unauthorized - User ist nicht authentifiziert
          toast.error('Bitte melden Sie sich an');
          // Optional: Redirect zu Login
          if (typeof window !== 'undefined') {
            window.location.href = '/auth/login';
          }
          break;
          
        case 403:
          // Forbidden - User hat keine Berechtigung
          toast.error('Keine Berechtigung für diese Aktion');
          break;
          
        case 404:
          // Not Found
          toast.error('Ressource nicht gefunden');
          break;
          
        case 422:
          // Validation Error
          const message = data?.error || 'Validierungsfehler';
          toast.error(message);
          break;
          
        case 500:
          // Server Error
          toast.error('Serverfehler. Bitte versuchen Sie es später erneut.');
          break;
          
        default:
          // Andere Errors
          const errorMessage = data?.error || data?.message || 'Ein Fehler ist aufgetreten';
          toast.error(errorMessage);
      }
    } else if (error.request) {
      // Request wurde gesendet, aber keine Antwort erhalten
      toast.error('Verbindungsfehler. Prüfen Sie Ihre Internetverbindung.');
    } else {
      // Anderer Fehler
      toast.error('Ein unerwarteter Fehler ist aufgetreten');
    }
    
    return Promise.reject(error);
  }
);

// API Helper Functions
export const api = {
  // GET Request
  get: <T>(url: string, params?: any): Promise<AxiosResponse<T>> => {
    return apiClient.get(url, { params });
  },
  
  // POST Request
  post: <T>(url: string, data?: any): Promise<AxiosResponse<T>> => {
    return apiClient.post(url, data);
  },
  
  // PUT Request
  put: <T>(url: string, data?: any): Promise<AxiosResponse<T>> => {
    return apiClient.put(url, data);
  },
  
  // PATCH Request
  patch: <T>(url: string, data?: any): Promise<AxiosResponse<T>> => {
    return apiClient.patch(url, data);
  },
  
  // DELETE Request
  delete: <T>(url: string): Promise<AxiosResponse<T>> => {
    return apiClient.delete(url);
  },
};

// Spezifische API Endpoints
export const userAPI = {
  // User Profile Management
  createProfile: (data: any) => api.post('/users/profile', data),
  getCurrentUser: () => api.get('/users/me'),
  updateProfile: (data: any) => api.put('/users/me', data),
  updateSellerInfo: (data: any) => api.put('/users/seller-info', data),
  deactivateAccount: () => api.delete('/users/me'),
  
  // Public User Endpoints
  getSellers: () => api.get('/users/sellers'),
  searchSellers: (specialization: string) => 
    api.get('/users/sellers/search', { specialization }),
};

export const carAPI = {
  // Car Management (Authenticated)
  createCar: (data: any) => api.post('/cars', data),
  updateCar: (id: string, data: any) => api.put(`/cars/${id}`, data),
  markAsSold: (id: string) => api.patch(`/cars/${id}/sold`),
  deleteCar: (id: string) => api.delete(`/cars/${id}`),
  getMyCars: () => api.get('/cars/my-cars'),
  
  // Public Car Endpoints
  getAllCars: (params?: any) => api.get('/cars', params),
  getCarById: (id: string) => api.get(`/cars/${id}`),
  searchCars: (params: any) => api.get('/cars/search', params),
  getSimilarCars: (id: string) => api.get(`/cars/${id}/similar`),
  getStats: () => api.get('/cars/stats'),
};

// Health Check
export const healthAPI = {
  check: () => api.get('/health'),
};

// Export default API client
export default apiClient;
