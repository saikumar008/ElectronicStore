package com.project.Electronic_Store.service;

import com.project.Electronic_Store.dto.CategoryDto;
import com.project.Electronic_Store.dto.UserDto;
import com.project.Electronic_Store.dto.pageableResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface CategoryService {

    //Create Category

    CategoryDto createCategory(CategoryDto categoryDto);

    //Update Category
    CategoryDto UpdateCategory(CategoryDto categoryDto,String categoryId);

    //Delete Category
    void DeleteCategoryById(String categoryId);

    pageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir);

    CategoryDto getSingleCategoryById(UUID categoryId);
}
