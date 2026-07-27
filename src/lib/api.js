import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Attach JWT token to every outgoing request, if present
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('bookstore_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Centralize 401 handling — expired/invalid token sends the user back to login
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('bookstore_token');
      localStorage.removeItem('bookstore_user');
      if (!window.location.pathname.startsWith('/login')) {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;

/** Pulls the human-readable message out of the backend's ApiResponse error shape. */
export function extractErrorMessage(error, fallback = 'Something went wrong. Please try again.') {
  return (
    error?.response?.data?.message ||
    error?.response?.data?.error ||
    error?.message ||
    fallback
  );
}
