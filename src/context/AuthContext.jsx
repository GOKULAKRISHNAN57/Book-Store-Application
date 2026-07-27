import { createContext, useContext, useState, useCallback } from 'react';
import api, { extractErrorMessage } from '../lib/api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      const stored = localStorage.getItem('bookstore_user');
      return stored ? JSON.parse(stored) : null;
    } catch {
      // Corrupted or stale data from an earlier session shouldn't crash the whole app
      localStorage.removeItem('bookstore_user');
      localStorage.removeItem('bookstore_token');
      return null;
    }
  });

  const login = useCallback(async (email, password) => {
    try {
      const res = await api.post('/users/login', { email, password });
      const data = res.data.data;
      const loggedInUser = {
        id: data.id,
        fullName: data.fullName,
        email: data.email,
        role: data.role,
      };
      localStorage.setItem('bookstore_token', data.token);
      localStorage.setItem('bookstore_user', JSON.stringify(loggedInUser));
      setUser(loggedInUser);
      return { success: true };
    } catch (error) {
      return { success: false, message: extractErrorMessage(error, 'Login failed.') };
    }
  }, []);

  const register = useCallback(async (payload) => {
    try {
      await api.post('/users/register', payload);
      return { success: true };
    } catch (error) {
      return { success: false, message: extractErrorMessage(error, 'Registration failed.') };
    }
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('bookstore_token');
    localStorage.removeItem('bookstore_user');
    setUser(null);
  }, []);

  const isAdmin = user?.role === 'ROLE_ADMIN';

  return (
    <AuthContext.Provider value={{ user, login, register, logout, isAdmin }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
