package com.project.Electronic_Store.dto;

import com.project.Electronic_Store.entity.Cart;
import com.project.Electronic_Store.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BasketItemDto {


    private UUID basketId;

    private int quantity;

    private int totalAmount;

    private ProductDto product;

}
