import React, { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';

export default function Navbar() {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav style={{
      display: 'flex',
      alignItems: 'center',
      padding: '10px 20px',
      borderBottom: '1px solid #ddd',
      gap: '20px'
    }}>
      
      <Link to="/">
        <h2 style={{ margin: 0 }}>MyShop</h2>
      </Link>

      <div style={{ flex: 1 }} />

      <Link to="/cart">Cart</Link>

      {user ? (
        <>
          <span>Hello, {user.username || user.email}</span>
          <button onClick={handleLogout}>Logout</button>
        </>
      ) : (
        <>
          <Link to="/login">Login</Link>
          <Link to="/signup">Signup</Link>
        </>
      )}

    </nav>
  );
}
