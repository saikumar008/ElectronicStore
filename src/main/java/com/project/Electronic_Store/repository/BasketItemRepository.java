package com.project.Electronic_Store.repository;

import com.project.Electronic_Store.entity.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BasketItemRepository extends JpaRepository<BasketItem, UUID> {
}
