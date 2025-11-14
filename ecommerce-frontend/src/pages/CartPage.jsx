import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function CartPage() {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  // Helper to extract image URL (same logic used in product components)
  const getImageUrl = (product) => {
    return (
      product?.image ||
      (product?.productImages && product.productImages.length > 0 && (product.productImages[0].url || product.productImages[0].image)) ||
      'https://via.placeholder.com/100'
    );
  };

  // Fetch cart for current user
  const fetchCart = async () => {
    setLoading(true);
    try {
      const storedUser = localStorage.getItem('user');
      if (!storedUser) {
        setCart({ items: [] });
        setLoading(false);
        return;
      }
      const user = JSON.parse(storedUser);
      const res = await api.get(`/carts/${user.id}`);
      setCart(res.data);
    } catch (err) {
      console.error('Error loading cart:', err);
      setCart({ items: [] });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCart();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // Compute cart total
  const computeTotal = () => {
    if (!cart?.items) return 0;
    return cart.items.reduce((sum, it) => {
      const price = it?.product?.price || 0;
      const qty = it?.quantity || 0;
      return sum + price * qty;
    }, 0);
  };

  // Update quantity of a cart item
  const updateQuantity = async (item, newQty) => {
    if (newQty < 1) return;
    try {
      // Attempt to update on server
      // NOTE: payload shape may differ in your backend.
      // This assumes backend accepts { cartItemId, quantity } in PUT /api/carts/update
      await api.put('/carts/update', { cartItemId: item.id, quantity: newQty });

      // Refresh cart from server to keep state consistent
      await fetchCart();
    } catch (err) {
      console.error('Could not update quantity:', err);
      alert('Could not update quantity. Try again.');
    }
  };

  // Remove a cart item
  const removeItem = async (item) => {
    try {
      // Axios delete with request body uses { data: {...} }
      // This assumes backend expects { cartItemId } in request body for DELETE /api/carts/remove
      await api.delete('/carts/remove', { data: { cartItemId: item.id } });
      await fetchCart();
    } catch (err) {
      console.error('Could not remove item:', err);
      alert('Could not remove item. Try again.');
    }
  };

  // Proceed to checkout
  const handleCheckout = () => {
    const storedUser = localStorage.getItem('user');
    if (!storedUser) {
      alert('Please login before checkout');
      navigate('/login');
      return;
    }
    navigate('/checkout');
  };

  if (loading) return <div>Loading cart...</div>;

  return (
    <div style={{ padding: 20 }}>
      <h2>Your Cart</h2>

      {(!cart?.items || cart.items.length === 0) ? (
        <div>
          <p>Your cart is empty.</p>
          <Link to="/">Continue shopping</Link>
        </div>
      ) : (
        <>
          <div style={{ display: 'grid', gap: 12 }}>
            {cart.items.map((item) => (
              <div key={item.id} style={{ display: 'flex', gap: 12, alignItems: 'center', borderBottom: '1px solid #eee', paddingBottom: 10 }}>
                <img src={getImageUrl(item.product)} alt={item.product?.name} style={{ width: 100, height: 80, objectFit: 'cover', borderRadius: 6 }} />

                <div style={{ flex: 1 }}>
                  <div style={{ fontWeight: 600 }}>{item.product?.name}</div>
                  <div>₹{item.product?.price}</div>
                  <div style={{ marginTop: 8 }}>
                    Qty:
                    <input
                      type="number"
                      min="1"
                      value={item.quantity}
                      onChange={(e) => {
                        const q = Number(e.target.value) || 1;
                        // Update UI optimistically (optional) — here we wait for server response
                        updateQuantity(item, q);
                      }}
                      style={{ width: 60, marginLeft: 8 }}
                    />
                  </div>
                </div>

                <div style={{ textAlign: 'right' }}>
                  <div style={{ marginBottom: 8, fontWeight: 600 }}>₹{(item.product?.price || 0) * (item.quantity || 0)}</div>
                  <button onClick={() => removeItem(item)} style={{ background: 'transparent', border: '1px solid #ccc', padding: '6px 10px', borderRadius: 6, cursor: 'pointer' }}>Remove</button>
                </div>
              </div>
            ))}
          </div>

          <div style={{ marginTop: 20, textAlign: 'right' }}>
            <div style={{ fontSize: 18, marginBottom: 12 }}>Total: <strong>₹{computeTotal()}</strong></div>
            <button onClick={handleCheckout} style={{ padding: '10px 18px', background: '#007bff', color: 'white', border: 'none', borderRadius: 6, cursor: 'pointer' }}>Proceed to Checkout</button>
          </div>
        </>
      )}
    </div>
  );
}
