package com.practice.fullstackbackendspringboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cartId;
    private Long quantity;
    private Double price;
    private Double totalAmount;
    private String shopName;
    private String productName;
    private boolean filter;
    private String photoUrl;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @OneToOne
    private CartTotal cartTotal;

}
