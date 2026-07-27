import { Star } from 'lucide-react';

export default function StarRating({ rating = 0, size = 16, showValue = true, count }) {
  const rounded = Math.round(rating);
  return (
    <div className="flex items-center gap-1">
      <div className="flex">
        {[1, 2, 3, 4, 5].map((n) => (
          <Star
            key={n}
            size={size}
            className={n <= rounded ? 'fill-brass text-brass' : 'fill-none text-line'}
          />
        ))}
      </div>
      {showValue && rating > 0 && (
        <span className="text-xs font-mono text-ink-soft">
          {rating.toFixed(1)}{count !== undefined ? ` (${count})` : ''}
        </span>
      )}
    </div>
  );
}
