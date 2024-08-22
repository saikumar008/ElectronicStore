package com.project.Electronic_Store.controller;

import com.project.Electronic_Store.CustomMessageForDelete.ApiResponse;
import com.project.Electronic_Store.UploadService.fileService;
import com.project.Electronic_Store.dto.*;
import com.project.Electronic_Store.repository.CategoryRepository;
import com.project.Electronic_Store.service.CategoryService;
import com.project.Electronic_Store.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private fileService fileservice;

    @Value("${category.profile.image.path}")
    private String imageUploadPath;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/addcategory")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){
        return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.CREATED);
    }

    @GetMapping("/allcategories")
    public ResponseEntity<pageableResponse<CategoryDto>> getAllCategory(
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value="pageSize", defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value="sortDir", defaultValue = "asc",required = false) String sortDir
    ){
        return new ResponseEntity<>(categoryService.getAllCategory(pageNumber, pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<ApiResponse> DeleteCategoryById(@PathVariable("categoryId") String categoryId){
        //  uuid = UUID.fromString(String.valueOf(Id));

        categoryService.DeleteCategoryById(categoryId);

        ApiResponse message = ApiResponse.builder()
                .message("User is deleted")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<pageableResponse<ProductDto>> searchByProductTitle(
            @PathVariable String query,
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value="pageSize", defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "live",required = false) String sortBy,
            @RequestParam(value="sortDir", defaultValue = "asc",required = false) String sortDir
    ){
        pageableResponse<ProductDto> pageableResponse = productService.searchByProductTitle(query,pageNumber, pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> UpdateCategory(@RequestBody CategoryDto categoryDto,@PathVariable("categoryId") String categoryId){
        CategoryDto  categoryRes = categoryService.UpdateCategory(categoryDto,categoryId);
        return new ResponseEntity<>(categoryRes,HttpStatus.OK);
    }

    @PostMapping("/coverImage/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(@RequestParam("coverImage") MultipartFile coverImage, @PathVariable("categoryId") String categoryId) throws IOException {
//        UUID uuid=UUID.fromString(id);

        String imageName = fileservice.uploadFile(coverImage, imageUploadPath);

        CategoryDto categoryDto = categoryService.getSingleCategoryById(UUID.fromString(categoryId));
        categoryDto.setCoverImage(imageName);
        categoryService.UpdateCategory(categoryDto,categoryId);

        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();

        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    @GetMapping("/image/{categoryId}")
    public void serveUserImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {

        CategoryDto category = categoryService.getSingleCategoryById(UUID.fromString(categoryId));
        logger.info("User image name : {}",category.getCoverImage());
        InputStream resourse = fileservice.getResource(imageUploadPath,category.getCoverImage());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resourse,response.getOutputStream());

    }

    @GetMapping("{categoryId}")
    public ResponseEntity<CategoryDto> getSingleCategoryById(@PathVariable("categoryId") String categoryId){
//        UUID uuid;
//            uuid = UUID.fromString(String.valueOf(id));

//        return new ResponseEntity<>(userService.getSingleUserById(UUID.fromString(id)),HttpStatus.FOUND);

        UUID uuid = UUID.fromString(categoryId);
        CategoryDto categoryDto = categoryService.getSingleCategoryById(uuid);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    //create product category
    @PostMapping("/{categoryId}/Products")
    public ResponseEntity<ProductDto> createProductWithCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestBody ProductDto productDto
    ){
       ProductDto productWithCategory = productService.createWithCategory(productDto, categoryId);
        return new ResponseEntity<>(productWithCategory, HttpStatus.CREATED);
    }

    // Update category of product
    @PutMapping("/{categoryId}/products/{Id}")
    public ResponseEntity<ProductDto> updateCategory(
            @PathVariable String categoryId,
            @PathVariable String Id
    ){
        ProductDto productDto = productService.updateCategory(Id,categoryId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }
    @GetMapping("{categoryId}/products")
    public ResponseEntity<pageableResponse<ProductDto>> getAllOfCategory(
            @PathVariable String categoryId,
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value="pageSize", defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "live",required = false) String sortBy,
            @RequestParam(value="sortDir", defaultValue = "asc",required = false) String sortDir){

        pageableResponse<ProductDto> response = productService.getAllOfCategory(categoryId,pageNumber, pageSize,sortBy,sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
