package com.yourapp.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @Column(length = 4000)
  private String description;
  private BigDecimal price;
  private Integer stock;
  private String sku;

  @ManyToOne
  @JoinColumn(name="category_id")
  private Category category;

  @OneToMany(mappedBy="product", cascade=CascadeType.ALL)
  private List<ProductImage> images;
}
