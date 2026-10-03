package com.shashank.ecommerce.payment.dto;

import com.shashank.ecommerce.payment.entity.PaymentMethod;
import com.shashank.ecommerce.payment.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private Long id;

    private Long orderId;

    private String transactionId;

    private BigDecimal amount;

    private PaymentMethod method;

    private PaymentStatus status;

    private LocalDateTime paidAt;

    private LocalDateTime createdAt;
}