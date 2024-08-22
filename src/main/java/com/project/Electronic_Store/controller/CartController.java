package com.project.Electronic_Store.controller;

import com.project.Electronic_Store.CustomMessageForDelete.ApiResponse;
import com.project.Electronic_Store.dto.CartDto;
import com.project.Electronic_Store.dto.AddItemToCartRequest;
import com.project.Electronic_Store.repository.CategoryRepository;
import com.project.Electronic_Store.service.CartService;
import com.project.Electronic_Store.service.CategoryService;
import com.project.Electronic_Store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cart")///cart/{id}
public class CartController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(
            @PathVariable(value="userId") String userId,
            @RequestBody AddItemToCartRequest request
    ){
        CartDto cartDto = cartService.addItemToCart(userId,request);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}/items/{basketId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable String id, @PathVariable UUID basketId) {
        cartService.removeItemFromCart(id, basketId);
        ApiResponse response = ApiResponse.builder()
                .message("Item is removed !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/allitems/{id}")
    public ResponseEntity<CartDto> getAllItems(
            @PathVariable String id
    ){
        CartDto cartDto = cartService.getCartByUser(id);
        return new ResponseEntity<>(cartDto, HttpStatus.FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable String id) {
        cartService.clearCart(id);
        ApiResponse response = ApiResponse.builder()
                .message("Now cart is blank !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
