package com.shashank.ecommerce.coupon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ApplyCouponRequest(

        @NotBlank(message = "Coupon code is required")
        String code,

        @NotNull(message = "Order amount is required")
        BigDecimal orderAmount
) {
}