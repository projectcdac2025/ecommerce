package com.yourapp.controller;

import com.yourapp.entity.Order;
import com.yourapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Place order from cart
    @PostMapping("/place")
    public Order placeOrder(@RequestParam Long userId) {
        return orderService.placeOrder(userId);
    }

    // Get all orders of a user
    @GetMapping("/{userId}")
    public List<Order> getOrders(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }
}
