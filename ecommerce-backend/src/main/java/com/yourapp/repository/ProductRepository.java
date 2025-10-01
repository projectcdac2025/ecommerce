package com.yourapp.repository;

import com.yourapp.entity.Product;
import com.yourapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    List<Product> findByNameContainingIgnoreCase(String name); // search products
}
