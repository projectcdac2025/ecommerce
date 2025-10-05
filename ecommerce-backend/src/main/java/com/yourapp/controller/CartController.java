package com.yourapp.controller;

import com.yourapp.entity.Cart;
import com.yourapp.service.CartService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;
    @PostMapping("/add")
    public Cart addToCart(@RequestBody Map<String, Object> requestBody) { 
        
        // 🔥 CORRECTED LOGIC: Casts to Number and uses longValue()/intValue()
        // This ensures compatibility whether Spring maps the number as Integer or Long.
        Long userId = ((Number) requestBody.get("userId")).longValue();
        Long productId = ((Number) requestBody.get("productId")).longValue();
        int quantity = ((Number) requestBody.get("quantity")).intValue(); 

        return cartService.addToCart(userId, productId, quantity);
    }
        

    // 2. Get cart for a user
    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable Long userId) {
        return cartService.getOrCreateCart(userId);
    }

    @PutMapping("/update")
    public Cart updateCartItemQuantity(@RequestParam Long itemId,
                                      @RequestParam int quantity) {
        return cartService.updateCartItemQuantity(itemId, quantity);
    }
    
    // 4. Remove a specific item from the cart
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeCartItem(@RequestParam Long itemId) {
        cartService.removeCartItem(itemId);
        // Returns 200 OK with a message on successful deletion
        return ResponseEntity.ok("Item removed from cart successfully"); 
    }

    // 5. Clear all items from a user's cart
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        // Returns 200 OK with a message on successful deletion
        return ResponseEntity.ok("Cart cleared successfully");
    }
}
