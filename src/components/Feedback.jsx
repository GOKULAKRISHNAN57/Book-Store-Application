import { BookX, Loader2 } from 'lucide-react';

export function Loader({ label = 'Loading…' }) {
  return (
    <div className="flex flex-col items-center justify-center gap-3 py-24 text-ink-faint">
      <Loader2 className="w-6 h-6 animate-spin" />
      <p className="font-mono text-xs tracking-wide">{label}</p>
    </div>
  );
}

export function EmptyState({ title, message, action }) {
  return (
    <div className="flex flex-col items-center justify-center gap-3 py-24 text-center px-4">
      <BookX className="w-10 h-10 text-ink-faint" />
      <h3 className="font-display text-xl text-ink">{title}</h3>
      {message && <p className="text-ink-soft max-w-sm">{message}</p>}
      {action}
    </div>
  );
}
