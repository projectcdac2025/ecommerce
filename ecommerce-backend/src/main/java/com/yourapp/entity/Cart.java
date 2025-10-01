package com.yourapp.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name="carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name="user_id")
  private User user;

  @OneToMany(mappedBy="cart", cascade=CascadeType.ALL, orphanRemoval=true)
  private List<CartItem> items;
}
