package com.project.Electronic_Store.service.serviceimpl;

import com.project.Electronic_Store.ExceptionHandling.ResourceNotFoundException;
import com.project.Electronic_Store.dto.CategoryDto;
import com.project.Electronic_Store.dto.ProductDto;
import com.project.Electronic_Store.dto.pageableResponse;
import com.project.Electronic_Store.entity.Category;
import com.project.Electronic_Store.entity.Product;
import com.project.Electronic_Store.helper.Helper;
import com.project.Electronic_Store.repository.CategoryRepository;
import com.project.Electronic_Store.repository.ProductRepository;
import com.project.Electronic_Store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageImpl;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepo){
        this.productRepo = productRepo;

    }
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = new Product();

        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setProductImage(productDto.getProductImage());
        product.setLive(productDto.isLive());
        product.setAddedDate(new Date());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.isStock());
        product.setQuantity(productDto.getQuantity());
        Product saveProduct = productRepo.save(product);

        ProductDto pDto = new ProductDto();

        pDto.setTitle(saveProduct.getTitle());
        pDto.setDescription(saveProduct.getDescription());
        pDto.setProductImage(saveProduct.getProductImage());
        pDto.setLive(saveProduct.isLive());
        pDto.setAddedDate(saveProduct.getAddedDate());
        pDto.setDiscountedPrice(saveProduct.getDiscountedPrice());
        pDto.setPrice(saveProduct.getPrice());
        pDto.setStock(saveProduct.isStock());
        pDto.setQuantity(saveProduct.getQuantity());

        return pDto;
    }

    @Override
    public ProductDto UpdateProduct(ProductDto productDto, String Id) {
        UUID uuid=UUID.fromString(Id);
        Product product = productRepo.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));

        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setProductImage(productDto.getProductImage());
        product.setLive(productDto.isLive());
        product.setAddedDate(productDto.getAddedDate());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.isStock());
        product.setQuantity(productDto.getQuantity());
        Product sProduct = productRepo.save(product);


        Product UpdateProduct = productRepo.save(sProduct);

        ProductDto productRes = entityToDto(UpdateProduct);
        return productRes;
    }

    @Override
    public void DeleteProductById(String Id) {
        java.util.UUID uuid= java.util.UUID.fromString(Id);
        Product product = productRepo.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        productRepo.deleteById(product.getId());
    }

    @Override
    public pageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc"))? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);

        Page<Product> page = productRepo.findAll(pageable);

        pageableResponse<ProductDto> response= Helper.getPageableResponse(page, ProductDto.class);

        return response;
    }

    @Override
    public pageableResponse<ProductDto> getAllProductLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc"))? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);

        Page<Product> page = productRepo.findByLiveTrue(pageable);

        pageableResponse<ProductDto> response= Helper.getPageableResponse(page, ProductDto.class);

        return response;
    }

    @Override
    public ProductDto getSingleProductById(UUID Id) {
        Product product = productRepo.findById(UUID.fromString(String.valueOf(Id))).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return entityToDto(product);
    }

    @Override
    public pageableResponse<ProductDto> searchByProductTitle(String subTitle,int pageNumber, int pageSize, String sortBy, String sortDir) {

            Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

            List<Product> productList = productRepo.findAll()
                    .stream()
                    .filter(product -> product.getTitle().contains(subTitle))
                    .sorted((p1, p2) -> sortDir.equalsIgnoreCase("asc") ? p1.getTitle().compareTo(p2.getTitle()) : p2.getTitle().compareTo(p1.getTitle()))
                    .collect(Collectors.toList());

            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), productList.size());

            List<Product> paginatedList = productList.subList(start, end);
            Page<Product> page = new PageImpl<>(paginatedList, pageable, productList.size());

            pageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);

            return response;
        }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {

        // fetch the category from db:
        Category category = categoryRepository.findById(UUID.fromString(categoryId)).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Product product = new Product();
        product.setId(productDto.getId());
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setProductImage(productDto.getProductImage());
        product.setLive(productDto.isLive());
        product.setAddedDate(new Date());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.isStock());
        product.setQuantity(productDto.getQuantity());
        product.setCategory(category);
        Product saveProduct = productRepo.save(product);

        ProductDto pDto = new ProductDto();
        pDto.setId(saveProduct.getId());
        pDto.setTitle(saveProduct.getTitle());
        pDto.setDescription(saveProduct.getDescription());
        pDto.setProductImage(saveProduct.getProductImage());
        pDto.setLive(saveProduct.isLive());
        pDto.setAddedDate(saveProduct.getAddedDate());
        pDto.setDiscountedPrice(saveProduct.getDiscountedPrice());
        pDto.setPrice(saveProduct.getPrice());
        pDto.setStock(saveProduct.isStock());
        pDto.setQuantity(saveProduct.getQuantity());
        pDto.setCategory(mapToCategoryDto(saveProduct.getCategory()));

        return pDto;
    }

    @Override
    public ProductDto updateCategory(String Id, String categoryId) {

       Product product = productRepo.findById(UUID.fromString(Id)).orElseThrow(() -> new ResourceNotFoundException("ProductId not found"));
       Category category = categoryRepository.findById(UUID.fromString(categoryId)).orElseThrow(() -> new ResourceNotFoundException("CategoryId not found"));
       product.setCategory(category);
       Product saveProduct = productRepo.save(product);

        ProductDto pDto = new ProductDto();
        pDto.setId(saveProduct.getId());
        pDto.setTitle(saveProduct.getTitle());
        pDto.setDescription(saveProduct.getDescription());
        pDto.setProductImage(saveProduct.getProductImage());
        pDto.setLive(saveProduct.isLive());
        pDto.setAddedDate(saveProduct.getAddedDate());
        pDto.setDiscountedPrice(saveProduct.getDiscountedPrice());
        pDto.setPrice(saveProduct.getPrice());
        pDto.setStock(saveProduct.isStock());
        pDto.setQuantity(saveProduct.getQuantity());
        pDto.setCategory(mapToCategoryDto(saveProduct.getCategory()));

        return pDto;
    }

    @Override
    public pageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageNumber, int pageSize, String sortBy, String sortDir) {
        Category category = categoryRepository.findById(UUID.fromString(categoryId)).orElseThrow(() -> new ResourceNotFoundException("CategoryId not found"));
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> product = productRepo.findByCategory(category,pageable);

        return Helper.getPageableResponse(product,ProductDto.class);

    }


    private CategoryDto mapToCategoryDto(Category category) {

        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setCategoryId(category.getCategoryId());
        categoryDto.setDescription(category.getDescription());
        categoryDto.setTitle(category.getTitle());

        return categoryDto;
    }


    private Product DtoToEntity(Product productDto){
        Product product = new Product();

        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setProductImage(productDto.getProductImage());
        product.setLive(productDto.isLive());
        product.setAddedDate(productDto.getAddedDate());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.isStock());
        product.setQuantity(productDto.getQuantity());

        return product;
    }

    //Entity to response
    private ProductDto entityToDto(Product saveProduct){

        ProductDto pDto = new ProductDto();

        pDto.setTitle(saveProduct.getTitle());
        pDto.setDescription(saveProduct.getDescription());
        pDto.setProductImage(saveProduct.getProductImage());
        pDto.setLive(saveProduct.isLive());
        pDto.setAddedDate(saveProduct.getAddedDate());
        pDto.setDiscountedPrice(saveProduct.getDiscountedPrice());
        pDto.setPrice(saveProduct.getPrice());
        pDto.setStock(saveProduct.isStock());
        pDto.setQuantity(saveProduct.getQuantity());

        return pDto;
    }

    private CategoryDto entityToDto(Category saveCategory){

        CategoryDto cDto = new CategoryDto();

        cDto.setDescription(saveCategory.getDescription());
        cDto.setTitle(saveCategory.getTitle());
        cDto.setCoverImage(saveCategory.getCoverImage());

        return cDto;

    }
}
