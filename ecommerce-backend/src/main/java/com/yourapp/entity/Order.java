package com.yourapp.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private BigDecimal totalAmount;
  private String status; // PENDING, CONFIRMED, SHIPPED, CANCELLED

  @ManyToOne
  @JoinColumn(name="user_id")
  private User user;

  @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval=true)
  private List<OrderItem> items;

  @ManyToOne
  @JoinColumn(name="address_id")
  private Address shippingAddress;
}
