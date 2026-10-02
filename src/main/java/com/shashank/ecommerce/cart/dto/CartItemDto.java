package com.shashank.ecommerce.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

    private Long id;

    private Long cartId;

    private Long productId;

    private String productName;

    private Double productPrice;

    private Integer quantity;

    private Double subtotal;
}