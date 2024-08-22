package com.project.Electronic_Store.service.serviceimpl;

import com.project.Electronic_Store.CustomMessageForDelete.BadApiRequestException;
import com.project.Electronic_Store.ExceptionHandling.ResourceNotFoundException;
import com.project.Electronic_Store.dto.*;
import com.project.Electronic_Store.entity.*;
import com.project.Electronic_Store.repository.BasketItemRepository;
import com.project.Electronic_Store.repository.CartRepository;
import com.project.Electronic_Store.repository.ProductRepository;
import com.project.Electronic_Store.repository.UserRepository;
import com.project.Electronic_Store.service.CartService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private BasketItemRepository basketItemRepository;

    @Autowired
    private CartRepository cartRepository;
    public CartServiceImpl(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    @Autowired
    private ModelMapper mapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        int quantity = request.getQuantity();
        String productId = request.getProductId();

        if (quantity <= 0) {
            throw new BadApiRequestException("Requested quantity is not valid !!");
        }

        //fetch the product
        Product product = productRepository.findById(UUID.fromString(productId)).orElseThrow(() -> new ResourceNotFoundException("Product not found in database !!"));
        //fetch the user from db
        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));

        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.fromString(UUID.randomUUID().toString()));
            cart.setCreatedAt(new Date());
        }

        //perform cart operations
        //if cart items already present; then update
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<BasketItem> items = cart.getItems();
        items = items.stream().map(item -> {

            if (item.getProduct().getId().equals(productId)) {
                //item already present in cart
                item.setQuantity(quantity);
                item.setTotalAmount(quantity * product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

//        cart.setItems(updatedItems);

        //create items
        if (!updated.get()) {
            BasketItem cartItem = BasketItem.builder()
                    .quantity(quantity)
                    .totalAmount(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
        }


        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String id, UUID basketId) {

        BasketItem cartItem1 = basketItemRepository.findById(basketId).orElseThrow(() -> new ResourceNotFoundException("Cart Item not found !!"));
        basketItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String id) {
        //fetch the user from db
        User user = userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String id) {
        User user = userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));

        return mapper.map(cart, CartDto.class);
    }
}
