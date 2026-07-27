import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import toast from 'react-hot-toast';
import api, { extractErrorMessage } from '../lib/api';
import { Loader, EmptyState } from '../components/Feedback';

const statusColors = {
  PENDING: 'bg-brass/20 text-brass',
  CONFIRMED: 'bg-sage/20 text-sage',
  PROCESSING: 'bg-sage/20 text-sage',
  SHIPPED: 'bg-ink/10 text-ink-soft',
  DELIVERED: 'bg-sage/30 text-sage',
  CANCELLED: 'bg-wine/10 text-wine',
};

export default function MyOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  const load = async () => {
    setLoading(true);
    try {
      const res = await api.get('/orders');
      setOrders(res.data.data || []);
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not load orders.'));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const handleCancel = async (orderId) => {
    if (!confirm('Cancel this order?')) return;
    try {
      await api.put(`/orders/${orderId}/cancel`);
      toast.success('Order cancelled.');
      load();
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not cancel order.'));
    }
  };

  if (loading) return <Loader label="Fetching your orders…" />;

  if (orders.length === 0) {
    return (
      <EmptyState
        title="No orders yet"
        message="Once you check out, your orders will show up here."
        action={
          <Link to="/" className="bg-wine hover:bg-wine-dark text-paper font-semibold px-5 py-2.5 rounded-sm transition-colors">
            Browse the catalog
          </Link>
        }
      />
    );
  }

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 py-10 animate-page-in">
      <h1 className="font-display text-3xl text-ink mb-8">Your orders</h1>
      <ul className="flex flex-col gap-4">
        {orders.map((order) => (
          <li key={order.orderId} className="border border-line rounded-sm p-5 bg-paper-deep/40">
            <div className="flex items-center justify-between flex-wrap gap-2">
              <div>
                <p className="font-mono text-xs text-ink-faint">Order #{order.orderId}</p>
                <p className="text-sm text-ink-soft">
                  {new Date(order.createdAt).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' })}
                </p>
              </div>
              <span className={`text-xs font-semibold px-3 py-1 rounded-full ${statusColors[order.orderStatus] || 'bg-line text-ink-soft'}`}>
                {order.orderStatus}
              </span>
            </div>

            <ul className="mt-3 flex flex-col gap-1">
              {order.orderItems?.map((item) => (
                <li key={item.orderItemId} className="text-sm text-ink-soft flex justify-between">
                  <span>{item.productName} × {item.quantity}</span>
                  <span className="font-mono">₹{Number(item.subtotal).toFixed(2)}</span>
                </li>
              ))}
            </ul>

            <div className="ledger-rule my-3" />

            <div className="flex items-center justify-between">
              <span className="font-display text-xl text-ink">₹{Number(order.totalAmount).toFixed(2)}</span>
              {(order.orderStatus === 'PENDING' || order.orderStatus === 'CONFIRMED') && (
                <button
                  onClick={() => handleCancel(order.orderId)}
                  className="text-sm text-wine font-semibold hover:underline"
                >
                  Cancel order
                </button>
              )}
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
