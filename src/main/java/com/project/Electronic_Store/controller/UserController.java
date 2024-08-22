package com.project.Electronic_Store.controller;

import com.project.Electronic_Store.CustomMessageForDelete.ApiResponse;
import com.project.Electronic_Store.UploadService.fileService;
import com.project.Electronic_Store.dto.*;
import com.project.Electronic_Store.repository.UserRepository;
import com.project.Electronic_Store.service.UserService;
//import org.apache.coyote.Response;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/users")///users/allusers
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private fileService fileservice;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/adduser")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping("{Id}")
    public ResponseEntity<UserDto> getSingleUserById(@PathVariable("Id") String id){
//        UUID uuid;
//            uuid = UUID.fromString(String.valueOf(id));

//        return new ResponseEntity<>(userService.getSingleUserById(UUID.fromString(id)),HttpStatus.FOUND);

        UUID uuid = UUID.fromString(id);
        UserDto userDto = userService.getSingleUserById(uuid);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @DeleteMapping("{Id}")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable("Id") String id){
      //  uuid = UUID.fromString(String.valueOf(Id));

        userService.deleteUserById(id);

        ApiResponse message = ApiResponse.builder()
                .message("User is deleted")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String emailId){

        UserDto userDto;
        try {
            userDto = userService.getUserByEmail(emailId);
        } catch (RuntimeException e) {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto,@PathVariable("id") String id){
        UserDto userRes = userService.updateUser(userDto,id);
        return new ResponseEntity<>(userRes,HttpStatus.OK);
    }
    @GetMapping("/allusers")
    public ResponseEntity<pageableResponse<UserDto>> getAllUsers(
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value="pageSize", defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "userName",required = false) String sortBy,
            @RequestParam(value="sortDir", defaultValue = "asc",required = false) String sortDir
    ){
        return new ResponseEntity<>(userService.getAllUsers(pageNumber, pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam MultipartFile image,@PathVariable("id") String id) throws IOException {
//        UUID uuid=UUID.fromString(id);

        String imageName = fileservice.uploadFile(image, imageUploadPath);

        UserDto userDto = userService.getSingleUserById(UUID.fromString(id));
        userDto.setUserImage(imageName);
        userService.updateUser(userDto,id);

        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();

        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable(value="userId") String id, HttpServletResponse response) throws IOException {

        UserDto user = userService.getSingleUserById(UUID.fromString(id));
        logger.info("User image name : {}",user.getUserImage());
        InputStream resourse = fileservice.getResource(imageUploadPath,user.getUserImage());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resourse,response.getOutputStream());

    }
}

