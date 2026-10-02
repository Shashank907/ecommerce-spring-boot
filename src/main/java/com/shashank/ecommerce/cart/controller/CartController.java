package com.shashank.ecommerce.cart.controller;

import com.shashank.ecommerce.cart.dto.CartDto;
import com.shashank.ecommerce.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            @PathVariable Long userId) {

        CartDto createdCart =
                cartService.createCart(userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdCart);
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart(
            @PathVariable Long userId) {

        CartDto cart =
                cartService.getCartByUserId(userId);

        return ResponseEntity.ok(cart);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCart(
            @PathVariable Long userId) {

        cartService.deleteCart(userId);

        return ResponseEntity.noContent().build();
    }
}