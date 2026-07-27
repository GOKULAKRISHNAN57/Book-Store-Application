import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { BookMarked } from 'lucide-react';
import toast from 'react-hot-toast';
import { useAuth } from '../context/AuthContext';

const initialForm = { fullName: '', email: '', phoneNumber: '', password: '' };

export default function Register() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState(initialForm);
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    const result = await register(form);
    setSubmitting(false);
    if (result.success) {
      toast.success('Account created. Please sign in.');
      navigate('/login');
    } else {
      toast.error(result.message);
    }
  };

  return (
    <div className="min-h-[80vh] flex items-center justify-center px-4 py-10 animate-page-in">
      <div className="w-full max-w-sm">
        <div className="flex flex-col items-center mb-8">
          <BookMarked className="w-8 h-8 text-wine mb-2" />
          <h1 className="font-display text-3xl text-ink">Join the club</h1>
          <p className="text-ink-soft text-sm mt-1">A library card for the whole catalog.</p>
        </div>

        <form onSubmit={handleSubmit} className="bg-paper-deep/50 border border-line rounded-sm p-6 flex flex-col gap-4">
          <Field
            label="Full name"
            value={form.fullName}
            onChange={(v) => setForm({ ...form, fullName: v })}
            placeholder="Gokul Krishnan"
            required
          />
          <Field
            label="Email (Gmail only)"
            type="email"
            value={form.email}
            onChange={(v) => setForm({ ...form, email: v })}
            placeholder="you@gmail.com"
            required
          />
          <Field
            label="Phone number"
            value={form.phoneNumber}
            onChange={(v) => setForm({ ...form, phoneNumber: v.replace(/\D/g, '').slice(0, 10) })}
            placeholder="9876543210"
            required
          />
          <div>
            <Field
              label="Password"
              type="password"
              value={form.password}
              onChange={(v) => setForm({ ...form, password: v })}
              placeholder="••••••••"
              required
            />
            <p className="text-[11px] text-ink-faint mt-1">
              8–20 characters, with an uppercase letter, lowercase letter, number, and a special character.
            </p>
          </div>
          <button
            type="submit"
            disabled={submitting}
            className="mt-2 bg-wine hover:bg-wine-dark disabled:opacity-60 transition-colors text-paper font-semibold py-2.5 rounded-sm"
          >
            {submitting ? 'Creating account…' : 'Create account'}
          </button>
        </form>

        <p className="text-center text-sm text-ink-soft mt-6">
          Already a member?{' '}
          <Link to="/login" className="text-wine font-semibold hover:underline">
            Sign in
          </Link>
        </p>
      </div>
    </div>
  );
}

function Field({ label, value, onChange, type = 'text', placeholder, required }) {
  return (
    <div>
      <label className="block text-xs font-mono uppercase tracking-wide text-ink-soft mb-1">
        {label}
      </label>
      <input
        type={type}
        required={required}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder}
        className="w-full px-3 py-2.5 bg-paper border border-line rounded-sm text-ink focus:border-wine outline-none transition-colors"
      />
    </div>
  );
}
