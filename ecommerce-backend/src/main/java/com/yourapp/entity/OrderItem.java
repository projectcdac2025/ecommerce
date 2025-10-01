package com.yourapp.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name="order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Integer quantity;
  private BigDecimal price; // price at purchase

  @ManyToOne
  @JoinColumn(name="order_id")
  private Order order;

  @ManyToOne
  @JoinColumn(name="product_id")
  private Product product;
}
