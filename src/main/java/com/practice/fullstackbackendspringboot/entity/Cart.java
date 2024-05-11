package com.practice.fullstackbackendspringboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_gen")
//    @SequenceGenerator(name = "cart_gen", sequenceName = "cart_seq", allocationSize = 1)
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


}
