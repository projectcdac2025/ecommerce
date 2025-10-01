package com.yourapp.repository;

import com.yourapp.entity.Order;
import com.yourapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user); // get all orders for a user
}
