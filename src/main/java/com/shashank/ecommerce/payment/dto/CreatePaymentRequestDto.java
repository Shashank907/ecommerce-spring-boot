package com.shashank.ecommerce.payment.dto;

import com.shashank.ecommerce.payment.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePaymentRequestDto {

    @NotNull(message = "Payment method is required")
    private PaymentMethod method;
}