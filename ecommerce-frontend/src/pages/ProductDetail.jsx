import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function ProductDetail() {
  const { id } = useParams(); // get product id from URL
  const [product, setProduct] = useState(null);
  const [qty, setQty] = useState(1);

  const navigate = useNavigate();

  // helper to extract image url (same logic as ProductCard)
  const getImageUrl = (p) => {
    return (
      p?.image ||
      (p?.productImages && p.productImages.length > 0 && (p.productImages[0].url || p.productImages[0].image)) ||
      "https://via.placeholder.com/400"
    );
  };

  // Fetch product by ID when page loads
  useEffect(() => {
    api.get(`/products/${id}`)
      .then(response => setProduct(response.data))
      .catch(error => {
        console.error("Error fetching product:", error);
        // optional: navigate to not-found page or show a message
      });
  }, [id]);

  const addToCart = async () => {
    if (!product) return;

    const storedUser = localStorage.getItem('user');
    if (!storedUser) {
      alert("You must be logged in to add to cart");
      navigate('/login');
      return;
    }

    const user = JSON.parse(storedUser);

    try {
      await api.post('/carts/add', {
        userId: user.id,
        productId: product.id,
        quantity: qty
      });
      navigate('/cart');
    } catch (err) {
      console.error('Error adding to cart:', err);
      alert('Could not add to cart. Please try again.');
    }
  };

  if (!product) return <h3>Loading...</h3>;

  return (
    <div style={{ display: 'flex', gap: '30px', padding: '20px' }}>

      <img
        src={getImageUrl(product)}
        alt={product.name}
        style={{ width: '350px', borderRadius: '8px' }}
      />

      <div>
        <h2>{product.name}</h2>
        <h3>₹{product.price}</h3>
        <p>{product.description}</p>

        <div style={{ marginTop: '15px' }}>
          <label>Quantity: </label>
          <input
            type="number"
            min="1"
            value={qty}
            onChange={(e) => setQty(Number(e.target.value))}
            style={{ width: '60px' }}
          />
        </div>

        <button
          onClick={addToCart}
          style={{
            marginTop: '20px',
            padding: '10px 20px',
            background: '#28a745',
            color: 'white',
            border: 'none',
            borderRadius: '6px',
            cursor: 'pointer'
          }}
        >
          Add to Cart
        </button>

      </div>
    </div>
  );
}
