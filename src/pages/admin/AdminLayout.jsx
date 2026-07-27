import { NavLink, Outlet } from 'react-router-dom';
import { Package, Tag, LayoutDashboard, ShoppingCart, Users } from 'lucide-react';

export default function AdminLayout() {
  const linkClass = ({ isActive }) =>
    `flex items-center gap-2 px-3 py-2 rounded-sm text-sm font-medium transition-colors ${
      isActive ? 'bg-ink text-paper' : 'text-ink-soft hover:bg-paper-deep'
    }`;

  return (
    <div className="max-w-6xl mx-auto px-4 sm:px-6 py-10 animate-page-in">
      <div className="flex items-center gap-2 mb-8">
        <LayoutDashboard className="text-wine" size={22} />
        <h1 className="font-display text-3xl text-ink">Admin desk</h1>
      </div>

      <div className="grid md:grid-cols-[200px_1fr] gap-8">
        <nav className="flex md:flex-col gap-2">
          <NavLink to="/admin/dashboard" className={linkClass}>
            <LayoutDashboard size={16} /> Dashboard
          </NavLink>
          <NavLink to="/admin/products" className={linkClass}>
            <Package size={16} /> Products
          </NavLink>
          <NavLink to="/admin/categories" className={linkClass}>
            <Tag size={16} /> Categories
          </NavLink>
          <NavLink to="/admin/orders" className={linkClass}>
            <ShoppingCart size={16} /> Orders
          </NavLink>
          <NavLink to="/admin/users" className={linkClass}>
            <Users size={16} /> Users
          </NavLink>
        </nav>
        <div>
          <Outlet />
        </div>
      </div>
    </div>
  );
}
