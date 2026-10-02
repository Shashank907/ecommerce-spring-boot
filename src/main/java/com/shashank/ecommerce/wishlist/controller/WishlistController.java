package com.shashank.ecommerce.wishlist.controller;

import com.shashank.ecommerce.wishlist.dto.WishlistDto;
import com.shashank.ecommerce.wishlist.dto.WishlistItemDto;
import com.shashank.ecommerce.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping
    public ResponseEntity<WishlistDto> createWishlist(
            @PathVariable Long userId) {

        WishlistDto wishlist = wishlistService.createWishlist(userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(wishlist);
    }

    @GetMapping
    public ResponseEntity<WishlistDto> getWishlist(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                wishlistService.getWishlist(userId)
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteWishlist(
            @PathVariable Long userId) {

        wishlistService.deleteWishlist(userId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/items/{productId}")
    public ResponseEntity<WishlistItemDto> addItem(
            @PathVariable Long userId,
            @PathVariable Long productId) {

        WishlistItemDto item = wishlistService.addItem(
                userId,
                productId
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(item);
    }

    @GetMapping("/items")
    public ResponseEntity<List<WishlistItemDto>> getWishlistItems(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                wishlistService.getWishlistItems(userId)
        );
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable Long userId,
            @PathVariable Long productId) {

        wishlistService.removeItem(userId, productId);

        return ResponseEntity.noContent().build();
    }
}