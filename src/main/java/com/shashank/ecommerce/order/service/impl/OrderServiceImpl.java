package com.shashank.ecommerce.order.service.impl;

import com.shashank.ecommerce.cart.entity.Cart;
import com.shashank.ecommerce.cart.entity.CartItem;
import com.shashank.ecommerce.cart.repository.CartItemRepository;
import com.shashank.ecommerce.cart.repository.CartRepository;
import com.shashank.ecommerce.coupon.dto.request.ApplyCouponRequest;
import com.shashank.ecommerce.coupon.dto.response.CouponApplyResponse;
import com.shashank.ecommerce.coupon.service.CouponService;
import com.shashank.ecommerce.exception.ResourceNotFoundException;
import com.shashank.ecommerce.inventory.service.InventoryService;
import com.shashank.ecommerce.notification.enums.NotificationType;
import com.shashank.ecommerce.notification.service.NotificationService;
import com.shashank.ecommerce.order.dto.OrderDto;
import com.shashank.ecommerce.order.entity.Order;
import com.shashank.ecommerce.order.entity.OrderItem;
import com.shashank.ecommerce.order.entity.OrderStatus;
import com.shashank.ecommerce.order.repository.OrderItemRepository;
import com.shashank.ecommerce.order.repository.OrderRepository;
import com.shashank.ecommerce.order.service.OrderService;
import com.shashank.ecommerce.payment.service.PaymentService;
import com.shashank.ecommerce.user.entity.Address;
import com.shashank.ecommerce.user.entity.User;
import com.shashank.ecommerce.user.repository.AddressRepository;
import com.shashank.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CouponService couponService;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public OrderDto createOrder(Long userId, String couponCode) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        ));

        Address address = addressRepository.findByUserId(userId)
                .stream()
                .filter(Address::getIsDefault)
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Default address not found for user id: " + userId
                        ));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found for user id: " + userId
                        ));

        List<CartItem> cartItems =
                cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot create order from an empty cart"
            );
        }

        // Check inventory for all cart items before creating the order
        for (CartItem cartItem : cartItems) {

            boolean available = inventoryService.checkStock(
                    cartItem.getProduct().getId(),
                    cartItem.getQuantity()
            );

            if (!available) {
                throw new IllegalArgumentException(
                        "Insufficient stock for product: "
                                + cartItem.getProduct().getName()
                );
            }
        }

        // Calculate subtotal
        BigDecimal subtotalAmount = cartItems.stream()
                .map(item ->
                        item.getProduct()
                                .getPrice()
                                .multiply(
                                        BigDecimal.valueOf(
                                                item.getQuantity()
                                        )
                                )
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Default values when no coupon is used
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal totalAmount = subtotalAmount;
        String appliedCouponCode = null;

        // Apply coupon if provided
        if (couponCode != null && !couponCode.isBlank()) {

            CouponApplyResponse couponResponse =
                    couponService.applyCoupon(
                            new ApplyCouponRequest(
                                    couponCode,
                                    subtotalAmount
                            )
                    );

            discountAmount = couponResponse.discountAmount();
            totalAmount = couponResponse.finalAmount();
            appliedCouponCode = couponResponse.couponCode();
        }

        // Create Order
        Order order = new Order();

        order.setUser(user);

        order.setOrderNumber(
                "ORD-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase()
        );

        order.setStatus(OrderStatus.PENDING);

        order.setSubtotalAmount(subtotalAmount);
        order.setDiscountAmount(discountAmount);
        order.setTotalAmount(totalAmount);
        order.setCouponCode(appliedCouponCode);

        // Address snapshot
        order.setShippingAddressLine1(address.getAddressLine1());
        order.setShippingAddressLine2(address.getAddressLine2());
        order.setShippingCity(address.getCity());
        order.setShippingState(address.getState());
        order.setShippingPostalCode(address.getPostalCode());
        order.setShippingCountry(address.getCountry());

        Order savedOrder = orderRepository.save(order);

        // Reserve inventory
        for (CartItem cartItem : cartItems) {

            inventoryService.reserveStock(
                    cartItem.getProduct().getId(),
                    cartItem.getQuantity()
            );
        }

        // Create OrderItems
        for (CartItem cartItem : cartItems) {

            BigDecimal unitPrice =
                    cartItem.getProduct().getPrice();

            BigDecimal itemSubtotal =
                    unitPrice.multiply(
                            BigDecimal.valueOf(
                                    cartItem.getQuantity()
                            )
                    );

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(savedOrder);

            orderItem.setProduct(
                    cartItem.getProduct()
            );

            orderItem.setProductName(
                    cartItem.getProduct().getName()
            );

            orderItem.setQuantity(
                    cartItem.getQuantity()
            );

            orderItem.setUnitPrice(unitPrice);

            orderItem.setSubtotal(itemSubtotal);

            orderItemRepository.save(orderItem);
        }

        return mapToDto(savedOrder);
    }

    @Override
    public OrderDto getOrderById(Long userId, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId
                        ));

        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException(
                    "You are not allowed to access this order"
            );
        }

        return mapToDto(order);
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<OrderDto> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }


    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId
                        ));

        // Check ownership
        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException(
                    "You are not allowed to cancel this order"
            );
        }

        // Already cancelled
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Order is already cancelled"
            );
        }

        // Cannot cancel after shipping
        if (order.getStatus() == OrderStatus.SHIPPED ||
                order.getStatus() == OrderStatus.DELIVERED) {

            throw new IllegalArgumentException(
                    "Order cannot be cancelled after shipping"
            );
        }

        /*
         * PENDING
         *
         * Payment has not succeeded.
         * Inventory is reserved.
         *
         * Release the reservation.
         */
        if (order.getStatus() == OrderStatus.PENDING) {

            List<OrderItem> orderItems =
                    orderItemRepository.findByOrderId(orderId);

            for (OrderItem orderItem : orderItems) {

                inventoryService.releaseStock(
                        orderItem.getProduct().getId(),
                        orderItem.getQuantity()
                );
            }

            order.setStatus(OrderStatus.CANCELLED);

            orderRepository.save(order);

            notificationService.createNotification(
                    order.getUser().getId(),
                    order.getId(),
                    NotificationType.ORDER_CANCELLED,
                    "Your order #" + order.getOrderNumber() + " has been cancelled"
            );

            return;
        }

        /*
         * CONFIRMED
         *
         * Payment succeeded.
         * Inventory was already consumed.
         *
         * Refund payment.
         * refundPayment() will restore inventory
         * and mark the order as CANCELLED.
         */
        if (order.getStatus() == OrderStatus.CONFIRMED) {

            paymentService.refundPayment(orderId);

            return;
        }

        /*
         * PROCESSING
         *
         * Cancellation is not allowed once processing starts.
         */
        if (order.getStatus() == OrderStatus.PROCESSING) {

            throw new IllegalArgumentException(
                    "Order cannot be cancelled after processing has started"
            );
        }

        throw new IllegalArgumentException(
                "Order cannot be cancelled in current status: "
                        + order.getStatus()
        );
    }

    @Override
    @Transactional
    public void updateOrderStatus(
            Long orderId,
            OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId
                        ));

        OrderStatus currentStatus = order.getStatus();

        if (currentStatus == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Cancelled order cannot be updated"
            );
        }

        if (currentStatus == OrderStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Payment must be successful before updating order status"
            );
        }

        if (currentStatus == OrderStatus.DELIVERED) {
            throw new IllegalArgumentException(
                    "Delivered order cannot be updated"
            );
        }

        if (currentStatus == OrderStatus.CONFIRMED
                && newStatus != OrderStatus.PROCESSING) {

            throw new IllegalArgumentException(
                    "Confirmed order can only move to PROCESSING"
            );
        }

        if (currentStatus == OrderStatus.PROCESSING
                && newStatus != OrderStatus.SHIPPED) {

            throw new IllegalArgumentException(
                    "Processing order can only move to SHIPPED"
            );
        }

        if (currentStatus == OrderStatus.SHIPPED
                && newStatus != OrderStatus.DELIVERED) {

            throw new IllegalArgumentException(
                    "Shipped order can only move to DELIVERED"
            );
        }

        order.setStatus(newStatus);
        orderRepository.save(order);

        if (newStatus == OrderStatus.PROCESSING) {

            notificationService.createNotification(
                    order.getUser().getId(),
                    order.getId(),
                    NotificationType.ORDER_PROCESSING,
                    "Your order #" + order.getOrderNumber() + " is now being processed"
            );


            }
        if (newStatus == OrderStatus.SHIPPED) {

            notificationService.createNotification(
                    order.getUser().getId(),
                    order.getId(),
                    NotificationType.ORDER_SHIPPED,
                    "Your order #" + order.getOrderNumber() + " has been shipped"
            );
        }

        if (newStatus == OrderStatus.DELIVERED) {

            notificationService.createNotification(
                    order.getUser().getId(),
                    order.getId(),
                    NotificationType.ORDER_DELIVERED,
                    "Your order #" + order.getOrderNumber() + " has been delivered"
            );
        }
    }

    private OrderDto mapToDto(Order order) {

        OrderDto dto = new OrderDto();

        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus());

        dto.setSubtotalAmount(
                order.getSubtotalAmount()
        );

        dto.setDiscountAmount(
                order.getDiscountAmount()
        );

        dto.setTotalAmount(
                order.getTotalAmount()
        );

        dto.setCouponCode(
                order.getCouponCode()
        );

        dto.setShippingAddressLine1(
                order.getShippingAddressLine1()
        );

        dto.setShippingAddressLine2(
                order.getShippingAddressLine2()
        );

        dto.setShippingCity(
                order.getShippingCity()
        );

        dto.setShippingState(
                order.getShippingState()
        );

        dto.setShippingPostalCode(
                order.getShippingPostalCode()
        );

        dto.setShippingCountry(
                order.getShippingCountry()
        );

        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        return dto;
    }
}