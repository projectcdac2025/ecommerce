package com.yourapp.service;

import com.yourapp.entity.Cart;
import com.yourapp.entity.CartItem;
import com.yourapp.entity.Product;
import com.yourapp.entity.User;
import com.yourapp.repository.CartRepository;
import com.yourapp.repository.ProductRepository;
import com.yourapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Get or create cart for a user
    public Cart getOrCreateCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        if(optionalCart.isPresent()) return optionalCart.get();

        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    // Add item to cart
    public CartItem addToCart(Long userId, Long productId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(quantity);

        cart.getItems().add(item);
        cartRepository.save(cart);
        return item;
    }
}
