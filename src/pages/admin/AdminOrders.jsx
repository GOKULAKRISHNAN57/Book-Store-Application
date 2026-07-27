import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import api, { extractErrorMessage } from '../../lib/api';
import { Loader, EmptyState } from '../../components/Feedback';

const STATUSES = ['PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED'];

export default function AdminOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [updatingId, setUpdatingId] = useState(null);

  const load = async () => {
    setLoading(true);
    try {
      const res = await api.get('/admin/orders');
      setOrders(res.data.data || []);
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not load orders.'));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const handleStatusChange = async (orderId, newStatus) => {
    setUpdatingId(orderId);
    try {
      await api.put(`/orders/${orderId}/status`, { orderStatus: newStatus });
      toast.success('Order status updated.');
      load();
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not update status.'));
    } finally {
      setUpdatingId(null);
    }
  };

  if (loading) return <Loader label="Loading orders…" />;
  if (orders.length === 0) return <EmptyState title="No orders yet" />;

  return (
    <div>
      <h2 className="font-display text-xl text-ink mb-6">All orders ({orders.length})</h2>
      <div className="border border-line rounded-sm overflow-hidden overflow-x-auto">
        <table className="w-full text-sm">
          <thead className="bg-ink text-paper text-left">
            <tr>
              <th className="px-4 py-2 font-medium">Order</th>
              <th className="px-4 py-2 font-medium">Customer</th>
              <th className="px-4 py-2 font-medium">Total</th>
              <th className="px-4 py-2 font-medium">Date</th>
              <th className="px-4 py-2 font-medium">Status</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((o) => (
              <tr key={o.orderId} className="border-t border-line hover:bg-paper-deep/40">
                <td className="px-4 py-2 font-mono text-ink-soft">#{o.orderId}</td>
                <td className="px-4 py-2 text-ink">{o.customerName}</td>
                <td className="px-4 py-2 font-mono text-ink-soft">₹{Number(o.totalAmount).toFixed(2)}</td>
                <td className="px-4 py-2 text-ink-soft">
                  {new Date(o.createdAt).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' })}
                </td>
                <td className="px-4 py-2">
                  <select
                    value={o.orderStatus}
                    disabled={updatingId === o.orderId}
                    onChange={(e) => handleStatusChange(o.orderId, e.target.value)}
                    className="text-xs font-medium bg-paper border border-line rounded-full px-2 py-1 disabled:opacity-50"
                  >
                    {STATUSES.map((s) => (
                      <option key={s} value={s}>{s}</option>
                    ))}
                  </select>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
