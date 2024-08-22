package com.project.Electronic_Store.repository;

import com.project.Electronic_Store.entity.Cart;
import com.project.Electronic_Store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    Optional<Cart> findByUser(User user);

}
