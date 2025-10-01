package com.yourapp.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="product_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String url;
  private String altText;
  private Boolean isPrimary = false;

  @ManyToOne
  @JoinColumn(name="product_id")
  private Product product;
}
