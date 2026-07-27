import { useEffect, useState } from 'react';
import { User, KeyRound, ShieldAlert } from 'lucide-react';
import toast from 'react-hot-toast';
import api, { extractErrorMessage } from '../lib/api';
import { useAuth } from '../context/AuthContext';
import { Loader } from '../components/Feedback';

export default function Profile() {
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);

  const [profileForm, setProfileForm] = useState({ fullName: '', phoneNumber: '' });
  const [savingProfile, setSavingProfile] = useState(false);

  const [pwdForm, setPwdForm] = useState({ oldPassword: '', newPassword: '' });
  const [savingPwd, setSavingPwd] = useState(false);

  useEffect(() => {
    if (!user) return;
    api.get(`/v1/users/${user.id}`).then((res) => {
      const data = res.data.data;
      setProfile(data);
      setProfileForm({ fullName: data.fullName, phoneNumber: data.phoneNumber });
    }).finally(() => setLoading(false));
  }, [user]);

  const handleProfileSave = async (e) => {
    e.preventDefault();
    setSavingProfile(true);
    try {
      const res = await api.put(`/v1/users/${user.id}`, profileForm);
      setProfile(res.data.data);
      toast.success('Profile updated.');
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not update profile.'));
    } finally {
      setSavingProfile(false);
    }
  };

  const handlePasswordChange = async (e) => {
    e.preventDefault();
    setSavingPwd(true);
    try {
      await api.put(`/v1/users/${user.id}/change-password`, pwdForm);
      toast.success('Password changed. Use it next time you sign in.');
      setPwdForm({ oldPassword: '', newPassword: '' });
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Could not change password.'));
    } finally {
      setSavingPwd(false);
    }
  };

  if (loading) return <Loader label="Loading your profile…" />;

  return (
    <div className="max-w-xl mx-auto px-4 sm:px-6 py-10 animate-page-in">
      <div className="flex items-center gap-3 mb-8">
        <div className="w-12 h-12 rounded-full bg-ink text-paper flex items-center justify-center">
          <User size={22} />
        </div>
        <div>
          <h1 className="font-display text-2xl text-ink">{profile?.fullName}</h1>
          <p className="text-sm text-ink-soft">{profile?.email}</p>
        </div>
      </div>

      <section className="bg-paper-deep/50 border border-line rounded-sm p-6 mb-6">
        <h2 className="font-display text-xl text-ink mb-4">Your details</h2>
        <form onSubmit={handleProfileSave} className="flex flex-col gap-4">
          <div>
            <label className="block text-xs font-mono uppercase tracking-wide text-ink-soft mb-1">
              Full name
            </label>
            <input
              value={profileForm.fullName}
              onChange={(e) => setProfileForm({ ...profileForm, fullName: e.target.value })}
              className="w-full px-3 py-2.5 bg-paper border border-line rounded-sm text-ink focus:border-wine outline-none"
              required
            />
          </div>
          <div>
            <label className="block text-xs font-mono uppercase tracking-wide text-ink-soft mb-1">
              Phone number
            </label>
            <input
              value={profileForm.phoneNumber}
              onChange={(e) => setProfileForm({ ...profileForm, phoneNumber: e.target.value.replace(/\D/g, '').slice(0, 10) })}
              className="w-full px-3 py-2.5 bg-paper border border-line rounded-sm text-ink focus:border-wine outline-none"
              required
            />
          </div>
          <button
            type="submit"
            disabled={savingProfile}
            className="self-start bg-ink hover:bg-wine disabled:opacity-60 transition-colors text-paper text-sm font-semibold px-5 py-2.5 rounded-sm"
          >
            {savingProfile ? 'Saving…' : 'Save changes'}
          </button>
        </form>
      </section>

      <section className="bg-paper-deep/50 border border-line rounded-sm p-6">
        <h2 className="font-display text-xl text-ink mb-4 flex items-center gap-2">
          <KeyRound size={18} /> Change password
        </h2>
        <form onSubmit={handlePasswordChange} className="flex flex-col gap-4">
          <div>
            <label className="block text-xs font-mono uppercase tracking-wide text-ink-soft mb-1">
              Current password
            </label>
            <input
              type="password"
              value={pwdForm.oldPassword}
              onChange={(e) => setPwdForm({ ...pwdForm, oldPassword: e.target.value })}
              className="w-full px-3 py-2.5 bg-paper border border-line rounded-sm text-ink focus:border-wine outline-none"
              required
            />
          </div>
          <div>
            <label className="block text-xs font-mono uppercase tracking-wide text-ink-soft mb-1">
              New password
            </label>
            <input
              type="password"
              value={pwdForm.newPassword}
              onChange={(e) => setPwdForm({ ...pwdForm, newPassword: e.target.value })}
              className="w-full px-3 py-2.5 bg-paper border border-line rounded-sm text-ink focus:border-wine outline-none"
              required
              minLength={5}
              maxLength={20}
            />
          </div>
          <div className="flex items-start gap-2 text-xs text-ink-faint">
            <ShieldAlert size={14} className="mt-0.5 shrink-0" />
            <p>You'll need this new password the next time you sign in.</p>
          </div>
          <button
            type="submit"
            disabled={savingPwd}
            className="self-start bg-wine hover:bg-wine-dark disabled:opacity-60 transition-colors text-paper text-sm font-semibold px-5 py-2.5 rounded-sm"
          >
            {savingPwd ? 'Updating…' : 'Update password'}
          </button>
        </form>
      </section>
    </div>
  );
}
