package com.practice.fullstackbackendspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String orderStatus;
    private Long quantity;
    private Double price;
    private Double totalAmount;
    private String storeName;
    private String productName;
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
