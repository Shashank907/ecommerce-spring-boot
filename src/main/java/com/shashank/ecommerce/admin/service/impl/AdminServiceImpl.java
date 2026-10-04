package com.shashank.ecommerce.admin.service.impl;


import com.shashank.ecommerce.admin.dto.AdminDashboardDto;
import com.shashank.ecommerce.admin.dto.AdminInventoryDto;
import com.shashank.ecommerce.admin.dto.AdminUserDto;
import com.shashank.ecommerce.admin.service.AdminService;
import com.shashank.ecommerce.inventory.entity.Inventory;
import com.shashank.ecommerce.inventory.repository.InventoryRepository;
import com.shashank.ecommerce.order.dto.OrderDto;
import com.shashank.ecommerce.order.entity.OrderStatus;
import com.shashank.ecommerce.order.repository.OrderRepository;
import com.shashank.ecommerce.order.service.OrderService;
import com.shashank.ecommerce.payment.repository.PaymentRepository;
import com.shashank.ecommerce.product.repository.ProductRepository;
import com.shashank.ecommerce.user.entity.Role;
import com.shashank.ecommerce.user.entity.User;
import com.shashank.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderService orderService;


    @Override
    public AdminDashboardDto getDashboard() {

        long totalUsers = userRepository.count();

        long totalProducts = productRepository.count();

        long totalOrders = orderRepository.count();

        long pendingOrders =
                orderRepository.countByStatus(OrderStatus.PENDING);

        BigDecimal totalRevenue =
                paymentRepository.getTotalSuccessfulRevenue();

        return new AdminDashboardDto(
                totalUsers,
                totalProducts,
                totalOrders,
                pendingOrders,
                totalRevenue
        );
    }

    @Override
    public List<AdminInventoryDto> getAllInventory() {

        return inventoryRepository.findAll()
                .stream()
                .map(this::mapToInventoryDto)
                .toList();
    }

    private AdminInventoryDto mapToInventoryDto(Inventory inventory) {

        return new AdminInventoryDto(
                inventory.getProduct().getId(),
                inventory.getProduct().getName(),
                inventory.getQuantity(),
                inventory.getReservedQuantity(),
                inventory.getQuantity() - inventory.getReservedQuantity()
        );
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Override
    public List<AdminUserDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> new AdminUserDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ))
                .toList();
    }


}