package com.practice.fullstackbackendspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_variation")
public class ProductVariation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_variation_gen")
    @SequenceGenerator(name = "product_variation_gen", sequenceName = "product_variation_seq", allocationSize = 1)
    private Long variationId;

    private String color;
    private String size;

    @OneToMany(mappedBy = "productVariation", cascade = CascadeType.ALL)
    private List<Inventory> inventory;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
