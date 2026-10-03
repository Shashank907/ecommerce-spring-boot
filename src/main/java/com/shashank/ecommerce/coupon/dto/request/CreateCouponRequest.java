package com.shashank.ecommerce.coupon.dto.request;

import com.shashank.ecommerce.coupon.enums.DiscountType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateCouponRequest(

        @NotBlank(message = "Coupon code is required")
        @Size(max = 50, message = "Coupon code cannot exceed 50 characters")
        String code,

        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description,

        @NotNull(message = "Discount type is required")
        DiscountType discountType,

        @NotNull(message = "Discount value is required")
        @DecimalMin(
                value = "0.01",
                message = "Discount value must be greater than 0"
        )
        BigDecimal discountValue,

        @DecimalMin(
                value = "0.00",
                message = "Minimum order amount cannot be negative"
        )
        BigDecimal minimumOrderAmount,

        @DecimalMin(
                value = "0.00",
                message = "Maximum discount cannot be negative"
        )
        BigDecimal maximumDiscount,

        @NotNull(message = "Start date is required")
        LocalDateTime startDate,

        @NotNull(message = "Expiry date is required")
        LocalDateTime expiryDate,

        @Min(
                value = 1,
                message = "Usage limit must be at least 1"
        )
        Integer usageLimit
) {
}