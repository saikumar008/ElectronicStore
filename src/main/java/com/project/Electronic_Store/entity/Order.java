package com.project.Electronic_Store.entity;

import com.project.Electronic_Store.Enum.OrderStatus;
import com.project.Electronic_Store.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="Order_table")
public class Order {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "Order_id")
    private UUID orderId;

    @Column(name="Order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name="Payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "Order_amount")
    private int orderAmount;

    @Column(length = 1000)
    private String billingAddress;

    @Column(name = "Billing_phone")
    private String billingPhone;

    @Column(name = "Billing_name")
    private String billingName;

    @Column(name = "Order_date")
    private Date orderedDate;

    @Column(name = "Delivery_Date")
    private Date deliveredDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "User_id")
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> basketItem = new ArrayList<>();

}
