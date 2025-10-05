package com.yourapp.repository;

import com.yourapp.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Basic CRUD operations are inherited from JpaRepository
}
