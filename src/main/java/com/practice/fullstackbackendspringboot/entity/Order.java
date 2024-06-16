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
@Table(name = "orders")
public class Order extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderId;
    private String orderStatus;
    private String orderStatusInfo;
    private Double orderTotalAmount;
    private String paymentMethod;
    private boolean active;
    private String deliveryAddress;
    private String fullName;
    private String contactNumber;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
}
