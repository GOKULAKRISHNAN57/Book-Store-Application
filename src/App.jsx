import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './context/AuthContext';
import { CartProvider } from './context/CartContext';
import { WishlistProvider } from './context/WishlistContext';
import { ProtectedRoute, AdminRoute } from './components/RouteGuards';
import Navbar from './components/Navbar';
import ErrorBoundary from './components/ErrorBoundary';

import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import BookDetail from './pages/BookDetail';
import Cart from './pages/Cart';
import Wishlist from './pages/Wishlist';
import Profile from './pages/Profile';
import Checkout from './pages/Checkout';
import MyOrders from './pages/MyOrders';
import AdminLayout from './pages/admin/AdminLayout';
import AdminDashboard from './pages/admin/AdminDashboard';
import AdminProducts from './pages/admin/AdminProducts';
import AdminCategories from './pages/admin/AdminCategories';
import AdminOrders from './pages/admin/AdminOrders';
import AdminUsers from './pages/admin/AdminUsers';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <CartProvider>
          <WishlistProvider>
            <div className="min-h-screen flex flex-col bg-paper">
              <Navbar />
              <main className="flex-1">
                <ErrorBoundary>
                <Routes>
                  <Route path="/" element={<Home />} />
                  <Route path="/books/:id" element={<BookDetail />} />
                  <Route path="/login" element={<Login />} />
                  <Route path="/register" element={<Register />} />

                  <Route path="/cart" element={<ProtectedRoute><Cart /></ProtectedRoute>} />
                  <Route path="/checkout" element={<ProtectedRoute><Checkout /></ProtectedRoute>} />
                  <Route path="/orders" element={<ProtectedRoute><MyOrders /></ProtectedRoute>} />
                  <Route path="/wishlist" element={<ProtectedRoute><Wishlist /></ProtectedRoute>} />
                  <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />

                  <Route path="/admin" element={<AdminRoute><AdminLayout /></AdminRoute>}>
                    <Route index element={<Navigate to="/admin/dashboard" replace />} />
                    <Route path="dashboard" element={<AdminDashboard />} />
                    <Route path="products" element={<AdminProducts />} />
                    <Route path="categories" element={<AdminCategories />} />
                    <Route path="orders" element={<AdminOrders />} />
                    <Route path="users" element={<AdminUsers />} />
                  </Route>

                  <Route path="*" element={<Navigate to="/" replace />} />
                </Routes>
                </ErrorBoundary>
              </main>
              <footer className="border-t border-line bg-paper-deep/50 py-6">
                <p className="text-center text-xs text-ink-faint font-mono">
                  Inkwell &amp; Co. — a bookstore built for learning, not for profit.
                </p>
              </footer>
            </div>
            <Toaster
              position="top-right"
              toastOptions={{
                style: {
                  background: '#1C2541',
                  color: '#FAF3E6',
                  fontSize: '14px',
                },
              }}
            />
          </WishlistProvider>
        </CartProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
