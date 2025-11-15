import React, { useState, useContext } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';

export default function Signup() {
  const { signup } = useContext(AuthContext);
  const [form, setForm] = useState({ username: '', email: '', password: '' });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const submit = async (e) => {
    e.preventDefault();
    // basic client-side validation
    if (!form.username.trim() || !form.email.trim() || !form.password) {
      alert('Please fill all fields');
      return;
    }
    setLoading(true);
    try {
      // AuthContext.signup calls POST /api/auth/signup (as implemented in AuthContext)
      await signup({ username: form.username, email: form.email, password: form.password });
      alert('Account created. Please login.');
      navigate('/login');
    } catch (err) {
      console.error('Signup failed:', err);
      const msg = err?.response?.data?.message || 'Signup failed — try again';
      alert(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 480, margin: '40px auto', padding: 16, border: '1px solid #eee', borderRadius: 8 }}>
      <h2>Create account</h2>

      <form onSubmit={submit}>
        <div style={{ marginBottom: 12 }}>
          <label>Username</label><br />
          <input
            name="username"
            value={form.username}
            onChange={handleChange}
            required
            style={{ width: '100%', padding: 8 }}
          />
        </div>

        <div style={{ marginBottom: 12 }}>
          <label>Email</label><br />
          <input
            name="email"
            type="email"
            value={form.email}
            onChange={handleChange}
            required
            style={{ width: '100%', padding: 8 }}
          />
        </div>

        <div style={{ marginBottom: 12 }}>
          <label>Password</label><br />
          <input
            name="password"
            type="password"
            value={form.password}
            onChange={handleChange}
            required
            minLength={6}
            style={{ width: '100%', padding: 8 }}
          />
          <small>Use at least 6 characters.</small>
        </div>

        <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
          <button type="submit" disabled={loading} style={{ padding: '10px 14px', background: '#007bff', color: 'white', border: 'none', borderRadius: 6, cursor: 'pointer' }}>
            {loading ? 'Creating...' : 'Create account'}
          </button>

          <div style={{ marginLeft: 'auto' }}>
            <small>Already registered? <Link to="/login">Login</Link></small>
          </div>
        </div>
      </form>
    </div>
  );
}
