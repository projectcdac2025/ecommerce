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
    public Cart addToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow();
        Cart cart = cartRepository.findByUser(user).orElse(new Cart(user));

        Product product = productRepository.findById(productId).orElseThrow();

        // create or update CartItem
        Optional<CartItem> existingItem = cart.getItems().stream()
            .filter(item -> item.getProduct().equals(product))
            .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(cart, product, quantity);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);  // ✅ return Cart, not CartItem
    }

}
