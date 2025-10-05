package com.yourapp.service;

import com.yourapp.entity.Cart;
import com.yourapp.entity.CartItem;
import com.yourapp.entity.Product;
import com.yourapp.entity.User;
import com.yourapp.repository.CartRepository;
import com.yourapp.repository.ProductRepository;
import com.yourapp.repository.UserRepository;
import com.yourapp.repository.CartItemRepository; 

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
    
    @Autowired
    private CartItemRepository cartItemRepository; 

 // Get or create cart for a user
    public Cart getOrCreateCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        
        // If cart exists, return it
        if(optionalCart.isPresent()) return optionalCart.get();

        // If cart does not exist, create a new one
        Cart cart = new Cart(user);
        return cartRepository.save(cart);
    }
    // Add item to cart
    public Cart addToCart(Long userId, Long productId, int quantity) {
        
        // 💡 FIX: Use getOrCreateCart() to ensure the cart is always persistent.
        Cart cart = getOrCreateCart(userId); 

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

        return cartRepository.save(cart);  
    }
    // Add item to cart (or update quantity)
//    public Cart addToCart(Long userId, Long productId, int quantity) {
//        User user = userRepository.findById(userId)
//            .orElseThrow(() -> new RuntimeException("User not found"));
//        
//        // Get or create the cart
//        Cart cart = cartRepository.findByUser(user).orElse(new Cart(user));
//        
//        Product product = productRepository.findById(productId)
//            .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        // Find existing item in the cart
//        Optional<CartItem> existingItem = cart.getItems().stream()
//            .filter(item -> item.getProduct().getId().equals(productId)) // Compare by Product ID
//            .findFirst();
//
//        if (existingItem.isPresent()) {
//            // Update existing item quantity
//            CartItem item = existingItem.get();
//            item.setQuantity(item.getQuantity() + quantity);
//            cartItemRepository.save(item); // Save the updated item
//        } else {
//            // Add new item to cart
//            CartItem newItem = new CartItem(cart, product, quantity);
//            cart.getItems().add(newItem);
//            cartItemRepository.save(newItem); // Save the new item
//        }
//        
//        // Save the Cart. Note that saving the Cart should handle Cascading of CartItems, 
//        // but explicit CartItem saves above ensure the item exists before the main cart save.
//        return cartRepository.save(cart);  
//    }
    
    // Update quantity of a specific cart item
    public Cart updateCartItemQuantity(Long itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        if (quantity <= 0) {
            // If quantity is zero or less, remove the item
            removeCartItem(itemId);
            return item.getCart(); // Return the parent cart after removal
        }
        
        item.setQuantity(quantity);
        cartItemRepository.save(item);
        
        return item.getCart();
    }
    
    // Remove a specific item from the cart
    public void removeCartItem(Long itemId) {
        // Check if item exists and delete it
        if (!cartItemRepository.existsById(itemId)) {
            throw new RuntimeException("Cart item not found with id: " + itemId);
        }
        cartItemRepository.deleteById(itemId);
    }
    
    // Clear all items from a user's cart
    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Cart cart = cartRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Cart not found for user"));
        
        // Delete all cart items associated with this cart
        cartItemRepository.deleteAll(cart.getItems());
        
        // Clear the list reference and save the empty cart
        cart.getItems().clear();
        cartRepository.save(cart);
    }

}
