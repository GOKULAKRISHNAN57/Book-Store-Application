import { useEffect, useState } from 'react';
import { Pencil, Trash2, Plus, X } from 'lucide-react';
import toast from 'react-hot-toast';
import api, { extractErrorMessage } from '../../lib/api';
import { Loader, EmptyState } from '../../components/Feedback';

const emptyForm = {
  title: '', author: '', isbn: '', description: '',
  price: '', stockQuantity: '', imageUrl: '', categoryId: '',
};

export default function AdminProducts() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editingId, setEditingId] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState(emptyForm);
  const [saving, setSaving] = useState(false);

  const load = async () => {
    setLoading(true);
    try {
      const [prodRes, catRes] = await Promise.all([
        api.get('/products'),
        api.get('/categories'),
      ]);
      setProducts(prodRes.data.data || []);
      setCategories(catRes.data.data || []);
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not load products.'));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const openCreate = () => {
    setEditingId(null);
    setForm(emptyForm);
    setShowForm(true);
  };

  const openEdit = (p) => {
    setEditingId(p.id);
    setForm({
      title: p.title, author: p.author, isbn: p.isbn || '', description: p.description || '',
      price: p.price, stockQuantity: p.stockQuantity, imageUrl: p.imageUrl || '', categoryId: p.categoryId,
    });
    setShowForm(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    const payload = {
      ...form,
      price: Number(form.price),
      stockQuantity: Number(form.stockQuantity),
      categoryId: Number(form.categoryId),
    };
    try {
      if (editingId) {
        // Update endpoint doesn't accept isbn (immutable after creation)
        const { isbn, ...updatePayload } = payload;
        await api.put(`/products/${editingId}`, updatePayload);
        toast.success('Product updated.');
      } else {
        await api.post('/products', payload);
        toast.success('Product created.');
      }
      setShowForm(false);
      load();
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not save product.'));
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    if (!confirm('Deactivate this product? It will be hidden from customers.')) return;
    try {
      await api.delete(`/products/${id}`);
      toast.success('Product deactivated.');
      load();
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not delete product.'));
    }
  };

  if (loading) return <Loader label="Loading inventory…" />;

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h2 className="font-display text-xl text-ink">Products ({products.length})</h2>
        <button
          onClick={openCreate}
          className="flex items-center gap-1.5 bg-wine hover:bg-wine-dark text-paper text-sm font-semibold px-4 py-2 rounded-sm transition-colors"
        >
          <Plus size={16} /> New product
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="bg-paper-deep/60 border border-line rounded-sm p-5 mb-6 flex flex-col gap-3">
          <div className="flex items-center justify-between">
            <p className="font-semibold text-ink">{editingId ? 'Edit product' : 'New product'}</p>
            <button type="button" onClick={() => setShowForm(false)} className="text-ink-faint hover:text-wine">
              <X size={18} />
            </button>
          </div>
          <div className="grid sm:grid-cols-2 gap-3">
            <Input label="Title" value={form.title} onChange={(v) => setForm({ ...form, title: v })} required />
            <Input label="Author" value={form.author} onChange={(v) => setForm({ ...form, author: v })} required />
            {!editingId && (
              <Input label="ISBN" value={form.isbn} onChange={(v) => setForm({ ...form, isbn: v })} required />
            )}
            <Select
              label="Category"
              value={form.categoryId}
              onChange={(v) => setForm({ ...form, categoryId: v })}
              options={categories.map((c) => ({ value: c.id, label: c.name }))}
              required
            />
            <Input label="Price (₹)" type="number" step="0.01" value={form.price} onChange={(v) => setForm({ ...form, price: v })} required />
            <Input label="Stock quantity" type="number" value={form.stockQuantity} onChange={(v) => setForm({ ...form, stockQuantity: v })} required />
            <Input label="Image URL" value={form.imageUrl} onChange={(v) => setForm({ ...form, imageUrl: v })} className="sm:col-span-2" />
          </div>
          <label className="block text-xs font-mono uppercase tracking-wide text-ink-soft">Description</label>
          <textarea
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
            rows={3}
            className="w-full px-3 py-2 bg-paper border border-line rounded-sm text-ink text-sm focus:border-wine outline-none resize-none"
          />
          <button
            type="submit"
            disabled={saving}
            className="self-start mt-2 bg-ink hover:bg-wine disabled:opacity-60 transition-colors text-paper text-sm font-semibold px-5 py-2.5 rounded-sm"
          >
            {saving ? 'Saving…' : editingId ? 'Save changes' : 'Create product'}
          </button>
        </form>
      )}

      {products.length === 0 ? (
        <EmptyState title="No products yet" message="Add your first title to the catalog." />
      ) : (
        <div className="border border-line rounded-sm overflow-hidden">
          <table className="w-full text-sm">
            <thead className="bg-ink text-paper text-left">
              <tr>
                <th className="px-4 py-2 font-medium">Title</th>
                <th className="px-4 py-2 font-medium">Category</th>
                <th className="px-4 py-2 font-medium">Price</th>
                <th className="px-4 py-2 font-medium">Stock</th>
                <th className="px-4 py-2 font-medium">Status</th>
                <th className="px-4 py-2 font-medium text-right">Actions</th>
              </tr>
            </thead>
            <tbody>
              {products.map((p) => (
                <tr key={p.id} className="border-t border-line hover:bg-paper-deep/40">
                  <td className="px-4 py-2 text-ink font-medium">{p.title}</td>
                  <td className="px-4 py-2 text-ink-soft">{p.categoryName}</td>
                  <td className="px-4 py-2 font-mono text-ink-soft">₹{Number(p.price).toFixed(2)}</td>
                  <td className="px-4 py-2 font-mono text-ink-soft">{p.stockQuantity}</td>
                  <td className="px-4 py-2">
                    <span className={`text-xs font-medium px-2 py-0.5 rounded-full ${p.active ? 'bg-sage/20 text-sage' : 'bg-wine/10 text-wine'}`}>
                      {p.active ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                  <td className="px-4 py-2">
                    <div className="flex justify-end gap-3">
                      <button onClick={() => openEdit(p)} className="text-ink-soft hover:text-wine" aria-label="Edit">
                        <Pencil size={15} />
                      </button>
                      <button onClick={() => handleDelete(p.id)} className="text-ink-soft hover:text-wine" aria-label="Delete">
                        <Trash2 size={15} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

function Input({ label, value, onChange, type = 'text', required, className = '', step }) {
  return (
    <div className={className}>
      <label className="block text-xs font-mono uppercase tracking-wide text-ink-soft mb-1">{label}</label>
      <input
        type={type}
        step={step}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        required={required}
        className="w-full px-3 py-2 bg-paper border border-line rounded-sm text-ink text-sm focus:border-wine outline-none"
      />
    </div>
  );
}

function Select({ label, value, onChange, options, required }) {
  return (
    <div>
      <label className="block text-xs font-mono uppercase tracking-wide text-ink-soft mb-1">{label}</label>
      <select
        value={value}
        onChange={(e) => onChange(e.target.value)}
        required={required}
        className="w-full px-3 py-2 bg-paper border border-line rounded-sm text-ink text-sm focus:border-wine outline-none"
      >
        <option value="">Select…</option>
        {options.map((o) => (
          <option key={o.value} value={o.value}>{o.label}</option>
        ))}
      </select>
    </div>
  );
}
