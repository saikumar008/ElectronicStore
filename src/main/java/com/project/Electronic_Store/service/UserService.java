package com.project.Electronic_Store.service;

import com.project.Electronic_Store.dto.UserDto;
import com.project.Electronic_Store.dto.pageableResponse;

import java.util.UUID;

public interface  UserService {


    //Creating user
    UserDto createUser(UserDto userDto);

    //Getting all Users
    pageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

    //Delete User by id
    void deleteUserById(String id);


    //Getting single User by id
    UserDto getSingleUserById(UUID id);

// Update User
    UserDto updateUser(UserDto userDto, String id);

    //Getting single user by email
    UserDto getUserByEmail(String emailId);


    //Search User
//    List<UserResponse> searchUser(String keyWord);
}
