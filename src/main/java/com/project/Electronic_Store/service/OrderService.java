package com.project.Electronic_Store.service;

import com.project.Electronic_Store.dto.OrderDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderDto createOrder(UUID userId, UUID cartId, OrderDto orderRequest);

    void removeOrder(UUID orderId);

    List<OrderDto> getAllOrders(UUID userId);
}
