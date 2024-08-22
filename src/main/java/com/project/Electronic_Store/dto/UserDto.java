package com.project.Electronic_Store.dto;

import com.project.Electronic_Store.Enum.Gender;
import com.project.Electronic_Store.validate.ImageNameValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID id;

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "Email not valid")
    @NotBlank(message = "Email is required")
    private String emailId;

    private String userName;

    private String password;

    private Gender gender;

    private String UserImage;

    private String about;

}
