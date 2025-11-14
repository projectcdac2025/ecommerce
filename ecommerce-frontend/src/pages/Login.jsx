import React, { useState, useContext } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';

export default function Login() {
  const { login } = useContext(AuthContext);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const submit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      // AuthContext.login will call POST /api/auth/login and store token/user
      await login({ email, password });
      // After successful login, redirect to home (or previous page)
      navigate('/');
    } catch (err) {
      console.error('Login failed:', err);
      // Show friendly message - try to extract server message if available
      const msg = err?.response?.data?.message || 'Login failed — check credentials';
      alert(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 420, margin: '40px auto', padding: 16, border: '1px solid #eee', borderRadius: 8 }}>
      <h2>Login</h2>

      <form onSubmit={submit}>
        <div style={{ marginBottom: 12 }}>
          <label>Email</label><br />
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            style={{ width: '100%', padding: 8 }}
          />
        </div>

        <div style={{ marginBottom: 12 }}>
          <label>Password</label><br />
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            style={{ width: '100%', padding: 8 }}
          />
        </div>

        <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
          <button type="submit" disabled={loading} style={{ padding: '10px 14px', background: '#007bff', color: 'white', border: 'none', borderRadius: 6, cursor: 'pointer' }}>
            {loading ? 'Logging in...' : 'Login'}
          </button>

          <div style={{ marginLeft: 'auto' }}>
            <small>Don't have an account? <Link to="/signup">Signup</Link></small>
          </div>
        </div>
      </form>
    </div>
  );
}
