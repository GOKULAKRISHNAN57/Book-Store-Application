import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import api, { extractErrorMessage } from '../../lib/api';
import { Loader, EmptyState } from '../../components/Feedback';

export default function AdminUsers() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/admin/users')
      .then((res) => setUsers(res.data.data || []))
      .catch((error) => toast.error(extractErrorMessage(error, 'Could not load users.')))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <Loader label="Loading users…" />;
  if (users.length === 0) return <EmptyState title="No users found" />;

  return (
    <div>
      <h2 className="font-display text-xl text-ink mb-6">Users ({users.length})</h2>
      <div className="border border-line rounded-sm overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-ink text-paper text-left">
            <tr>
              <th className="px-4 py-2 font-medium">Name</th>
              <th className="px-4 py-2 font-medium">Email</th>
              <th className="px-4 py-2 font-medium">Role</th>
              <th className="px-4 py-2 font-medium">Status</th>
            </tr>
          </thead>
          <tbody>
            {users.map((u) => (
              <tr key={u.userId} className="border-t border-line hover:bg-paper-deep/40">
                <td className="px-4 py-2 text-ink font-medium">{u.name}</td>
                <td className="px-4 py-2 text-ink-soft">{u.email}</td>
                <td className="px-4 py-2">
                  <span className="text-xs font-mono px-2 py-0.5 rounded-full bg-paper-deep border border-line">
                    {u.role?.replace('ROLE_', '')}
                  </span>
                </td>
                <td className="px-4 py-2">
                  <span className={`text-xs font-medium px-2 py-0.5 rounded-full ${u.active ? 'bg-sage/20 text-sage' : 'bg-wine/10 text-wine'}`}>
                    {u.active ? 'Active' : 'Inactive'}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
