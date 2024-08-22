package com.project.Electronic_Store.dto;

import com.project.Electronic_Store.entity.Order;
import com.project.Electronic_Store.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private UUID orderItemId;

    private int quantity;

    private int totalPrice;

    private OrderDto order;

    private ProductDto product;

}
