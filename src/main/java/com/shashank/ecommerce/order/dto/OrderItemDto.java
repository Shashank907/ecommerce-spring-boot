package com.shashank.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {

    private Long id;

    private Long orderId;

    private Long productId;

    private String productName;

    private Integer quantity;

    private Double unitPrice;

    private Double subtotal;
}