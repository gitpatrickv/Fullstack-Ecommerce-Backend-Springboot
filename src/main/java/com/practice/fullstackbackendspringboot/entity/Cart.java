package com.practice.fullstackbackendspringboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class Cart extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cartId;
    private Long quantity;
    private Double price;
    private Double totalAmount;
    private String storeName;
    private String storeId;
    private String productName;
    private boolean filter;
    private String photoUrl;
    private String colors;
    private String sizes;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

}
