package com.project.Electronic_Store.service;

import com.project.Electronic_Store.dto.CategoryDto;
import com.project.Electronic_Store.dto.ProductDto;
import com.project.Electronic_Store.dto.pageableResponse;
import com.project.Electronic_Store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface ProductService {
    ProductDto createProduct(ProductDto productDto);

    //Update Category
    ProductDto UpdateProduct(ProductDto productDto,String categoryId);


    //Delete Category
    void DeleteProductById(String productId);

    pageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir);

    pageableResponse<ProductDto> getAllProductLive(int pageNumber, int pageSize, String sortBy, String sortDir);

    ProductDto getSingleProductById(UUID productId);

    pageableResponse<ProductDto> searchByProductTitle(String subTitle,int pageNumber, int pageSize, String sortBy, String sortDir);

    //Create product with category
    ProductDto createWithCategory(ProductDto productDto, String categoryId);

    ProductDto updateCategory(String productId,String categoryId);

    pageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageNumber, int pageSize, String sortBy, String sortDir);
}
