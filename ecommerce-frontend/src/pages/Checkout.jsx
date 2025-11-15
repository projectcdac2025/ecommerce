import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function Checkout() {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const [address, setAddress] = useState({
    line1: '',
    city: '',
    state: '',
    pincode: '',
    country: ''
  });
  const [placing, setPlacing] = useState(false);
  const navigate = useNavigate();

  // Helper to compute total
  const computeTotal = (cartData) => {
    if (!cartData?.items) return 0;
    return cartData.items.reduce((sum, it) => {
      const price = it?.product?.price || 0;
      const qty = it?.quantity || 0;
      return sum + price * qty;
    }, 0);
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
      console.error('Error loading cart for checkout:', err);
      setCart({ items: [] });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCart();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // Place order
  const placeOrder = async (e) => {
    e.preventDefault();
    const storedUser = localStorage.getItem('user');
    if (!storedUser) {
      alert('Please login to place an order');
      navigate('/login');
      return;
    }

    if (!cart?.items || cart.items.length === 0) {
      alert('Your cart is empty');
      return;
    }

    setPlacing(true);
    const user = JSON.parse(storedUser);

    try {
      // **Important:** Controller expects userId as a request parameter:
      // @PostMapping("/place")
      // public Order placeOrder(@RequestParam Long userId)
      // So we send the POST with params (query string), not JSON body.
      await api.post('/orders/place', null, { params: { userId: user.id } });

      // Try to clear cart (non-critical; backend may already clear on order)
      try {
        await api.delete(`/carts/clear/${user.id}`);
      } catch (clearErr) {
        console.warn('Could not clear cart automatically:', clearErr);
      }

      alert('Order placed successfully!');
      navigate('/'); // or navigate('/orders') if you want to show order history
    } catch (err) {
      console.error('Error placing order:', err);
      alert('Could not place order — please try again.');
    } finally {
      setPlacing(false);
    }
  };

  if (loading) return <div>Loading checkout...</div>;

  return (
    <div style={{ padding: 20, maxWidth: 900, margin: '0 auto' }}>
      <h2>Checkout</h2>

      {(!cart?.items || cart.items.length === 0) ? (
        <div>
          <p>Your cart is empty.</p>
        </div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 380px', gap: 24 }}>

          {/* Left: Shipping form */}
          <form onSubmit={placeOrder} style={{ border: '1px solid #eee', padding: 16, borderRadius: 8 }}>
            <h3>Shipping details</h3>

            <div style={{ marginBottom: 10 }}>
              <label>Address line 1</label><br />
              <input value={address.line1} onChange={(e) => setAddress({ ...address, line1: e.target.value })} required style={{ width: '100%', padding: 8 }} />
            </div>

            <div style={{ display: 'flex', gap: 8, marginBottom: 10 }}>
              <div style={{ flex: 1 }}>
                <label>City</label><br />
                <input value={address.city} onChange={(e) => setAddress({ ...address, city: e.target.value })} required style={{ width: '100%', padding: 8 }} />
              </div>
              <div style={{ width: 120 }}>
                <label>Pincode</label><br />
                <input value={address.pincode} onChange={(e) => setAddress({ ...address, pincode: e.target.value })} required style={{ width: '100%', padding: 8 }} />
              </div>
            </div>

            <div style={{ display: 'flex', gap: 8, marginBottom: 10 }}>
              <div style={{ flex: 1 }}>
                <label>State</label><br />
                <input value={address.state} onChange={(e) => setAddress({ ...address, state: e.target.value })} required style={{ width: '100%', padding: 8 }} />
              </div>
              <div style={{ width: 150 }}>
                <label>Country</label><br />
                <input value={address.country} onChange={(e) => setAddress({ ...address, country: e.target.value })} required style={{ width: '100%', padding: 8 }} />
              </div>
            </div>

            <div style={{ marginTop: 12 }}>
              <button type="submit" disabled={placing} style={{ padding: '10px 16px', background: '#28a745', color: 'white', border: 'none', borderRadius: 6, cursor: 'pointer' }}>
                {placing ? 'Placing...' : 'Place Order'}
              </button>
            </div>
          </form>

          {/* Right: Order summary */}
          <aside style={{ border: '1px solid #eee', padding: 16, borderRadius: 8 }}>
            <h3>Order summary</h3>

            <div style={{ display: 'grid', gap: 10, marginTop: 8 }}>
              {cart.items.map((item) => (
                <div key={item.id} style={{ display: 'flex', gap: 10, alignItems: 'center', borderBottom: '1px solid #f1f1f1', paddingBottom: 8 }}>
                  <img src={ item?.product?.image || (item?.product?.productImages && item.product.productImages.length? (item.product.productImages[0].url || item.product.productImages[0].image) : 'https://via.placeholder.com/80') } alt={item.product?.name} style={{ width: 80, height: 60, objectFit: 'cover', borderRadius: 6 }} />
                  <div style={{ flex: 1 }}>
                    <div style={{ fontWeight: 600 }}>{item.product?.name}</div>
                    <div>Qty: {item.quantity}</div>
                  </div>
                  <div style={{ fontWeight: 700 }}>₹{(item.product?.price || 0) * (item.quantity || 0)}</div>
                </div>
              ))}
            </div>

            <div style={{ marginTop: 12, borderTop: '1px solid #eee', paddingTop: 12 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 6 }}>
                <div>Subtotal</div>
                <div>₹{computeTotal(cart)}</div>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 6 }}>
                <div>Shipping</div>
                <div>₹0</div>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 18, fontWeight: 700 }}>
                <div>Total</div>
                <div>₹{computeTotal(cart)}</div>
              </div>
            </div>
          </aside>

        </div>
      )}
    </div>
  );
}
