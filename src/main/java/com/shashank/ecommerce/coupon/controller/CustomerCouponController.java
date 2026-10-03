package com.shashank.ecommerce.coupon.controller;

import com.shashank.ecommerce.coupon.dto.request.ApplyCouponRequest;
import com.shashank.ecommerce.coupon.dto.response.CouponApplyResponse;
import com.shashank.ecommerce.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CustomerCouponController {

    private final CouponService couponService;

    // =========================
    // APPLY COUPON
    // =========================

    @PostMapping("/apply")
    public ResponseEntity<CouponApplyResponse> applyCoupon(
            @Valid @RequestBody ApplyCouponRequest request) {

        return ResponseEntity.ok(
                couponService.applyCoupon(request)
        );
    }
}