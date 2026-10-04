package com.shashank.ecommerce.payment.service.impl;

import com.shashank.ecommerce.cart.entity.Cart;
import com.shashank.ecommerce.cart.entity.CartItem;
import com.shashank.ecommerce.cart.repository.CartItemRepository;
import com.shashank.ecommerce.cart.repository.CartRepository;
import com.shashank.ecommerce.coupon.entity.Coupon;
import com.shashank.ecommerce.coupon.repository.CouponRepository;
import com.shashank.ecommerce.exception.ResourceNotFoundException;
import com.shashank.ecommerce.inventory.service.InventoryService;
import com.shashank.ecommerce.notification.enums.NotificationType;
import com.shashank.ecommerce.notification.service.NotificationService;
import com.shashank.ecommerce.order.entity.Order;
import com.shashank.ecommerce.order.entity.OrderItem;
import com.shashank.ecommerce.order.entity.OrderStatus;
import com.shashank.ecommerce.order.repository.OrderItemRepository;
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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    private final CouponRepository couponRepository;

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private final OrderItemRepository orderItemRepository;

    private final InventoryService inventoryService;


    private final NotificationService notificationService;

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
           return mapToDto(payment);
        }

        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            throw new IllegalArgumentException(
                    "Refunded payment cannot be marked as successful"
            );
        }

        Order order = payment.getOrder();

        // 1. Get order items
        List<OrderItem> orderItems =
                orderItemRepository.findByOrderId(orderId);

        // 2. Consume inventory
        for (OrderItem orderItem : orderItems) {

            inventoryService.consumeStock(
                    orderItem.getProduct().getId(),
                    orderItem.getQuantity()
            );
        }

        // 3. Mark payment as successful
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());



        // 4. Confirm order
        order.setStatus(OrderStatus.CONFIRMED);

        Payment savedPayment = paymentRepository.save(payment);
        orderRepository.save(order);
         //5. Create notification
        notificationService.createNotification(
                order.getUser().getId(),
                order.getId(),
                NotificationType.PAYMENT_SUCCESS,
                "Payment successful for order #" + order.getOrderNumber()
        );

        notificationService.createNotification(
                order.getUser().getId(),
                order.getId(),
                NotificationType.ORDER_CONFIRMED,
                "Your order #" + order.getOrderNumber() + " has been confirmed"
        );



        // 6. Increment coupon usage
        incrementCouponUsage(order);

        // 7. Clear cart
        clearCart(order);

        return mapToDto(savedPayment);
    }

    private void incrementCouponUsage(Order order) {

        String couponCode = order.getCouponCode();

        if (couponCode == null || couponCode.isBlank()) {
            return;
        }

        Coupon coupon = couponRepository
                .findByCodeIgnoreCase(couponCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Coupon not found: " + couponCode
                        ));

        coupon.setUsedCount(coupon.getUsedCount() + 1);

        couponRepository.save(coupon);
    }

    private void clearCart(Order order) {

        Long userId = order.getUser().getId();

        Cart cart = cartRepository.findByUserId(userId)
                .orElse(null);

        if (cart == null) {
            return;
        }

        List<CartItem> cartItems =
                cartItemRepository.findByCartId(cart.getId());

        if (!cartItems.isEmpty()) {
            cartItemRepository.deleteAll(cartItems);
        }
    }

    @Override
    @Transactional
    public PaymentDto markPaymentFailed(Long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for order id: " + orderId
                        ));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new IllegalArgumentException(
                    "Successful payment cannot be marked as failed"
            );
        }

        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            throw new IllegalArgumentException(
                    "Refunded payment cannot be marked as failed"
            );
        }

        Order order = payment.getOrder();

        List<OrderItem> orderItems =
                orderItemRepository.findByOrderId(orderId);

        // Release reserved inventory
        for (OrderItem orderItem : orderItems) {

            inventoryService.releaseStock(
                    orderItem.getProduct().getId(),
                    orderItem.getQuantity()
            );
        }

        // Mark payment as failed
        payment.setStatus(PaymentStatus.FAILED);

        // Cancel order
        order.setStatus(OrderStatus.CANCELLED);

        Payment savedPayment = paymentRepository.save(payment);

        orderRepository.save(order);

        notificationService.createNotification(
                order.getUser().getId(),
                order.getId(),
                NotificationType.PAYMENT_FAILED,
                "Payment failed for order #" + order.getOrderNumber()
        );

        notificationService.createNotification(
                order.getUser().getId(),
                order.getId(),
                NotificationType.ORDER_CANCELLED,
                "Your order #" + order.getOrderNumber() + " has been cancelled"
        );

        return mapToDto(savedPayment);
    }

    @Override
    @Transactional
    public PaymentDto refundPayment(Long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for order id: " + orderId
                        ));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new IllegalArgumentException(
                    "Only successful payments can be refunded"
            );
        }

        Order order = payment.getOrder();

        List<OrderItem> orderItems =
                orderItemRepository.findByOrderId(orderId);

        // Restore inventory
        for (OrderItem orderItem : orderItems) {

            inventoryService.restoreStock(
                    orderItem.getProduct().getId(),
                    orderItem.getQuantity()
            );
        }

        // Mark payment as refunded
        payment.setStatus(PaymentStatus.REFUNDED);

        // Cancel order
        order.setStatus(OrderStatus.CANCELLED);

        Payment savedPayment = paymentRepository.save(payment);

        orderRepository.save(order);

        notificationService.createNotification(
                order.getUser().getId(),
                order.getId(),
                NotificationType.PAYMENT_REFUNDED,
                "Payment for order #" + order.getOrderNumber() + " has been refunded"
        );

        notificationService.createNotification(
                order.getUser().getId(),
                order.getId(),
                NotificationType.ORDER_CANCELLED,
                "Your order #" + order.getOrderNumber() + " has been cancelled"
        );

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