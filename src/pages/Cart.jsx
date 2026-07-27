import { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Trash2, ShoppingBag } from 'lucide-react';
import { useCart } from '../context/CartContext';
import { Loader, EmptyState } from '../components/Feedback';

export default function Cart() {
  const { cart, loading, refreshCart, updateQuantity, removeFromCart, clearCart } = useCart();
  const navigate = useNavigate();

  useEffect(() => {
    refreshCart();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  if (loading) return <Loader label="Fetching your cart…" />;

  if (!cart || !cart.items || cart.items.length === 0) {
    return (
      <EmptyState
        title="Your cart is empty"
        message="Find something worth reading and it'll show up here."
        action={
          <Link to="/" className="bg-wine hover:bg-wine-dark text-paper font-semibold px-5 py-2.5 rounded-sm transition-colors">
            Browse the catalog
          </Link>
        }
      />
    );
  }

  const handleCheckout = () => {
    navigate('/checkout');
  };

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 py-10 animate-page-in">
      <div className="flex items-center justify-between mb-8">
        <h1 className="font-display text-3xl text-ink">Your cart</h1>
        <button onClick={clearCart} className="text-sm text-ink-faint hover:text-wine">
          Clear cart
        </button>
      </div>

      <ul className="flex flex-col gap-4">
        {cart.items.map((item) => (
          <li key={item.productId} className="flex gap-4 border border-line rounded-sm p-4 bg-paper-deep/40">
            <div className="w-16 h-20 bg-ink/5 rounded-sm overflow-hidden shrink-0">
              {item.imageUrl && (
                <img src={item.imageUrl} alt={item.productTitle} className="w-full h-full object-cover" />
              )}
            </div>
            <div className="flex-1 min-w-0">
              <p className="font-display text-lg text-ink truncate">{item.productTitle}</p>
              <p className="text-sm text-ink-soft">{item.productAuthor}</p>
              <p className="font-mono text-sm text-ink-soft mt-1">₹{Number(item.unitPrice).toFixed(2)} each</p>
            </div>
            <div className="flex flex-col items-end justify-between">
              <button
                onClick={() => removeFromCart(item.productId)}
                className="text-ink-faint hover:text-wine"
                aria-label="Remove item"
              >
                <Trash2 size={16} />
              </button>
              <div className="flex items-center border border-line rounded-sm">
                <button
                  onClick={() => updateQuantity(item.productId, Math.max(1, item.quantity - 1))}
                  className="px-2.5 py-1 text-ink-soft hover:text-wine"
                >
                  −
                </button>
                <span className="px-2 font-mono text-sm">{item.quantity}</span>
                <button
                  onClick={() => updateQuantity(item.productId, item.quantity + 1)}
                  className="px-2.5 py-1 text-ink-soft hover:text-wine"
                >
                  +
                </button>
              </div>
              <span className="font-mono text-sm text-ink font-semibold">
                ₹{Number(item.totalPrice).toFixed(2)}
              </span>
            </div>
          </li>
        ))}
      </ul>

      <div className="ledger-rule my-8" />

      <div className="flex items-center justify-between">
        <div>
          <p className="text-xs uppercase tracking-widest text-ink-faint">Total</p>
          <p className="font-display text-3xl text-ink">₹{Number(cart.totalAmount).toFixed(2)}</p>
        </div>
        <button
          onClick={handleCheckout}
          className="flex items-center gap-2 bg-wine hover:bg-wine-dark transition-colors text-paper font-semibold px-6 py-3 rounded-sm"
        >
          <ShoppingBag size={18} /> Checkout
        </button>
      </div>
    </div>
  );
}
