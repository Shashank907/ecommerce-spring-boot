package com.shashank.ecommerce.payment.service.impl;

import com.shashank.ecommerce.exception.ResourceNotFoundException;
import com.shashank.ecommerce.order.entity.Order;
import com.shashank.ecommerce.order.entity.OrderStatus;
import com.shashank.ecommerce.order.repository.OrderRepository;
import com.shashank.ecommerce.payment.dto.CreatePaymentRequestDto;
import com.shashank.ecommerce.payment.dto.PaymentDto;
import com.shashank.ecommerce.payment.entity.Payment;
import com.shashank.ecommerce.payment.entity.PaymentStatus;
import com.shashank.ecommerce.payment.repository.PaymentRepository;
import com.shashank.ecommerce.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public PaymentDto createPayment(
            Long orderId,
            CreatePaymentRequestDto request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId
                        ));

        if (paymentRepository.existsByOrderId(orderId)) {
            throw new IllegalArgumentException(
                    "Payment already exists for order id: " + orderId
            );
        }

        Payment payment = new Payment();

        payment.setOrder(order);

        payment.setTransactionId(
                "TXN-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 12)
                        .toUpperCase()
        );

        payment.setAmount(order.getTotalAmount());

        payment.setMethod(request.getMethod());

        payment.setStatus(PaymentStatus.PENDING);

        Payment savedPayment = paymentRepository.save(payment);

        return mapToDto(savedPayment);
    }

    @Override
    public PaymentDto getPaymentByOrderId(Long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for order id: "
                                        + orderId
                        ));

        return mapToDto(payment);
    }

    @Override
    @Transactional
    public PaymentDto markPaymentSuccessful(Long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for order id: " + orderId
                        ));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new IllegalArgumentException(
                    "Payment is already successful"
            );
        }

        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            throw new IllegalArgumentException(
                    "Refunded payment cannot be marked as successful"
            );
        }

        Order order = payment.getOrder();

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());

        order.setStatus(OrderStatus.CONFIRMED);

        Payment savedPayment = paymentRepository.save(payment);

        orderRepository.save(order);

        return mapToDto(savedPayment);
    }

    private PaymentDto mapToDto(Payment payment) {

        PaymentDto dto = new PaymentDto();

        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder().getId());
        dto.setTransactionId(payment.getTransactionId());
        dto.setAmount(payment.getAmount());
        dto.setMethod(payment.getMethod());
        dto.setStatus(payment.getStatus());
        dto.setPaidAt(payment.getPaidAt());
        dto.setCreatedAt(payment.getCreatedAt());

        return dto;
    }
}