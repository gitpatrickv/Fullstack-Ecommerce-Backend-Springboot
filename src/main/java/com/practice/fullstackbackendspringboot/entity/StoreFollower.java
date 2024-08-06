package com.practice.fullstackbackendspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "store_follower")
@EqualsAndHashCode(of = {"user", "store"})
public class StoreFollower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeFollowerId;
    private boolean followed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
