package com.yourapp.service;

import com.yourapp.entity.Order;
import com.yourapp.entity.Cart;
import com.yourapp.entity.CartItem;
import com.yourapp.entity.User;
import com.yourapp.repository.OrderRepository;
import com.yourapp.repository.CartRepository;
import com.yourapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    // Place order from cart
    public Order placeOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        double totalAmount = cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalAmount);

        // Clear cart after placing order
        cart.getItems().clear();
        cartRepository.save(cart);

        return orderRepository.save(order);
    }
}
