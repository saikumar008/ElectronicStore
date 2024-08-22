package com.project.Electronic_Store.repository;

import com.project.Electronic_Store.entity.Category;
import com.project.Electronic_Store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    //Search
    Page<Product> findByTitle(Pageable pageable,String subTitle);

    Page<Product> findByLiveTrue(Pageable pageable);

    Page<Product> findByCategory(Category category, Pageable pageable);
}
