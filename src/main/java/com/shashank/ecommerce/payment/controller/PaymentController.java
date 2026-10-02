package com.shashank.ecommerce.payment.controller;

import com.shashank.ecommerce.payment.dto.CreatePaymentRequestDto;
import com.shashank.ecommerce.payment.dto.PaymentDto;
import com.shashank.ecommerce.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders/{orderId}/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(
            @PathVariable Long orderId,
            @RequestBody @Valid CreatePaymentRequestDto request) {

        PaymentDto payment = paymentService.createPayment(
                orderId,
                request
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(payment);
    }

    @GetMapping
    public ResponseEntity<PaymentDto> getPayment(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                paymentService.getPaymentByOrderId(orderId)
        );
    }

    @PostMapping("/success")
    public ResponseEntity<PaymentDto> markPaymentSuccessful(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                paymentService.markPaymentSuccessful(orderId)
        );
    }
}