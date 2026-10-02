package com.shashank.ecommerce.cart.service;

import com.shashank.ecommerce.cart.dto.CartDto;

public interface CartService {

    CartDto createCart(Long userId);

    CartDto getCartByUserId(Long userId);

    void deleteCart(Long userId);
}