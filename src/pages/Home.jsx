import { useEffect, useState, useCallback } from 'react';
import { Search, BookOpen } from 'lucide-react';
import api from '../lib/api';
import BookCard from '../components/BookCard';
import { Loader, EmptyState } from '../components/Feedback';

export default function Home() {
  const [books, setBooks] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [query, setQuery] = useState('');
  const [activeCategory, setActiveCategory] = useState(null);

  useEffect(() => {
    api.get('/categories').then((res) => {
      const data = res.data.data;
      setCategories(Array.isArray(data) ? data : []);
    }).catch(() => {});
  }, []);

  const loadBooks = useCallback(async (search, categoryId) => {
    setLoading(true);
    try {
      let res;
      if (categoryId) {
        // Category endpoint still returns a plain array — unchanged
        res = await api.get(`/products/category/${categoryId}`);
        const data = res.data.data;
        setBooks(Array.isArray(data) ? data : []);
      } else {
        // Base + search now share one paginated endpoint (Page<ProductResponse>)
        res = await api.get('/products', {
          params: {
            keyword: search || undefined,
            active: true,
            size: 100,
            sortBy: 'title',
            direction: 'asc',
          },
        });
        const content = res.data.data?.content;
        setBooks(Array.isArray(content) ? content : []);
      }
    } catch {
      setBooks([]);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadBooks(query, activeCategory);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    setActiveCategory(null);
    loadBooks(query, null);
  };

  const handleCategoryClick = (id) => {
    const next = activeCategory === id ? null : id;
    setActiveCategory(next);
    setQuery('');
    loadBooks('', next);
  };

  return (
    <div className="animate-page-in">
      {/* Hero */}
      <section className="bg-ink text-paper">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 py-16 md:py-20 flex flex-col md:flex-row items-start md:items-center gap-8 justify-between">
          <div className="max-w-xl">
            <p className="font-mono text-xs uppercase tracking-[0.2em] text-brass-light mb-3">
              Est. reading, one spine at a time
            </p>
            <h1 className="font-display text-4xl md:text-5xl leading-tight">
              A shelf worth <span className="text-brass-light italic">getting lost in.</span>
            </h1>
            <p className="mt-4 text-ink-faint text-lg">
              Browse the full catalog, save what calls to you, and check out
              whenever you're ready. No membership fees, just good books.
            </p>
          </div>
          <form onSubmit={handleSearch} className="w-full md:w-80">
            <label htmlFor="search" className="sr-only">Search books</label>
            <div className="flex items-center bg-paper rounded-sm overflow-hidden border-2 border-brass">
              <Search className="w-4 h-4 ml-3 text-ink-soft" />
              <input
                id="search"
                type="text"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Search title or author…"
                className="w-full px-3 py-3 text-ink bg-transparent outline-none text-sm"
              />
              <button
                type="submit"
                className="bg-wine hover:bg-wine-dark transition-colors px-4 py-3 text-sm font-semibold text-paper"
              >
                Search
              </button>
            </div>
          </form>
        </div>
      </section>

      {/* Category filter rail */}
      {categories.length > 0 && (
        <div className="border-b border-line bg-paper-deep">
          <div className="max-w-6xl mx-auto px-4 sm:px-6 py-3 flex gap-2 overflow-x-auto">
            <button
              onClick={() => handleCategoryClick(null)}
              className={`shrink-0 text-xs font-mono uppercase tracking-wide px-3 py-1.5 rounded-full border transition-colors ${
                activeCategory === null
                  ? 'bg-ink text-paper border-ink'
                  : 'border-line text-ink-soft hover:border-brass'
              }`}
            >
              All
            </button>
            {categories.map((c) => (
              <button
                key={c.id}
                onClick={() => handleCategoryClick(c.id)}
                className={`shrink-0 text-xs font-mono uppercase tracking-wide px-3 py-1.5 rounded-full border transition-colors ${
                  activeCategory === c.id
                    ? 'bg-ink text-paper border-ink'
                    : 'border-line text-ink-soft hover:border-brass'
                }`}
              >
                {c.name}
              </button>
            ))}
          </div>
        </div>
      )}

      {/* Book grid */}
      <section className="max-w-6xl mx-auto px-4 sm:px-6 py-10">
        <div className="ledger-rule mb-8" />
        {loading ? (
          <Loader label="Fetching the catalog…" />
        ) : books.length === 0 ? (
          <EmptyState
            icon={BookOpen}
            title="No books found"
            message="Try a different search term or browse another category."
          />
        ) : (
          <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-5 md:gap-6">
            {books.map((book) => (
              <BookCard key={book.id} book={book} />
            ))}
          </div>
        )}
      </section>
    </div>
  );
}
