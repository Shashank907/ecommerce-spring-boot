package com.shashank.ecommerce.order.service;

import com.shashank.ecommerce.order.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(Long userId);

    OrderDto getOrderById(Long id);

    List<OrderDto> getUserOrders(Long userId);

    void cancelOrder(Long userId, Long orderId);
}