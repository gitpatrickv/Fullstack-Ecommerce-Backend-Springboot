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
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String storeId;
    private String storeName;
    private String storeDescription;
    private String address;
    private String contactNumber;
    private Double shippingFee;
    private String photoUrl;
    private Long productCount = 0L;
    private Long orderCount = 0L;
    private boolean online;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Product> product = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Order> order = new ArrayList<>();

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<StoreRating> storeRatings = new ArrayList<>();

}
