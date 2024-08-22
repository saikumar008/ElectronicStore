package com.project.Electronic_Store.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="Products_table")
public class Product {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "Product_id")
    private UUID Id;

    @Column(name="product_Title")
    private String title;

    @Column(name="product_Description",length=10000)
    private String description;

    @Column(name="product_Price")
    private int price;

    @Column(name="product_DiscountedPrice")
    private int discountedPrice;

    @Column(name="product_quantity")
    private int quantity;

    @Column(name="product_AddedDate")
    private Date addedDate;

    @Column(name="product_Live")
    private boolean live;

    @Column(name="product_Stock")
    private boolean stock;

    @Column(name="product_ProductImage")
    private String productImage;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(mappedBy = "product", fetch = FetchType.EAGER)
    private BasketItem basketItem;
}
