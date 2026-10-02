package com.shashank.ecommerce.order.service.impl;

import com.shashank.ecommerce.order.dto.OrderItemDto;
import com.shashank.ecommerce.order.entity.OrderItem;
import com.shashank.ecommerce.order.repository.OrderItemRepository;
import com.shashank.ecommerce.order.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Override
    public List<OrderItemDto> getOrderItems(Long orderId) {

        return orderItemRepository.findByOrderId(orderId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private OrderItemDto mapToDto(OrderItem orderItem) {

        OrderItemDto dto = new OrderItemDto();

        dto.setId(orderItem.getId());
        dto.setOrderId(orderItem.getOrder().getId());
        dto.setProductId(orderItem.getProduct().getId());
        dto.setProductName(orderItem.getProductName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setUnitPrice(orderItem.getUnitPrice());
        dto.setSubtotal(orderItem.getSubtotal());

        return dto;
    }
}