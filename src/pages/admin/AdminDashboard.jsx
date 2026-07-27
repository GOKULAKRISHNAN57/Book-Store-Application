import { useEffect, useState } from 'react';
import { Users, BookOpen, Tag, ShoppingCart, MessageSquare, IndianRupee } from 'lucide-react';
import api from '../../lib/api';
import { Loader } from '../../components/Feedback';

export default function AdminDashboard() {
  const [dashboard, setDashboard] = useState(null);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([api.get('/admin/dashboard'), api.get('/admin/statistics')])
      .then(([dashRes, statsRes]) => {
        setDashboard(dashRes.data.data);
        setStats(statsRes.data.data);
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <Loader label="Crunching the numbers…" />;
  if (!dashboard) return <p className="text-ink-soft text-sm">Could not load dashboard.</p>;

  const cards = [
    { icon: Users, label: 'Total users', value: dashboard.totalUsers },
    { icon: BookOpen, label: 'Total products', value: dashboard.totalProducts },
    { icon: Tag, label: 'Categories', value: dashboard.totalCategories },
    { icon: ShoppingCart, label: 'Total orders', value: dashboard.totalOrders },
    { icon: MessageSquare, label: 'Reviews', value: dashboard.totalFeedbacks },
    { icon: IndianRupee, label: 'Total revenue', value: `₹${Number(dashboard.totalRevenue || 0).toFixed(2)}` },
  ];

  const orderStatusRows = stats ? [
    ['Pending', stats.pendingOrders],
    ['Confirmed', stats.confirmedOrders],
    ['Processing', stats.processingOrders],
    ['Shipped', stats.shippedOrders],
    ['Delivered', stats.deliveredOrders],
    ['Cancelled', stats.cancelledOrders],
  ] : [];

  return (
    <div>
      <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-4 mb-8">
        {cards.map(({ icon: Icon, label, value }) => (
          <div key={label} className="border border-line rounded-sm p-4 bg-paper-deep/40 flex items-center gap-3">
            <div className="w-10 h-10 rounded-sm bg-ink text-paper flex items-center justify-center shrink-0">
              <Icon size={18} />
            </div>
            <div>
              <p className="text-xs text-ink-faint uppercase tracking-wide font-mono">{label}</p>
              <p className="font-display text-xl text-ink">{value ?? 0}</p>
            </div>
          </div>
        ))}
      </div>

      {stats && (
        <div className="border border-line rounded-sm p-5 bg-paper-deep/40">
          <h3 className="font-display text-lg text-ink mb-3">Orders by status</h3>
          <ul className="grid sm:grid-cols-2 gap-2">
            {orderStatusRows.map(([label, count]) => (
              <li key={label} className="flex justify-between text-sm border-b border-line py-1.5">
                <span className="text-ink-soft">{label}</span>
                <span className="font-mono text-ink">{count ?? 0}</span>
              </li>
            ))}
          </ul>
          {stats.averageRating != null && (
            <p className="text-sm text-ink-soft mt-3">
              Average product rating across the catalog: <span className="font-mono text-ink">{Number(stats.averageRating).toFixed(1)} / 5</span>
            </p>
          )}
        </div>
      )}
    </div>
  );
}
