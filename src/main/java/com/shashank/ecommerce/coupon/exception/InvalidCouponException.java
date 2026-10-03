package com.shashank.ecommerce.coupon.exception;

public class InvalidCouponException extends RuntimeException{
    public InvalidCouponException(String message){
        super(message);
    }
}
