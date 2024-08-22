package com.project.Electronic_Store.service.serviceimpl;

import com.project.Electronic_Store.CustomMessageForDelete.BadApiRequestException;
import com.project.Electronic_Store.dto.OrderDto;
import com.project.Electronic_Store.entity.*;
import com.project.Electronic_Store.repository.CartRepository;
import com.project.Electronic_Store.repository.OrderRepository;
import com.project.Electronic_Store.repository.UserRepository;
import com.project.Electronic_Store.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ModelMapper modelMapper;

    private OrderServiceImpl(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public OrderDto createOrder(UUID userId, UUID cartId, OrderDto orderRequest) {

        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));

        List<BasketItem> cartItem = cart.getItems();

        if(cartItem.size() <= 0){
            throw new BadApiRequestException("no items in the cart");
        }

        Order order = Order.builder()
                .orderAmount(orderRequest.getOrderAmount())
                .orderId(orderRequest.getOrderId())
                .billingName(orderRequest.getBillingName())
                .billingAddress(orderRequest.getBillingAddress())
                .billingPhone(orderRequest.getBillingPhone())
                .user(user)
                .build();

        AtomicReference<Integer> amount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItem.stream().map(cartItems -> {

            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItems.getQuantity())
                    .product(cartItems.getProduct())
                    .totalPrice(cartItems.getQuantity() * cartItems.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();

            amount.set(amount.get() * orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setBasketItem(orderItems);
        order.setOrderAmount(amount.get());

        cart.getItems().clear();
        cartRepo.save(cart);
        Order saveOrder = orderRepo.save(order);

        return modelMapper.map(saveOrder, OrderDto.class);

    }

    @Override
    public void removeOrder(UUID orderId) {

        Order order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Id not found"));

        orderRepo.delete(order);
    }

    @Override
    public List<OrderDto> getAllOrders(UUID userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("ID not found"));
        List<Order> orders = orderRepo.findByUser(userId);

        List<OrderDto> orderDtos = orders.stream().map(order -> modelMapper.map(order,OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }
}
