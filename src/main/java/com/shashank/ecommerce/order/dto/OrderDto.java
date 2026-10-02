package com.shashank.ecommerce.order.dto;

import com.shashank.ecommerce.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;

    private Long userId;

    private String orderNumber;

    private OrderStatus status;

    private Double totalAmount;

    private String shippingAddressLine1;

    private String shippingAddressLine2;

    private String shippingCity;

    private String shippingState;

    private String shippingPostalCode;

    private String shippingCountry;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}