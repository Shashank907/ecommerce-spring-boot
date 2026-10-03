package com.shashank.ecommerce.payment.service;

import com.shashank.ecommerce.payment.dto.CreatePaymentRequestDto;
import com.shashank.ecommerce.payment.dto.PaymentDto;

public interface PaymentService {

    PaymentDto createPayment(
            Long orderId,
            CreatePaymentRequestDto request
    );

    PaymentDto getPaymentByOrderId(Long orderId);
    PaymentDto markPaymentSuccessful(Long orderId);
    PaymentDto markPaymentFailed(Long orderId);

    PaymentDto refundPayment(Long orderId);
}