package com.shashank.ecommerce.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistItemDto {

    private Long id;
    private Long wishlistId;
    private Long productId;
    private String productName;
    private Double productPrice;
    private String imageUrl;
    private LocalDateTime createdAt;
}