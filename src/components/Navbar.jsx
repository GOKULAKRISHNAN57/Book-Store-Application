import { Link, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { BookMarked, ShoppingBag, Heart, User, LogOut, Menu, X, LayoutDashboard, Package } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';

export default function Navbar() {
  const { user, logout, isAdmin } = useAuth();
  const { itemCount } = useCart();
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/');
    setOpen(false);
  };

  return (
    <header className="sticky top-0 z-40 bg-ink text-paper border-b-4 border-brass">
      <div className="max-w-6xl mx-auto px-4 sm:px-6">
        <div className="flex items-center justify-between h-16">
          <Link to="/" className="flex items-center gap-2 group" onClick={() => setOpen(false)}>
            <BookMarked className="w-6 h-6 text-brass-light group-hover:rotate-[-6deg] transition-transform" />
            <span className="font-display text-2xl tracking-tight">
              Inkwell &amp; Co.
            </span>
          </Link>

          <nav className="hidden md:flex items-center gap-8 font-medium text-sm tracking-wide">
            <Link to="/" className="hover:text-brass-light transition-colors">
              Browse
            </Link>
            {isAdmin && (
              <Link to="/admin" className="hover:text-brass-light transition-colors flex items-center gap-1">
                <LayoutDashboard className="w-4 h-4" /> Admin
              </Link>
            )}
          </nav>

          <div className="hidden md:flex items-center gap-5">
            {user ? (
              <>
                <Link to="/orders" aria-label="My orders" className="hover:text-brass-light transition-colors">
                  <Package className="w-5 h-5" />
                </Link>
                <Link to="/wishlist" aria-label="Wishlist" className="hover:text-brass-light transition-colors">
                  <Heart className="w-5 h-5" />
                </Link>
                <Link to="/cart" aria-label="Cart" className="relative hover:text-brass-light transition-colors">
                  <ShoppingBag className="w-5 h-5" />
                  {itemCount > 0 && (
                    <span className="absolute -top-2 -right-2 bg-wine text-white text-[10px] font-mono rounded-full w-4 h-4 flex items-center justify-center">
                      {itemCount}
                    </span>
                  )}
                </Link>
                <Link to="/profile" aria-label="Profile" className="hover:text-brass-light transition-colors">
                  <User className="w-5 h-5" />
                </Link>
                <button
                  onClick={handleLogout}
                  aria-label="Log out"
                  className="hover:text-wine-dark transition-colors"
                >
                  <LogOut className="w-5 h-5" />
                </button>
              </>
            ) : (
              <>
                <Link
                  to="/login"
                  className="text-sm font-medium hover:text-brass-light transition-colors"
                >
                  Sign in
                </Link>
                <Link
                  to="/register"
                  className="text-sm font-semibold bg-wine hover:bg-wine-dark transition-colors px-4 py-2 rounded-sm"
                >
                  Join the club
                </Link>
              </>
            )}
          </div>

          <button className="md:hidden" onClick={() => setOpen(!open)} aria-label="Toggle menu">
            {open ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
          </button>
        </div>
      </div>

      {open && (
        <div className="md:hidden bg-ink-soft px-4 py-4 flex flex-col gap-4 text-sm font-medium animate-page-in">
          <Link to="/" onClick={() => setOpen(false)}>Browse</Link>
          {isAdmin && <Link to="/admin" onClick={() => setOpen(false)}>Admin</Link>}
          {user ? (
            <>
              <Link to="/orders" onClick={() => setOpen(false)}>My orders</Link>
              <Link to="/wishlist" onClick={() => setOpen(false)}>Wishlist</Link>
              <Link to="/cart" onClick={() => setOpen(false)}>Cart ({itemCount})</Link>
              <Link to="/profile" onClick={() => setOpen(false)}>Profile</Link>
              <button onClick={handleLogout} className="text-left text-brass-light">
                Log out
              </button>
            </>
          ) : (
            <>
              <Link to="/login" onClick={() => setOpen(false)}>Sign in</Link>
              <Link to="/register" onClick={() => setOpen(false)}>Join the club</Link>
            </>
          )}
        </div>
      )}
    </header>
  );
}
