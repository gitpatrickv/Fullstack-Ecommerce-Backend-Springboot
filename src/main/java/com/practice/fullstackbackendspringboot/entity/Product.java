package com.practice.fullstackbackendspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String productId;

    private String shopName;
    private String productName;
    private String productDescription;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Inventory> inventory = new ArrayList<>();

    @OneToMany(mappedBy = "product",  cascade = CascadeType.ALL)
    private List<ProductImage> productImage = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
