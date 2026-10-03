package com.shashank.ecommerce.order.service;

import com.shashank.ecommerce.order.dto.OrderDto;
import com.shashank.ecommerce.order.entity.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(Long userId, String couponCode);

    OrderDto getOrderById(Long id);

    List<OrderDto> getUserOrders(Long userId);

    void cancelOrder(Long userId, Long orderId);

    void updateOrderStatus(Long orderId, OrderStatus newStatus);
}