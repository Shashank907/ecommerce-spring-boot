package com.shashank.ecommerce.order.service;

import com.shashank.ecommerce.order.dto.OrderItemDto;

import java.util.List;

public interface OrderItemService {

    List<OrderItemDto> getOrderItems(Long orderId);
}
