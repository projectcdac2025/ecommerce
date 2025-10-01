package com.yourapp.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Integer quantity;

  @ManyToOne
  @JoinColumn(name="cart_id")
  private Cart cart;

  @ManyToOne					//we are not providing mappedBy=cart_items in Product
  @JoinColumn(name="product_id")// class bcz we don't need to fetch card_items details there in product class
  private Product product;
}
