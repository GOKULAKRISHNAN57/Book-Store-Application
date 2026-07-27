import { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { BookMarked } from 'lucide-react';
import toast from 'react-hot-toast';
import { useAuth } from '../context/AuthContext';

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [form, setForm] = useState({ email: '', password: '' });
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    const result = await login(form.email, form.password);
    setSubmitting(false);
    if (result.success) {
      toast.success('Welcome back.');
      const redirectTo = location.state?.from?.pathname || '/';
      navigate(redirectTo, { replace: true });
    } else {
      toast.error(result.message);
    }
  };

  return (
    <div className="min-h-[80vh] flex items-center justify-center px-4 animate-page-in">
      <div className="w-full max-w-sm">
        <div className="flex flex-col items-center mb-8">
          <BookMarked className="w-8 h-8 text-wine mb-2" />
          <h1 className="font-display text-3xl text-ink">Welcome back</h1>
          <p className="text-ink-soft text-sm mt-1">Sign in to your reading list.</p>
        </div>

        <form onSubmit={handleSubmit} className="bg-paper-deep/50 border border-line rounded-sm p-6 flex flex-col gap-4">
          <div>
            <label htmlFor="email" className="block text-xs font-mono uppercase tracking-wide text-ink-soft mb-1">
              Email
            </label>
            <input
              id="email"
              type="email"
              required
              value={form.email}
              onChange={(e) => setForm({ ...form, email: e.target.value })}
              className="w-full px-3 py-2.5 bg-paper border border-line rounded-sm text-ink focus:border-wine outline-none transition-colors"
              placeholder="you@gmail.com"
            />
          </div>
          <div>
            <label htmlFor="password" className="block text-xs font-mono uppercase tracking-wide text-ink-soft mb-1">
              Password
            </label>
            <input
              id="password"
              type="password"
              required
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })}
              className="w-full px-3 py-2.5 bg-paper border border-line rounded-sm text-ink focus:border-wine outline-none transition-colors"
              placeholder="••••••••"
            />
          </div>
          <button
            type="submit"
            disabled={submitting}
            className="mt-2 bg-wine hover:bg-wine-dark disabled:opacity-60 transition-colors text-paper font-semibold py-2.5 rounded-sm"
          >
            {submitting ? 'Signing in…' : 'Sign in'}
          </button>
        </form>

        <p className="text-center text-sm text-ink-soft mt-6">
          New here?{' '}
          <Link to="/register" className="text-wine font-semibold hover:underline">
            Create an account
          </Link>
        </p>
      </div>
    </div>
  );
}
