package com.shashank.ecommerce.coupon.service;

import com.shashank.ecommerce.coupon.dto.request.ApplyCouponRequest;
import com.shashank.ecommerce.coupon.dto.request.CreateCouponRequest;
import com.shashank.ecommerce.coupon.dto.request.UpdateCouponRequest;
import com.shashank.ecommerce.coupon.dto.response.CouponApplyResponse;
import com.shashank.ecommerce.coupon.dto.response.CouponResponse;

import java.util.List;

public interface CouponService {

    CouponResponse createCoupon(CreateCouponRequest request);

    CouponResponse updateCoupon(
            Long id,
            UpdateCouponRequest request
    );

    CouponResponse getCoupon(Long id);

    List<CouponResponse> getAllCoupons();

    void deactivateCoupon(Long id);

    CouponApplyResponse applyCoupon(
            ApplyCouponRequest request
    );
}