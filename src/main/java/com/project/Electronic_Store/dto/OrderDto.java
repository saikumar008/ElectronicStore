package com.project.Electronic_Store.dto;

import com.project.Electronic_Store.Enum.OrderStatus;
import com.project.Electronic_Store.Enum.PaymentStatus;
import com.project.Electronic_Store.entity.OrderItem;
import com.project.Electronic_Store.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private UUID orderId;

    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;

    private int orderAmount;

    private String billingAddress;

    private String billingPhone;

    private String billingName;

    private Date orderedDate;

    private Date deliveredDate;

    private UserDto user;

    private List<OrderItemDto> basketItem = new ArrayList<>();

}
