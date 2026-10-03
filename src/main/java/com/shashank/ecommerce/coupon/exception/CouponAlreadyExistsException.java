package com.shashank.ecommerce.coupon.exception;

public class CouponAlreadyExistsException extends RuntimeException{
    public CouponAlreadyExistsException(String message){
        super(message);
    }
}
