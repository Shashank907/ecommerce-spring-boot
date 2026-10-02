package com.shashank.ecommerce.cart.controller;

import com.shashank.ecommerce.cart.dto.AddCartItemRequestDto;
import com.shashank.ecommerce.cart.dto.CartItemDto;
import com.shashank.ecommerce.cart.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/cart/items")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<CartItemDto> addItem(
            @PathVariable Long userId,
            @RequestBody @Valid AddCartItemRequestDto request) {

        CartItemDto cartItem =
                cartItemService.addItem(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartItem);
    }

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCartItems(
            @PathVariable Long userId) {

        List<CartItemDto> items =
                cartItemService.getCartItems(userId);

        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable Long userId,
            @PathVariable Long itemId) {

        cartItemService.removeItem(userId, itemId);

        return ResponseEntity.noContent().build();
    }
}