package com.shashank.ecommerce.coupon.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CouponApplyResponse(

        String couponCode,

        BigDecimal orderAmount,

        BigDecimal discountAmount,

        BigDecimal finalAmount,

        String message
) {
}