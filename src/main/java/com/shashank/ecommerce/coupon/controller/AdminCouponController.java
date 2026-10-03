package com.shashank.ecommerce.coupon.controller;

import com.shashank.ecommerce.coupon.dto.request.CreateCouponRequest;
import com.shashank.ecommerce.coupon.dto.request.UpdateCouponRequest;
import com.shashank.ecommerce.coupon.dto.response.CouponResponse;
import com.shashank.ecommerce.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/coupons")
@RequiredArgsConstructor
public class AdminCouponController {

    private final CouponService couponService;

    // =========================
    // CREATE COUPON
    // =========================

    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(
            @Valid @RequestBody CreateCouponRequest request) {

        CouponResponse response =
                couponService.createCoupon(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================
    // GET ALL COUPONS
    // =========================

    @GetMapping
    public ResponseEntity<List<CouponResponse>> getAllCoupons() {

        return ResponseEntity.ok(
                couponService.getAllCoupons()
        );
    }

    // =========================
    // GET COUPON BY ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<CouponResponse> getCoupon(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                couponService.getCoupon(id)
        );
    }

    // =========================
    // UPDATE COUPON
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<CouponResponse> updateCoupon(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCouponRequest request) {

        return ResponseEntity.ok(
                couponService.updateCoupon(id, request)
        );
    }

    // =========================
    // DEACTIVATE COUPON
    // =========================

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateCoupon(
            @PathVariable Long id) {

        couponService.deactivateCoupon(id);

        return ResponseEntity.noContent().build();
    }
}