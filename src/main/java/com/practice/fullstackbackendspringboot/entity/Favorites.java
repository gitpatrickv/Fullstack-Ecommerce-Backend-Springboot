package com.practice.fullstackbackendspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "favorites")
public class Favorites {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long favoriteId;
    private String productId;
    private String productName;
    private Double price;
    private String photoUrl;
    private boolean favorites;

    @ManyToOne
    private User user;
}
