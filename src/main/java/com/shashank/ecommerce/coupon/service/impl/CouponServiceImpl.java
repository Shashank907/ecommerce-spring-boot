package com.shashank.ecommerce.coupon.service.impl;

import com.shashank.ecommerce.coupon.dto.request.ApplyCouponRequest;
import com.shashank.ecommerce.coupon.dto.request.CreateCouponRequest;
import com.shashank.ecommerce.coupon.dto.request.UpdateCouponRequest;
import com.shashank.ecommerce.coupon.dto.response.CouponApplyResponse;
import com.shashank.ecommerce.coupon.dto.response.CouponResponse;
import com.shashank.ecommerce.coupon.entity.Coupon;
import com.shashank.ecommerce.coupon.enums.DiscountType;
import com.shashank.ecommerce.coupon.exception.CouponAlreadyExistsException;
import com.shashank.ecommerce.coupon.exception.CouponNotFoundException;
import com.shashank.ecommerce.coupon.exception.InvalidCouponException;
import com.shashank.ecommerce.coupon.repository.CouponRepository;
import com.shashank.ecommerce.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    // =========================
    // CREATE COUPON
    // =========================

    @Override
    public CouponResponse createCoupon(
            CreateCouponRequest request) {

        String code = normalizeCode(request.code());

        // Check duplicate coupon code
        if (couponRepository.existsByCodeIgnoreCase(code)) {
            throw new CouponAlreadyExistsException(
                    "Coupon code already exists: " + code
            );
        }

        // Validate coupon rules
        validateCouponRules(
                request.discountType(),
                request.discountValue(),
                request.maximumDiscount(),
                request.startDate(),
                request.expiryDate()
        );

        Coupon coupon = Coupon.builder()
                .code(code)
                .description(request.description())
                .discountType(request.discountType())
                .discountValue(request.discountValue())
                .minimumOrderAmount(
                        request.minimumOrderAmount()
                )
                .maximumDiscount(
                        request.maximumDiscount()
                )
                .startDate(request.startDate())
                .expiryDate(request.expiryDate())
                .usageLimit(request.usageLimit())
                .usedCount(0)
                .active(true)
                .build();

        Coupon savedCoupon = couponRepository.save(coupon);

        return mapToResponse(savedCoupon);
    }

    // =========================
    // UPDATE COUPON
    // =========================

    @Override
    public CouponResponse updateCoupon(
            Long id,
            UpdateCouponRequest request) {

        Coupon coupon = findCouponById(id);

        String code = normalizeCode(request.code());

        // Check if another coupon already has this code
        if (couponRepository
                .existsByCodeIgnoreCaseAndIdNot(code, id)) {

            throw new CouponAlreadyExistsException(
                    "Coupon code already exists: " + code
            );
        }

        // Validate updated coupon rules
        validateCouponRules(
                request.discountType(),
                request.discountValue(),
                request.maximumDiscount(),
                request.startDate(),
                request.expiryDate()
        );

        coupon.setCode(code);

        coupon.setDescription(
                request.description()
        );

        coupon.setDiscountType(
                request.discountType()
        );

        coupon.setDiscountValue(
                request.discountValue()
        );

        coupon.setMinimumOrderAmount(
                request.minimumOrderAmount()
        );

        coupon.setMaximumDiscount(
                request.maximumDiscount()
        );

        coupon.setStartDate(
                request.startDate()
        );

        coupon.setExpiryDate(
                request.expiryDate()
        );

        coupon.setUsageLimit(
                request.usageLimit()
        );

        coupon.setActive(
                request.active()
        );

        Coupon updatedCoupon =
                couponRepository.save(coupon);

        return mapToResponse(updatedCoupon);
    }

    // =========================
    // GET COUPON BY ID
    // =========================

    @Override
    @Transactional(readOnly = true)
    public CouponResponse getCoupon(Long id) {

        Coupon coupon = findCouponById(id);

        return mapToResponse(coupon);
    }

    // =========================
    // GET ALL COUPONS
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<CouponResponse> getAllCoupons() {

        return couponRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // DEACTIVATE COUPON
    // =========================

    @Override
    public void deactivateCoupon(Long id) {

        Coupon coupon = findCouponById(id);

        coupon.setActive(false);

        couponRepository.save(coupon);
    }

    // =========================
    // APPLY COUPON
    // =========================

    @Override
    @Transactional(readOnly = true)
    public CouponApplyResponse applyCoupon(
            ApplyCouponRequest request) {

        String code = normalizeCode(
                request.code()
        );

        Coupon coupon = couponRepository
                .findByCodeIgnoreCase(code)
                .orElseThrow(() ->
                        new InvalidCouponException(
                                "Invalid coupon code"
                        )
                );

        // Validate coupon
        validateCouponForOrder(
                coupon,
                request.orderAmount()
        );

        // Calculate discount
        BigDecimal discount =
                calculateDiscount(
                        coupon,
                        request.orderAmount()
                );

        // Calculate final amount
        BigDecimal finalAmount =
                request.orderAmount()
                        .subtract(discount);

        return CouponApplyResponse.builder()
                .couponCode(
                        coupon.getCode()
                )
                .orderAmount(
                        request.orderAmount()
                )
                .discountAmount(
                        discount
                )
                .finalAmount(
                        finalAmount
                )
                .message(
                        "Coupon applied successfully"
                )
                .build();
    }

    // =========================
    // VALIDATE COUPON FOR ORDER
    // =========================

    private void validateCouponForOrder(
            Coupon coupon,
            BigDecimal orderAmount) {

        LocalDateTime now =
                LocalDateTime.now();

        // Coupon active check
        if (!coupon.getActive()) {

            throw new InvalidCouponException(
                    "Coupon is inactive"
            );
        }

        // Start date check
        if (now.isBefore(
                coupon.getStartDate()
        )) {

            throw new InvalidCouponException(
                    "Coupon is not active yet"
            );
        }

        // Expiry check
        if (now.isAfter(
                coupon.getExpiryDate()
        )) {

            throw new InvalidCouponException(
                    "Coupon has expired"
            );
        }

        // Usage limit check
        if (coupon.getUsageLimit() != null
                && coupon.getUsedCount()
                >= coupon.getUsageLimit()) {

            throw new InvalidCouponException(
                    "Coupon usage limit has been reached"
            );
        }

        // Minimum order amount check
        if (coupon.getMinimumOrderAmount() != null
                && orderAmount.compareTo(
                coupon.getMinimumOrderAmount()
        ) < 0) {

            throw new InvalidCouponException(
                    "Minimum order amount is "
                            + coupon.getMinimumOrderAmount()
            );
        }
    }

    // =========================
    // CALCULATE DISCOUNT
    // =========================

    private BigDecimal calculateDiscount(
            Coupon coupon,
            BigDecimal orderAmount) {

        BigDecimal discount;

        // Percentage discount
        if (coupon.getDiscountType()
                == DiscountType.PERCENTAGE) {

            discount = orderAmount
                    .multiply(
                            coupon.getDiscountValue()
                    )
                    .divide(
                            BigDecimal.valueOf(100),
                            2,
                            RoundingMode.HALF_UP
                    );

            // Maximum discount limit
            if (coupon.getMaximumDiscount() != null
                    && discount.compareTo(
                    coupon.getMaximumDiscount()
            ) > 0) {

                discount =
                        coupon.getMaximumDiscount();
            }

        } else {

            // Fixed discount
            discount =
                    coupon.getDiscountValue()
                            .min(orderAmount);
        }

        return discount.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }

    // =========================
    // VALIDATE COUPON RULES
    // =========================

    private void validateCouponRules(
            DiscountType discountType,
            BigDecimal discountValue,
            BigDecimal maximumDiscount,
            LocalDateTime startDate,
            LocalDateTime expiryDate) {

        // Expiry must be after start
        if (!expiryDate.isAfter(startDate)) {

            throw new InvalidCouponException(
                    "Expiry date must be after start date"
            );
        }

        // Percentage cannot exceed 100%
        if (discountType == DiscountType.PERCENTAGE
                && discountValue.compareTo(
                BigDecimal.valueOf(100)
        ) > 0) {

            throw new InvalidCouponException(
                    "Percentage discount cannot exceed 100%"
            );
        }

        // Maximum discount only makes sense
        // for percentage coupons
        if (discountType == DiscountType.FIXED
                && maximumDiscount != null) {

            throw new InvalidCouponException(
                    "Maximum discount is only applicable "
                            + "to percentage coupons"
            );
        }
    }

    // =========================
    // FIND COUPON BY ID
    // =========================

    private Coupon findCouponById(Long id) {

        return couponRepository.findById(id)
                .orElseThrow(() ->
                        new CouponNotFoundException(
                                "Coupon not found with id: "
                                        + id
                        )
                );
    }

    // =========================
    // NORMALIZE COUPON CODE
    // =========================

    private String normalizeCode(String code) {

        return code
                .trim()
                .toUpperCase();
    }

    // =========================
    // ENTITY -> RESPONSE DTO
    // =========================

    private CouponResponse mapToResponse(
            Coupon coupon) {

        return CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .description(coupon.getDescription())
                .discountType(
                        coupon.getDiscountType()
                )
                .discountValue(
                        coupon.getDiscountValue()
                )
                .minimumOrderAmount(
                        coupon.getMinimumOrderAmount()
                )
                .maximumDiscount(
                        coupon.getMaximumDiscount()
                )
                .startDate(
                        coupon.getStartDate()
                )
                .expiryDate(
                        coupon.getExpiryDate()
                )
                .usageLimit(
                        coupon.getUsageLimit()
                )
                .usedCount(
                        coupon.getUsedCount()
                )
                .active(
                        coupon.getActive()
                )
                .createdAt(
                        coupon.getCreatedAt()
                )
                .updatedAt(
                        coupon.getUpdatedAt()
                )
                .build();
    }
}