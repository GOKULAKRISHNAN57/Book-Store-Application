import { useEffect, useState, useCallback } from 'react';
import { useParams, Link } from 'react-router-dom';
import { Heart, ShoppingBag, ArrowLeft, Trash2 } from 'lucide-react';
import toast from 'react-hot-toast';
import api, { extractErrorMessage } from '../lib/api';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import { useWishlist } from '../context/WishlistContext';
import StarRating from '../components/StarRating';
import { Loader, EmptyState } from '../components/Feedback';

export default function BookDetail() {
  const { id } = useParams();
  const { user } = useAuth();
  const { addToCart } = useCart();
  const { isInWishlist, toggleWishlist } = useWishlist();

  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);
  const [quantity, setQuantity] = useState(1);

  const [reviews, setReviews] = useState([]);
  const [summary, setSummary] = useState({ averageRating: 0, totalReviews: 0 });
  const [reviewsLoading, setReviewsLoading] = useState(true);

  const [reviewForm, setReviewForm] = useState({ rating: 5, comment: '' });
  const [submittingReview, setSubmittingReview] = useState(false);

  const loadBook = useCallback(async () => {
    setLoading(true);
    try {
      const res = await api.get(`/products/${id}`);
      setBook(res.data.data);
    } catch {
      setBook(null);
    } finally {
      setLoading(false);
    }
  }, [id]);

  const loadReviews = useCallback(async () => {
    setReviewsLoading(true);
    try {
      const [reviewsRes, summaryRes] = await Promise.all([
        api.get(`/feedback/product/${id}`),
        api.get(`/feedback/product/${id}/summary`),
      ]);
      setReviews(reviewsRes.data.data || []);
      setSummary(summaryRes.data.data || { averageRating: 0, totalReviews: 0 });
    } catch {
      setReviews([]);
    } finally {
      setReviewsLoading(false);
    }
  }, [id]);

  useEffect(() => {
    loadBook();
    loadReviews();
  }, [loadBook, loadReviews]);

  const alreadyReviewed = user && reviews.some((r) => r.userId === user.id);

  const handleReviewSubmit = async (e) => {
    e.preventDefault();
    if (!user) {
      toast.error('Please log in to leave a review.');
      return;
    }
    setSubmittingReview(true);
    try {
      await api.post('/feedback', { productId: Number(id), ...reviewForm });
      toast.success('Review posted.');
      setReviewForm({ rating: 5, comment: '' });
      loadReviews();
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not post review.'));
    } finally {
      setSubmittingReview(false);
    }
  };

  const handleDeleteReview = async (feedbackId) => {
    try {
      await api.delete(`/feedback/${feedbackId}`);
      toast.success('Review deleted.');
      loadReviews();
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not delete review.'));
    }
  };

  if (loading) return <Loader label="Opening the book…" />;
  if (!book) {
    return (
      <EmptyState
        title="Book not found"
        message="This title may have been removed from the catalog."
        action={<Link to="/" className="text-wine font-semibold hover:underline">Back to browsing</Link>}
      />
    );
  }

  const outOfStock = book.stockQuantity <= 0;
  const inWishlist = isInWishlist(book.id);

  return (
    <div className="max-w-6xl mx-auto px-4 sm:px-6 py-10 animate-page-in">
      <Link to="/" className="inline-flex items-center gap-1 text-sm text-ink-soft hover:text-wine mb-8">
        <ArrowLeft size={16} /> Back to catalog
      </Link>

      <div className="grid md:grid-cols-[320px_1fr] gap-10">
        {/* Cover */}
        <div>
          <div className="aspect-[3/4] bg-paper-deep border border-line rounded-sm overflow-hidden">
            {book.imageUrl ? (
              <img src={book.imageUrl} alt={book.title} className="w-full h-full object-cover" />
            ) : (
              <div className="w-full h-full flex items-center justify-center font-display text-ink-faint px-4 text-center">
                {book.title}
              </div>
            )}
          </div>
        </div>

        {/* Details */}
        <div>
          <p className="text-xs uppercase tracking-widest text-brass font-semibold mb-2">
            {book.categoryName || 'Uncategorised'}
          </p>
          <h1 className="font-display text-4xl text-ink leading-tight">{book.title}</h1>
          <p className="text-ink-soft text-lg mt-1">by {book.author}</p>

          <div className="mt-3">
            <StarRating rating={summary.averageRating} count={summary.totalReviews} size={18} />
          </div>

          <p className="mt-6 text-ink-soft leading-relaxed max-w-xl">{book.description}</p>

          <div className="ledger-rule my-6" />

          <div className="flex flex-wrap items-center gap-6">
            <span className="price-tag bg-ink text-paper font-mono text-lg px-4 py-2">
              ₹{Number(book.price).toFixed(2)}
            </span>
            <span className={`text-sm font-medium ${outOfStock ? 'text-wine' : 'text-sage'}`}>
              {outOfStock ? 'Out of stock' : `${book.stockQuantity} in stock`}
            </span>
            {book.isbn && (
              <span className="text-xs font-mono text-ink-faint">ISBN {book.isbn}</span>
            )}
          </div>

          <div className="flex items-center gap-3 mt-6">
            <div className="flex items-center border border-line rounded-sm">
              <button
                onClick={() => setQuantity((q) => Math.max(1, q - 1))}
                className="px-3 py-2 text-ink-soft hover:text-wine"
                aria-label="Decrease quantity"
              >
                −
              </button>
              <span className="px-3 font-mono text-sm">{quantity}</span>
              <button
                onClick={() => setQuantity((q) => Math.min(book.stockQuantity || 1, q + 1))}
                className="px-3 py-2 text-ink-soft hover:text-wine"
                aria-label="Increase quantity"
              >
                +
              </button>
            </div>

            <button
              onClick={() => addToCart(book.id, quantity)}
              disabled={outOfStock}
              className="flex items-center gap-2 bg-wine hover:bg-wine-dark disabled:opacity-50 transition-colors text-paper font-semibold px-5 py-2.5 rounded-sm"
            >
              <ShoppingBag size={18} /> Add to cart
            </button>

            <button
              onClick={() => toggleWishlist(book.id)}
              className={`p-2.5 rounded-sm border transition-colors ${
                inWishlist ? 'bg-wine border-wine text-paper' : 'border-line text-ink-soft hover:border-wine hover:text-wine'
              }`}
              aria-label={inWishlist ? 'Remove from wishlist' : 'Save to wishlist'}
            >
              <Heart size={18} className={inWishlist ? 'fill-current' : ''} />
            </button>
          </div>
        </div>
      </div>

      {/* Reviews */}
      <section className="mt-16 max-w-2xl">
        <h2 className="font-display text-2xl text-ink mb-6">Reader reviews</h2>

        {user && !alreadyReviewed && (
          <form onSubmit={handleReviewSubmit} className="bg-paper-deep/50 border border-line rounded-sm p-5 mb-8">
            <p className="text-sm font-semibold text-ink mb-2">Leave a review</p>
            <div className="flex items-center gap-1 mb-3">
              {[1, 2, 3, 4, 5].map((n) => (
                <button
                  key={n}
                  type="button"
                  onClick={() => setReviewForm({ ...reviewForm, rating: n })}
                  aria-label={`Rate ${n} stars`}
                >
                  <StarRating rating={n <= reviewForm.rating ? 1 : 0} showValue={false} size={22} />
                </button>
              ))}
            </div>
            <textarea
              value={reviewForm.comment}
              onChange={(e) => setReviewForm({ ...reviewForm, comment: e.target.value })}
              placeholder="What did you think of this book?"
              rows={3}
              maxLength={1000}
              className="w-full px-3 py-2 bg-paper border border-line rounded-sm text-ink text-sm focus:border-wine outline-none resize-none"
            />
            <button
              type="submit"
              disabled={submittingReview}
              className="mt-3 bg-ink hover:bg-wine disabled:opacity-60 transition-colors text-paper text-sm font-semibold px-4 py-2 rounded-sm"
            >
              {submittingReview ? 'Posting…' : 'Post review'}
            </button>
          </form>
        )}

        {reviewsLoading ? (
          <Loader label="Loading reviews…" />
        ) : reviews.length === 0 ? (
          <p className="text-ink-soft text-sm">No reviews yet. Be the first to share your thoughts.</p>
        ) : (
          <ul className="flex flex-col gap-5">
            {reviews.map((r) => (
              <li key={r.id} className="border-b border-line pb-5">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-semibold text-ink text-sm">{r.userFullName}</p>
                    <StarRating rating={r.rating} showValue={false} size={14} />
                  </div>
                  {user && user.id === r.userId && (
                    <button
                      onClick={() => handleDeleteReview(r.id)}
                      className="text-ink-faint hover:text-wine"
                      aria-label="Delete your review"
                    >
                      <Trash2 size={16} />
                    </button>
                  )}
                </div>
                {r.comment && <p className="text-ink-soft text-sm mt-2">{r.comment}</p>}
              </li>
            ))}
          </ul>
        )}
      </section>
    </div>
  );
}
