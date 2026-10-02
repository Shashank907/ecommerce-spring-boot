package com.shashank.ecommerce.cart.service;

import com.shashank.ecommerce.cart.dto.AddCartItemRequestDto;
import com.shashank.ecommerce.cart.dto.CartItemDto;

import java.util.List;

public interface CartItemService {

    CartItemDto addItem(Long userId, AddCartItemRequestDto request);

    List<CartItemDto> getCartItems(Long userId);

    void removeItem(Long userId, Long itemId);
}