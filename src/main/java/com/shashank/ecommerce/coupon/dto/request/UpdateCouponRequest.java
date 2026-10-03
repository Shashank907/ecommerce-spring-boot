package com.shashank.ecommerce.coupon.dto.request;

import com.shashank.ecommerce.coupon.enums.DiscountType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateCouponRequest(

        @NotBlank
        @Size(max = 50)
        String code,

        @Size(max = 255)
        String description,

        @NotNull
        DiscountType discountType,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal discountValue,

        @DecimalMin("0.00")
        BigDecimal minimumOrderAmount,

        @DecimalMin("0.00")
        BigDecimal maximumDiscount,

        @NotNull
        LocalDateTime startDate,

        @NotNull
        LocalDateTime expiryDate,

        @Min(1)
        Integer usageLimit,

        @NotNull
        Boolean active
) {
}