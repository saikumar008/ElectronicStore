package com.project.Electronic_Store.service.serviceimpl;

import com.project.Electronic_Store.ExceptionHandling.ResourceNotFoundException;
import com.project.Electronic_Store.dto.CategoryDto;
import com.project.Electronic_Store.dto.pageableResponse;
import com.project.Electronic_Store.entity.Category;
import com.project.Electronic_Store.helper.Helper;
import com.project.Electronic_Store.repository.CategoryRepository;
import com.project.Electronic_Store.repository.UserRepository;
import com.project.Electronic_Store.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepo;

    public CategoryServiceImpl(CategoryRepository categoryRepo){
        this.categoryRepo = categoryRepo;
    }
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = new Category();

        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(category.getCoverImage());
        Category saveCategory = categoryRepo.save(category);

        CategoryDto cDto = new CategoryDto();

        cDto.setDescription(saveCategory.getDescription());
        cDto.setTitle(saveCategory.getTitle());
        cDto.setCoverImage(saveCategory.getCoverImage());

        return cDto;
    }

    @Override
    public CategoryDto UpdateCategory(CategoryDto categoryDto, String categoryId) {
        UUID uuid=UUID.fromString(categoryId);
        Category category = categoryRepo.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));

        category.setDescription(categoryDto.getDescription());
        category.setTitle(categoryDto.getTitle());
        category.setCoverImage(categoryDto.getCoverImage());

        Category updatedCategory = categoryRepo.save(category);

        CategoryDto categoryRes = entityToDto(updatedCategory);

        return categoryRes;

    }

    @Override
    public void DeleteCategoryById(String categoryId) {
        java.util.UUID uuid= java.util.UUID.fromString(categoryId);
        Category category = categoryRepo.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        categoryRepo.deleteById(category.getCategoryId());
    }

    @Override
    public pageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc"))? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);

        Page<Category> page = categoryRepo.findAll(pageable);

        pageableResponse<CategoryDto> response= Helper.getPageableResponse(page, CategoryDto.class);

        return response;
    }

    @Override
    public CategoryDto getSingleCategoryById(UUID categoryId) {
        Category category = categoryRepo.findById(UUID.fromString(String.valueOf(categoryId))).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return entityToDto(category);
    }

    private Category DtoToEntity(CategoryDto categoryDto){
        Category category = new Category();

        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(category.getCoverImage());

        return category;
    }

    //Entity to response
    private CategoryDto entityToDto(Category saveCategory){

        CategoryDto cDto = new CategoryDto();

        cDto.setDescription(saveCategory.getDescription());
        cDto.setTitle(saveCategory.getTitle());
        cDto.setCoverImage(saveCategory.getCoverImage());

        return cDto;

    }
}
