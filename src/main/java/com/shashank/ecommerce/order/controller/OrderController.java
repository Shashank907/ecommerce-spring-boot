package com.shashank.ecommerce.order.controller;

import com.shashank.ecommerce.order.dto.CreateOrderRequest;
import com.shashank.ecommerce.order.dto.OrderDto;
import com.shashank.ecommerce.order.entity.OrderStatus;
import com.shashank.ecommerce.order.service.OrderService;
import com.shashank.ecommerce.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(
            @PathVariable Long userId,
            @Valid @RequestBody CreateOrderRequest request) {

        OrderDto createdOrder =
                orderService.createOrder(
                        userId,
                        request.couponCode()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrder);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getUserOrders() {

        Long authenticatedUserId =
                SecurityUtils.getCurrentUser()
                        .getUser()
                        .getId();

        List<OrderDto> orders =
                orderService.getUserOrders(authenticatedUserId);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(
            @PathVariable Long userId,
            @PathVariable Long orderId) {

        Long authenticatedUserId =
                SecurityUtils.getCurrentUser()
                        .getUser()
                        .getId();

        OrderDto order =
                orderService.getOrderById(
                        authenticatedUserId,
                        orderId
                );

        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long userId,
            @PathVariable Long orderId) {

        Long authenticatedUserId =
                SecurityUtils.getCurrentUser()
                        .getUser()
                        .getId();

        orderService.cancelOrder(
                authenticatedUserId,
                orderId
        );

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {

        orderService.updateOrderStatus(
                orderId,
                status
        );

        return ResponseEntity.noContent().build();
    }
}