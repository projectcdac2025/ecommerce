import React from 'react';
import { Link } from 'react-router-dom';

export default function ProductCard({ product }) {
  // Determine the best image to show: product.image OR first productImages.url OR placeholder
  const imageUrl =
    product?.image ||
    (product?.productImages && product.productImages.length > 0 && (product.productImages[0].url || product.productImages[0].image)) ||
    'https://via.placeholder.com/200';

  return (
    <div style={{
      border: '1px solid #ddd',
      padding: '15px',
      borderRadius: '8px',
      textAlign: 'center'
    }}>
      
      <img 
        src={imageUrl}
        alt={product?.name || 'product'}
        style={{ width: '100%', height: '160px', objectFit: 'cover', borderRadius: '8px' }}
      />

      <h3>{product?.name}</h3>
      <p>₹{product?.price}</p>

      <Link 
        to={`/products/${product?.id}`}
        style={{
          display: 'inline-block',
          marginTop: '10px',
          padding: '6px 12px',
          backgroundColor: '#007bff',
          color: 'white',
          borderRadius: '6px',
          textDecoration: 'none'
        }}
      >
        View
      </Link>

    </div>
  );
}
