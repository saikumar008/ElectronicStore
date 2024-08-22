package com.project.Electronic_Store.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name="Basket_item")
public class BasketItem {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name =" Basket_id")
    private UUID basketId;

    private int quantity;

    private int totalAmount;

    @OneToOne
    @JoinColumn(name="Prod_Id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Cartid")
    private Cart cart;
    

}
