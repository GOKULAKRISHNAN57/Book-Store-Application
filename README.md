# Inkwell & Co. — Bookstore Frontend

A React (Vite) + Tailwind CSS v4 frontend for the Book Store Application Spring Boot backend.

## What's included

**Customer flow**
- Browse books (search, filter by category)
- Book detail page with reviews & average rating
- Add to cart, update quantity, remove, clear
- Wishlist (save/remove, add to cart from wishlist)
- Register / Login (JWT stored in localStorage, auto-attached to requests)
- **Checkout** — create customer profile, add/select delivery address, place a real order
- **My Orders** — view order history, cancel pending/confirmed orders
- Profile (view/update details, change password)

**Admin flow** (visible only to users with `ROLE_ADMIN`)
- Dashboard — totals for users, products, categories, orders, reviews, revenue, plus order-status breakdown
- Product management (create, edit, deactivate)
- Category management (create, edit, deactivate)
- Orders — view all orders, update order status
- Users — view all registered users and their roles

## Setup

### 1. Install dependencies
```bash
npm install
```

### 2. Point it at your backend
Edit `.env`:
```
VITE_API_BASE_URL=http://localhost:8081/api
```

### 3. Apply the SecurityConfig fix
This frontend assumes your backend allows public GET access to
`/api/products/**`, `/api/categories/**`, `/api/feedback/product/**`, and
restricts `/api/admin/**` plus product/category writes and order-status
updates to `ROLE_ADMIN`. Use the `SecurityConfig.java` provided alongside
this zip — it's a drop-in replacement for that file.

### 4. Run the backend, then the frontend
```bash
npm run dev
```
Opens at `http://localhost:5173`.

### 5. Build for production
```bash
npm run build
```

## Getting an admin account

Every registration creates a `ROLE_USER` account by default. To test the
admin flow, register normally, then manually promote that user in MySQL:
```sql
UPDATE users SET role = 'ROLE_ADMIN' WHERE email = 'you@gmail.com';
```
Log out and back in afterward so the JWT reflects the new role.

## Checkout flow, step by step

1. Add books to your cart.
2. Go to Cart → Checkout.
3. First time only: fill in first/last name + phone (creates your customer profile).
4. Add a delivery address (or pick a saved one).
5. Place order — stock is deducted, cart is cleared, order appears under "My Orders".

## Folder structure
```
src/
├── lib/api.js              Axios instance + JWT interceptor
├── context/                Auth, Cart, Wishlist global state
├── components/             Navbar, BookCard, StarRating, route guards
├── pages/                  Home, BookDetail, Login, Register, Cart, Checkout,
│                            MyOrders, Wishlist, Profile
└── pages/admin/             AdminLayout, AdminDashboard, AdminProducts,
                             AdminCategories, AdminOrders, AdminUsers
```
