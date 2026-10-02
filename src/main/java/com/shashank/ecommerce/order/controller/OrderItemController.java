package com.shashank.ecommerce.order.controller;

import com.shashank.ecommerce.order.dto.OrderItemDto;
import com.shashank.ecommerce.order.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders/{orderId}/items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<List<OrderItemDto>> getOrderItems(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(orderItemService.getOrderItems(orderId));
    }
}