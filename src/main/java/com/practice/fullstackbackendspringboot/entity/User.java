package com.practice.fullstackbackendspringboot.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.practice.fullstackbackendspringboot.entity.constants.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    private String email;
    private String name;
    private String address;
    private String contactNumber;
    private String photoUrl;
    private boolean frozen;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @CreationTimestamp
    private LocalDate createdDate;
    @UpdateTimestamp
    private LocalDate lastModified;

    @OneToMany(mappedBy = "user")
    private List<Cart> cart;
    @OneToMany(mappedBy = "user")
    private List<Product> product;
    @OneToMany(mappedBy = "buyer")
    private List<Order> order;
    @OneToOne(mappedBy = "user")
    private Store store;
    @OneToOne
    private Image image;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Favorites> favorites = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<OrderItem> orderItems = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<RatingAndReview> ratingAndReviews = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<StoreFollower> followedStores = new HashSet<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public String getPassword(){
        return password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !frozen;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !frozen;
    }
}
