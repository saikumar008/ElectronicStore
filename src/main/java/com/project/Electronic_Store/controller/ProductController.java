package com.project.Electronic_Store.controller;

import com.project.Electronic_Store.CustomMessageForDelete.ApiResponse;
import com.project.Electronic_Store.UploadService.fileService;
import com.project.Electronic_Store.dto.*;
import com.project.Electronic_Store.repository.ProductRepository;
import com.project.Electronic_Store.service.ProductService;
import jakarta.servlet.ServletOutputStream;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/Products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private fileService fileservice;

    @Value("${product.image.path}")
    private String imageUploadPath;

    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    @PostMapping("/addproduct")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
        return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.CREATED);
    }

    @GetMapping("/allproducts")
    public ResponseEntity<pageableResponse<ProductDto>> getAllProduct(
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value="pageSize", defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "price",required = false) String sortBy,
            @RequestParam(value="sortDir", defaultValue = "asc",required = false) String sortDir
    ){
        return new ResponseEntity<>(productService.getAllProduct(pageNumber, pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    @GetMapping("/Live")
    public ResponseEntity<pageableResponse<ProductDto>> getAllProductLive(
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value="pageSize", defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value="sortDir", defaultValue = "asc",required = false) String sortDir
    ){
        pageableResponse<ProductDto> pageableResponse = productService.getAllProductLive(pageNumber, pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<pageableResponse<ProductDto>> searchByProductTitle(
            @PathVariable String query,
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value="pageSize", defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value="sortDir", defaultValue = "asc",required = false) String sortDir
    ){
        pageableResponse<ProductDto> pageableResponse = productService.searchByProductTitle(query,pageNumber, pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<ApiResponse> DeleteProductById(@PathVariable("Id") String Id){
        //  uuid = UUID.fromString(String.valueOf(Id));

        productService.DeleteProductById(Id);

        ApiResponse message = ApiResponse.builder()
                .message("Product is deleted")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @PutMapping("/update/{Id}")
    public ResponseEntity<ProductDto> UpdateCategory(@RequestBody ProductDto productDto,@PathVariable("Id") String Id){
        ProductDto  productRes = productService.UpdateProduct(productDto,Id);
        return new ResponseEntity<>(productRes,HttpStatus.OK);
    }

    @PostMapping("/image/{Id}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam MultipartFile image,@PathVariable("Id") String Id) throws IOException {
//        UUID uuid=UUID.fromString(id);

        String imageName = fileservice.uploadFile(image, imageUploadPath);

        ProductDto userDto = productService.getSingleProductById(UUID.fromString(Id));
        userDto.setProductImage(imageName);
        productService.UpdateProduct(userDto,Id);

        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();

        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }
    @GetMapping("/image/{Id}")
    public void serveUserImage(@PathVariable("Id") String Id, HttpServletResponse response) throws IOException {

        ProductDto user = productService.getSingleProductById(UUID.fromString(Id));
        logger.info("User image name : {}",user.getProductImage());
        InputStream resourse = fileservice.getResource(imageUploadPath,user.getProductImage());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resourse,response.getOutputStream());

    }




}



