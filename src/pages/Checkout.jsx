import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, MapPin, CheckCircle2 } from 'lucide-react';
import toast from 'react-hot-toast';
import api, { extractErrorMessage } from '../lib/api';
import { useCart } from '../context/CartContext';
import { Loader } from '../components/Feedback';

const emptyAddressForm = {
  addressLine: '', city: '', state: '', country: 'India', postalCode: '', addressType: 'HOME',
};

export default function Checkout() {
  const { cart, refreshCart } = useCart();
  const navigate = useNavigate();

  const [customer, setCustomer] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedAddressId, setSelectedAddressId] = useState(null);
  const [showAddressForm, setShowAddressForm] = useState(false);
  const [addressForm, setAddressForm] = useState(emptyAddressForm);
  const [profileForm, setProfileForm] = useState({ firstName: '', lastName: '', phoneNumber: '' });
  const [needsProfile, setNeedsProfile] = useState(false);
  const [saving, setSaving] = useState(false);
  const [placingOrder, setPlacingOrder] = useState(false);

  const loadCustomer = async () => {
    setLoading(true);
    try {
      const res = await api.get('/customer/profile');
      setCustomer(res.data.data);
      if (res.data.data.addresses?.length > 0) {
        setSelectedAddressId(res.data.data.addresses[0].id);
      }
      setNeedsProfile(false);
    } catch {
      // No profile yet — normal for a first-time buyer
      setNeedsProfile(true);
      setCustomer(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadCustomer(); }, []);

  const handleCreateProfile = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      const res = await api.post('/customer/profile', profileForm);
      setCustomer(res.data.data);
      setNeedsProfile(false);
      toast.success('Profile created. Now add a delivery address.');
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not create profile.'));
    } finally {
      setSaving(false);
    }
  };

  const handleAddAddress = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      const res = await api.post('/customer/address', addressForm);
      setCustomer(res.data.data);
      const newAddress = res.data.data.addresses[res.data.data.addresses.length - 1];
      setSelectedAddressId(newAddress.id);
      setShowAddressForm(false);
      setAddressForm(emptyAddressForm);
      toast.success('Address added.');
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not add address.'));
    } finally {
      setSaving(false);
    }
  };

  const handlePlaceOrder = async () => {
    if (!selectedAddressId) {
      toast.error('Please select a delivery address.');
      return;
    }
    setPlacingOrder(true);
    try {
      const res = await api.post('/orders', { shippingAddressId: selectedAddressId });
      toast.success('Order placed!');
      await refreshCart();
      navigate('/orders');
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not place order.'));
    } finally {
      setPlacingOrder(false);
    }
  };

  if (loading) return <Loader label="Preparing checkout…" />;

  if (!cart || !cart.items || cart.items.length === 0) {
    return (
      <div className="max-w-lg mx-auto px-4 py-16 text-center">
        <p className="text-ink-soft">Your cart is empty — nothing to check out yet.</p>
      </div>
    );
  }

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 py-10 animate-page-in">
      <h1 className="font-display text-3xl text-ink mb-8">Checkout</h1>

      {needsProfile ? (
        <section className="bg-paper-deep/50 border border-line rounded-sm p-6 mb-6">
          <h2 className="font-display text-xl text-ink mb-1">First, a few details</h2>
          <p className="text-sm text-ink-soft mb-4">We need this once, so we know who's ordering.</p>
          <form onSubmit={handleCreateProfile} className="grid sm:grid-cols-2 gap-3">
            <TextInput label="First name" value={profileForm.firstName} onChange={(v) => setProfileForm({ ...profileForm, firstName: v })} required />
            <TextInput label="Last name" value={profileForm.lastName} onChange={(v) => setProfileForm({ ...profileForm, lastName: v })} required />
            <TextInput
              label="Phone number"
              value={profileForm.phoneNumber}
              onChange={(v) => setProfileForm({ ...profileForm, phoneNumber: v.replace(/\D/g, '').slice(0, 10) })}
              required
              className="sm:col-span-2"
            />
            <button
              type="submit"
              disabled={saving}
              className="sm:col-span-2 self-start bg-wine hover:bg-wine-dark disabled:opacity-60 transition-colors text-paper font-semibold px-5 py-2.5 rounded-sm"
            >
              {saving ? 'Saving…' : 'Continue'}
            </button>
          </form>
        </section>
      ) : (
        <section className="bg-paper-deep/50 border border-line rounded-sm p-6 mb-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="font-display text-xl text-ink flex items-center gap-2">
              <MapPin size={18} /> Delivery address
            </h2>
            <button
              onClick={() => setShowAddressForm(!showAddressForm)}
              className="flex items-center gap-1 text-sm text-wine font-semibold hover:underline"
            >
              <Plus size={14} /> Add new
            </button>
          </div>

          {customer?.addresses?.length > 0 && (
            <div className="grid sm:grid-cols-2 gap-3 mb-4">
              {customer.addresses.map((addr) => (
                <button
                  key={addr.id}
                  onClick={() => setSelectedAddressId(addr.id)}
                  className={`text-left border rounded-sm p-3 transition-colors ${
                    selectedAddressId === addr.id ? 'border-wine bg-wine/5' : 'border-line hover:border-brass'
                  }`}
                >
                  <div className="flex items-center justify-between">
                    <span className="text-xs font-mono uppercase tracking-wide text-brass">{addr.addressType}</span>
                    {selectedAddressId === addr.id && <CheckCircle2 size={16} className="text-wine" />}
                  </div>
                  <p className="text-sm text-ink mt-1">{addr.addressLine}</p>
                  <p className="text-sm text-ink-soft">{addr.city}, {addr.state} {addr.postalCode}</p>
                  <p className="text-sm text-ink-soft">{addr.country}</p>
                </button>
              ))}
            </div>
          )}

          {showAddressForm && (
            <form onSubmit={handleAddAddress} className="border-t border-line pt-4 grid sm:grid-cols-2 gap-3">
              <TextInput label="Address line" value={addressForm.addressLine} onChange={(v) => setAddressForm({ ...addressForm, addressLine: v })} required className="sm:col-span-2" />
              <TextInput label="City" value={addressForm.city} onChange={(v) => setAddressForm({ ...addressForm, city: v })} required />
              <TextInput label="State" value={addressForm.state} onChange={(v) => setAddressForm({ ...addressForm, state: v })} required />
              <TextInput label="Postal code (6-digit)" value={addressForm.postalCode} onChange={(v) => setAddressForm({ ...addressForm, postalCode: v.replace(/\D/g, '').slice(0, 6) })} required />
              <TextInput label="Country" value={addressForm.country} onChange={(v) => setAddressForm({ ...addressForm, country: v })} required />
              <div className="sm:col-span-2">
                <label className="block text-xs font-mono uppercase tracking-wide text-ink-soft mb-1">Address type</label>
                <select
                  value={addressForm.addressType}
                  onChange={(e) => setAddressForm({ ...addressForm, addressType: e.target.value })}
                  className="w-full px-3 py-2 bg-paper border border-line rounded-sm text-ink text-sm focus:border-wine outline-none"
                >
                  <option value="HOME">Home</option>
                  <option value="WORK">Work</option>
                  <option value="OTHER">Other</option>
                </select>
              </div>
              <button
                type="submit"
                disabled={saving}
                className="sm:col-span-2 self-start bg-ink hover:bg-wine disabled:opacity-60 transition-colors text-paper text-sm font-semibold px-5 py-2.5 rounded-sm"
              >
                {saving ? 'Saving…' : 'Save address'}
              </button>
            </form>
          )}

          {!customer?.addresses?.length && !showAddressForm && (
            <p className="text-sm text-ink-soft">No saved addresses yet — add one to continue.</p>
          )}
        </section>
      )}

      <section className="bg-paper-deep/50 border border-line rounded-sm p-6">
        <h2 className="font-display text-xl text-ink mb-4">Order summary</h2>
        <ul className="flex flex-col gap-2 mb-4">
          {cart.items.map((item) => (
            <li key={item.productId} className="flex justify-between text-sm">
              <span className="text-ink-soft">{item.productTitle} × {item.quantity}</span>
              <span className="font-mono text-ink">₹{Number(item.totalPrice).toFixed(2)}</span>
            </li>
          ))}
        </ul>
        <div className="ledger-rule mb-4" />
        <div className="flex items-center justify-between">
          <span className="font-display text-2xl text-ink">₹{Number(cart.totalAmount).toFixed(2)}</span>
          <button
            onClick={handlePlaceOrder}
            disabled={placingOrder || needsProfile || !selectedAddressId}
            className="bg-wine hover:bg-wine-dark disabled:opacity-50 disabled:cursor-not-allowed transition-colors text-paper font-semibold px-6 py-3 rounded-sm"
          >
            {placingOrder ? 'Placing order…' : 'Place order'}
          </button>
        </div>
      </section>
    </div>
  );
}

function TextInput({ label, value, onChange, required, className = '' }) {
  return (
    <div className={className}>
      <label className="block text-xs font-mono uppercase tracking-wide text-ink-soft mb-1">{label}</label>
      <input
        value={value}
        onChange={(e) => onChange(e.target.value)}
        required={required}
        className="w-full px-3 py-2 bg-paper border border-line rounded-sm text-ink text-sm focus:border-wine outline-none"
      />
    </div>
  );
}
