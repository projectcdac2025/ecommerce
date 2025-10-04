package com.yourapp.controller;

import com.yourapp.entity.Product;
import com.yourapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/{categoryId}")
    public Product createProduct(@RequestBody Product product, @PathVariable Long categoryId) {
        return productService.createProduct(product, categoryId);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }
    
    @PutMapping("/{productId}/{categoryId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @PathVariable(required = false) Long categoryId,
            @RequestBody Product updatedProduct) {

        Product product = productService.updateProduct(productId, updatedProduct, categoryId);
        return ResponseEntity.ok(product);
    }

    
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully");
    }

}
