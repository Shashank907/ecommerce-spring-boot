package com.shashank.ecommerce.admin.controller;


import com.shashank.ecommerce.admin.dto.AdminDashboardDto;
import com.shashank.ecommerce.admin.dto.AdminInventoryDto;
import com.shashank.ecommerce.admin.dto.AdminUserDto;
import com.shashank.ecommerce.admin.service.AdminService;
import com.shashank.ecommerce.order.dto.OrderDto;
import com.shashank.ecommerce.order.entity.OrderStatus;
import com.shashank.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final OrderService orderService;


    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDto> getDashboard() {

        return ResponseEntity.ok(
                adminService.getDashboard()
        );
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<AdminInventoryDto>> getInventory() {

        return ResponseEntity.ok(
                adminService.getAllInventory()
        );
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {

        return ResponseEntity.ok(
                adminService.getAllOrders()
        );
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDto>> getAllUsers() {

        return ResponseEntity.ok(
                adminService.getAllUsers()
        );
    }

    @PutMapping("/orders/{orderId}/status")
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