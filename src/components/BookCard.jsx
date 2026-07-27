import { Link } from 'react-router-dom';
import { Heart, ShoppingBag } from 'lucide-react';
import { useState } from 'react';
import { useCart } from '../context/CartContext';
import { useWishlist } from '../context/WishlistContext';

export default function BookCard({ book }) {
  const { addToCart } = useCart();
  const { isInWishlist, toggleWishlist } = useWishlist();
  const inWishlist = isInWishlist(book.id);
  const outOfStock = book.stockQuantity <= 0;
  const [imageFailed, setImageFailed] = useState(false);

  return (
    <div className="group relative bg-paper-deep/60 border border-line rounded-sm overflow-hidden flex flex-col hover:shadow-lg hover:-translate-y-1 transition-all duration-200">
      <button
        onClick={(e) => { e.preventDefault(); toggleWishlist(book.id); }}
        aria-label={inWishlist ? 'Remove from wishlist' : 'Save to wishlist'}
        className={`ribbon absolute top-0 right-4 w-8 h-11 flex items-start justify-center pt-2 z-10 transition-colors ${
          inWishlist ? 'bg-wine' : 'bg-ink/70 group-hover:bg-wine'
        }`}
      >
        <Heart size={14} className={inWishlist ? 'fill-paper text-paper' : 'text-paper'} />
      </button>

      <Link to={`/books/${book.id}`} className="block">
        <div className="aspect-[3/4] bg-ink/5 overflow-hidden">
          {book.imageUrl && !imageFailed ? (
            <img
              src={book.imageUrl}
              alt={book.title}
              className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
              onError={() => setImageFailed(true)}
            />
          ) : (
            <div className="w-full h-full flex items-center justify-center text-ink-faint font-display text-sm px-4 text-center">
              {book.title}
            </div>
          )}
        </div>
      </Link>

      <div className="p-4 flex flex-col gap-1 flex-1">
        <p className="text-[11px] uppercase tracking-widest text-brass font-semibold">
          {book.categoryName || 'Uncategorised'}
        </p>
        <Link to={`/books/${book.id}`}>
          <h3 className="font-display text-lg leading-snug text-ink hover:text-wine transition-colors line-clamp-2">
            {book.title}
          </h3>
        </Link>
        <p className="text-sm text-ink-soft">{book.author}</p>

        <div className="mt-auto pt-3 flex items-center justify-between">
          <span className="price-tag bg-ink text-paper font-mono text-sm px-3 py-1">
            ₹{Number(book.price).toFixed(2)}
          </span>
          <button
            onClick={() => addToCart(book.id, 1)}
            disabled={outOfStock}
            title={outOfStock ? 'Out of stock' : 'Add to cart'}
            className="p-2 rounded-full bg-ink text-paper hover:bg-wine disabled:bg-line disabled:text-ink-faint disabled:cursor-not-allowed transition-colors"
          >
            <ShoppingBag size={16} />
          </button>
        </div>
        {outOfStock && (
          <p className="text-xs text-wine font-medium mt-1">Out of stock</p>
        )}
      </div>
    </div>
  );
}
