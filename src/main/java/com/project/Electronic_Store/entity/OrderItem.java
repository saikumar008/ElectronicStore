package com.project.Electronic_Store.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="OrderItem_table")
public class OrderItem {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "OrderItemId_id")
    private UUID orderItemId;

    @Column(name = "Order_quantity")
    private int quantity;

    @Column(name = "Order_totalprice")
    private int totalPrice;

    @ManyToOne
    private Order order;

    @OneToOne
    private Product product;

}
