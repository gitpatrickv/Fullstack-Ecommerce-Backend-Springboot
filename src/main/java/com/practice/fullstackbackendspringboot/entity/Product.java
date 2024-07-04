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
    private String productName;
    @Column(length = 1000)
    private String productDescription;
    private boolean deleted;
    private Long productSold = 0L;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Inventory> inventory = new ArrayList<>();

    @OneToMany(mappedBy = "product",  cascade = CascadeType.ALL)
    private List<Image> image = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Cart> cart = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<RatingAndReview> ratingAndReviews = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Favorites> favorites = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
