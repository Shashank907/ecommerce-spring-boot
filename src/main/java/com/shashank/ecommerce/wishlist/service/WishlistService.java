package com.shashank.ecommerce.wishlist.service;

import com.shashank.ecommerce.wishlist.dto.WishlistDto;
import com.shashank.ecommerce.wishlist.dto.WishlistItemDto;

import java.util.List;

public interface WishlistService {

    WishlistDto createWishlist(Long userId);

    WishlistDto getWishlist(Long userId);

    void deleteWishlist(Long userId);

    WishlistItemDto addItem(
            Long userId,
            Long productId
    );

    List<WishlistItemDto> getWishlistItems(Long userId);

    void removeItem(
            Long userId,
            Long productId
    );
}