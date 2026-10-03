package com.shashank.ecommerce.order.dto;

import jakarta.validation.constraints.Size;

public record CreateOrderRequest(

        @Size(
                max = 50,
                message = "Coupon code cannot exceed 50 characters"
        )
        String couponCode

) {
}