import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useWishlist } from '../context/WishlistContext';
import { useCart } from '../context/CartContext';
import { Loader, EmptyState } from '../components/Feedback';
import { ShoppingBag, X } from 'lucide-react';

export default function Wishlist() {
  const { wishlist, refreshWishlist, toggleWishlist, clearWishlist } = useWishlist();
  const { addToCart } = useCart();

  useEffect(() => {
    refreshWishlist();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  if (!wishlist) return <Loader label="Fetching your wishlist…" />;

  if (!wishlist.items || wishlist.items.length === 0) {
    return (
      <EmptyState
        title="Nothing saved yet"
        message="Tap the ribbon on any book to keep it here for later."
        action={
          <Link to="/" className="bg-wine hover:bg-wine-dark text-paper font-semibold px-5 py-2.5 rounded-sm transition-colors">
            Browse the catalog
          </Link>
        }
      />
    );
  }

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 py-10 animate-page-in">
      <div className="flex items-center justify-between mb-8">
        <h1 className="font-display text-3xl text-ink">Your wishlist</h1>
        <button onClick={clearWishlist} className="text-sm text-ink-faint hover:text-wine">
          Clear all
        </button>
      </div>

      <ul className="grid sm:grid-cols-2 gap-4">
        {wishlist.items.map((item) => (
          <li key={item.productId} className="flex gap-4 border border-line rounded-sm p-4 bg-paper-deep/40 relative">
            <button
              onClick={() => toggleWishlist(item.productId)}
              className="absolute top-2 right-2 text-ink-faint hover:text-wine"
              aria-label="Remove from wishlist"
            >
              <X size={16} />
            </button>
            <Link to={`/books/${item.productId}`} className="w-16 h-20 bg-ink/5 rounded-sm overflow-hidden shrink-0">
              {item.imageUrl && (
                <img src={item.imageUrl} alt={item.title} className="w-full h-full object-cover" />
              )}
            </Link>
            <div className="flex-1 min-w-0">
              <Link to={`/books/${item.productId}`}>
                <p className="font-display text-lg text-ink truncate hover:text-wine">{item.title}</p>
              </Link>
              <p className="text-sm text-ink-soft">{item.author}</p>
              <p className="font-mono text-sm text-ink mt-1">₹{Number(item.price).toFixed(2)}</p>
              <button
                onClick={() => addToCart(item.productId, 1)}
                className="flex items-center gap-1.5 text-xs font-semibold text-wine hover:text-wine-dark mt-2"
              >
                <ShoppingBag size={14} /> Add to cart
              </button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
