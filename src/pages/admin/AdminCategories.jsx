import { useEffect, useState } from 'react';
import { Pencil, Trash2, Plus, X } from 'lucide-react';
import toast from 'react-hot-toast';
import api, { extractErrorMessage } from '../../lib/api';
import { Loader, EmptyState } from '../../components/Feedback';

const emptyForm = { name: '', description: '' };

export default function AdminCategories() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editingId, setEditingId] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState(emptyForm);
  const [saving, setSaving] = useState(false);

  const load = async () => {
    setLoading(true);
    try {
      const res = await api.get('/categories');
      setCategories(res.data.data || []);
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not load categories.'));
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

  const openEdit = (c) => {
    setEditingId(c.id);
    setForm({ name: c.name, description: c.description || '' });
    setShowForm(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      if (editingId) {
        await api.put(`/categories/${editingId}`, form);
        toast.success('Category updated.');
      } else {
        await api.post('/categories', form);
        toast.success('Category created.');
      }
      setShowForm(false);
      load();
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not save category.'));
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    if (!confirm('Deactivate this category?')) return;
    try {
      await api.delete(`/categories/${id}`);
      toast.success('Category deactivated.');
      load();
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not delete category.'));
    }
  };

  if (loading) return <Loader label="Loading categories…" />;

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h2 className="font-display text-xl text-ink">Categories ({categories.length})</h2>
        <button
          onClick={openCreate}
          className="flex items-center gap-1.5 bg-wine hover:bg-wine-dark text-paper text-sm font-semibold px-4 py-2 rounded-sm transition-colors"
        >
          <Plus size={16} /> New category
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="bg-paper-deep/60 border border-line rounded-sm p-5 mb-6 flex flex-col gap-3">
          <div className="flex items-center justify-between">
            <p className="font-semibold text-ink">{editingId ? 'Edit category' : 'New category'}</p>
            <button type="button" onClick={() => setShowForm(false)} className="text-ink-faint hover:text-wine">
              <X size={18} />
            </button>
          </div>
          <div>
            <label className="block text-xs font-mono uppercase tracking-wide text-ink-soft mb-1">Name</label>
            <input
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
              required
              className="w-full px-3 py-2 bg-paper border border-line rounded-sm text-ink text-sm focus:border-wine outline-none"
            />
          </div>
          <div>
            <label className="block text-xs font-mono uppercase tracking-wide text-ink-soft mb-1">Description</label>
            <textarea
              value={form.description}
              onChange={(e) => setForm({ ...form, description: e.target.value })}
              rows={2}
              className="w-full px-3 py-2 bg-paper border border-line rounded-sm text-ink text-sm focus:border-wine outline-none resize-none"
            />
          </div>
          <button
            type="submit"
            disabled={saving}
            className="self-start mt-1 bg-ink hover:bg-wine disabled:opacity-60 transition-colors text-paper text-sm font-semibold px-5 py-2.5 rounded-sm"
          >
            {saving ? 'Saving…' : editingId ? 'Save changes' : 'Create category'}
          </button>
        </form>
      )}

      {categories.length === 0 ? (
        <EmptyState title="No categories yet" message="Create one so products have somewhere to live." />
      ) : (
        <ul className="flex flex-col gap-2">
          {categories.map((c) => (
            <li key={c.id} className="flex items-center justify-between border border-line rounded-sm p-3 bg-paper-deep/30">
              <div>
                <p className="font-medium text-ink">{c.name}</p>
                {c.description && <p className="text-xs text-ink-soft">{c.description}</p>}
              </div>
              <div className="flex items-center gap-3">
                <span className={`text-xs font-medium px-2 py-0.5 rounded-full ${c.active ? 'bg-sage/20 text-sage' : 'bg-wine/10 text-wine'}`}>
                  {c.active ? 'Active' : 'Inactive'}
                </span>
                <button onClick={() => openEdit(c)} className="text-ink-soft hover:text-wine" aria-label="Edit">
                  <Pencil size={15} />
                </button>
                <button onClick={() => handleDelete(c.id)} className="text-ink-soft hover:text-wine" aria-label="Delete">
                  <Trash2 size={15} />
                </button>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
