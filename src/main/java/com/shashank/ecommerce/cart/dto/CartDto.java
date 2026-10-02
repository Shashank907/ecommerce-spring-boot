package com.shashank.ecommerce.cart.dto;

import com.shashank.ecommerce.cart.entity.CartStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private Long id;

    private Long userId;

    private CartStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}