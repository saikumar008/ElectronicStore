package com.project.Electronic_Store.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {

    public String imageName;

    public String message;
    public boolean success;
    public HttpStatus status;

}
