package com.shashank.ecommerce.coupon.exception;

public class CouponNotFoundException extends RuntimeException{
    public CouponNotFoundException(String message){
        super(message);
    }
}
