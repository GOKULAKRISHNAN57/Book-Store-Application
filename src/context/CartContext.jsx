import { createContext, useContext, useState, useCallback, useEffect } from 'react';
import api, { extractErrorMessage } from '../lib/api';
import { useAuth } from './AuthContext';
import toast from 'react-hot-toast';

const CartContext = createContext(null);

export function CartProvider({ children }) {
  const { user } = useAuth();
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(false);

  const refreshCart = useCallback(async () => {
    if (!user) {
      setCart(null);
      return;
    }
    setLoading(true);
    try {
      const res = await api.get('/cart');
      setCart(res.data.data);
    } catch {
      // No cart yet is a normal state for a brand-new account — treat as empty
      setCart(null);
    } finally {
      setLoading(false);
    }
  }, [user]);

  const addToCart = useCallback(async (productId, quantity = 1) => {
    if (!user) {
      toast.error('Please log in to add books to your cart.');
      return { success: false };
    }
    try {
      const res = await api.post('/cart/add', { productId, quantity });
      setCart(res.data.data);
      toast.success('Added to cart.');
      return { success: true };
    } catch (error) {
      const msg = extractErrorMessage(error, 'Could not add to cart.');
      toast.error(msg);
      return { success: false, message: msg };
    }
  }, [user]);

  const updateQuantity = useCallback(async (productId, quantity) => {
    try {
      const res = await api.put('/cart/update', { productId, quantity });
      setCart(res.data.data);
      return { success: true };
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not update quantity.'));
      return { success: false };
    }
  }, []);

  const removeFromCart = useCallback(async (productId) => {
    try {
      const res = await api.delete(`/cart/remove/${productId}`);
      setCart(res.data.data);
      toast.success('Removed from cart.');
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not remove item.'));
    }
  }, []);

  const clearCart = useCallback(async () => {
    try {
      await api.delete('/cart/clear');
      setCart(null);
      toast.success('Cart cleared.');
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not clear cart.'));
    }
  }, []);

  useEffect(() => {
    refreshCart();
  }, [user, refreshCart]);

  const itemCount = cart?.items?.reduce((sum, item) => sum + item.quantity, 0) || 0;

  return (
    <CartContext.Provider
      value={{ cart, loading, itemCount, refreshCart, addToCart, updateQuantity, removeFromCart, clearCart }}
    >
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  const ctx = useContext(CartContext);
  if (!ctx) throw new Error('useCart must be used within CartProvider');
  return ctx;
}
