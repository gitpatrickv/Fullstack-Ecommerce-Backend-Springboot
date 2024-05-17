package com.practice.fullstackbackendspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToOne(mappedBy = "cartTotal")
    private Cart cart;

    @OneToOne
    private User user;
}
