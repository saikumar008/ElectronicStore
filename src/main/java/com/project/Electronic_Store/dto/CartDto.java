package com.project.Electronic_Store.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CartDto {

    private UUID cartId;

    private Date createdAt;

    private UserDto user;

    private List<BasketItemDto> Items = new ArrayList<>();

}
