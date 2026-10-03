package com.shashank.ecommerce.coupon.dto.response;

import com.shashank.ecommerce.coupon.enums.DiscountType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CouponResponse(

        Long id,

        String code,

        String description,

        DiscountType discountType,

        BigDecimal discountValue,

        BigDecimal minimumOrderAmount,

        BigDecimal maximumDiscount,

        LocalDateTime startDate,

        LocalDateTime expiryDate,

        Integer usageLimit,

        Integer usedCount,

        Boolean active,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}