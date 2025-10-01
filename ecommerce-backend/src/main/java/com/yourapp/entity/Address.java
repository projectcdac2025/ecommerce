package com.yourapp.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String line1;
  private String city;
  private String state;
  private String pincode;
  private String country;

  @ManyToOne
  @JoinColumn(name="user_id")
  private User user;
}
