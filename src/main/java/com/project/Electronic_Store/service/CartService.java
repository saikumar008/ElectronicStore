package com.project.Electronic_Store.service;

import com.project.Electronic_Store.dto.CartDto;
import com.project.Electronic_Store.dto.AddItemToCartRequest;

import java.util.UUID;

public interface CartService {

    CartDto addItemToCart(String userId, AddItemToCartRequest request );

    void removeItemFromCart( String id, UUID basketId);

    void clearCart(String id);

//    CartDto getAllItems(String id);

    CartDto getCartByUser(String id);
}
