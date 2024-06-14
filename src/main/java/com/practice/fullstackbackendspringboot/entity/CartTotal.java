package com.practice.fullstackbackendspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "cart_total")
public class CartTotal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_gen")
    @SequenceGenerator(name = "cart_gen", sequenceName = "cart_seq", allocationSize = 1)
    private Long cartTotalId;
    private Double cartTotal;
    private Long cartItems;
    private Long qty;
    private Double totalShippingFee;
    private Double totalPayment;
    private Long productCount;

    @OneToOne(mappedBy = "cartTotal")
    private Cart cart;

    @OneToOne
    private User user;
}
