package com.practice.fullstackbackendspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
@EqualsAndHashCode(of = "productId", callSuper = false)
public class Product extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String productId;
    private String productName;
    @Column(length = 1000)
    private String productDescription;
    private boolean deleted;
    private Long productSold = 0L;
    private boolean listed;
    private boolean suspended;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<Inventory> inventory = new HashSet<>();

    @OneToMany(mappedBy = "product",  cascade = CascadeType.ALL)
    private List<Image> image = new ArrayList<>();

    @OneToMany(mappedBy = "product",  cascade = CascadeType.ALL)
    private List<Cart> cart = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<RatingAndReview> ratingAndReviews = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
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
