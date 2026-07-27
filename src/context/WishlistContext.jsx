import { createContext, useContext, useState, useCallback, useEffect } from 'react';
import api, { extractErrorMessage } from '../lib/api';
import { useAuth } from './AuthContext';
import toast from 'react-hot-toast';

const WishlistContext = createContext(null);

export function WishlistProvider({ children }) {
  const { user } = useAuth();
  const [wishlist, setWishlist] = useState(null);

  const refreshWishlist = useCallback(async () => {
    if (!user) {
      setWishlist(null);
      return;
    }
    try {
      const res = await api.get('/wishlist');
      setWishlist(res.data.data);
    } catch {
      setWishlist(null);
    }
  }, [user]);

  const isInWishlist = useCallback(
    (productId) => wishlist?.items?.some((i) => i.productId === productId) || false,
    [wishlist]
  );

  const toggleWishlist = useCallback(async (productId) => {
    if (!user) {
      toast.error('Please log in to save books to your wishlist.');
      return;
    }
    try {
      if (isInWishlist(productId)) {
        const res = await api.delete(`/wishlist/remove/${productId}`);
        setWishlist(res.data.data);
        toast.success('Removed from wishlist.');
      } else {
        const res = await api.post('/wishlist/add', { productId });
        setWishlist(res.data.data);
        toast.success('Saved to wishlist.');
      }
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not update wishlist.'));
    }
  }, [user, isInWishlist]);

  const clearWishlist = useCallback(async () => {
    try {
      await api.delete('/wishlist/clear');
      setWishlist(null);
      toast.success('Wishlist cleared.');
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not clear wishlist.'));
    }
  }, []);

  useEffect(() => {
    refreshWishlist();
  }, [user, refreshWishlist]);

  return (
    <WishlistContext.Provider
      value={{ wishlist, refreshWishlist, isInWishlist, toggleWishlist, clearWishlist }}
    >
      {children}
    </WishlistContext.Provider>
  );
}

export function useWishlist() {
  const ctx = useContext(WishlistContext);
  if (!ctx) throw new Error('useWishlist must be used within WishlistProvider');
  return ctx;
}
