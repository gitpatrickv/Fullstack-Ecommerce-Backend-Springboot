package com.practice.fullstackbackendspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_gen")
    @SequenceGenerator(name = "inventory_gen", sequenceName = "inventory_seq", allocationSize = 1)
    private Long inventoryId;
    private Long quantity;
    private Double price;
    private String colors;
    private String sizes;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


}