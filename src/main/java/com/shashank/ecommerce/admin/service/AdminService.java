package com.shashank.ecommerce.admin.service;


import com.shashank.ecommerce.admin.dto.AdminDashboardDto;
import com.shashank.ecommerce.admin.dto.AdminInventoryDto;
import com.shashank.ecommerce.admin.dto.AdminUserDto;
import com.shashank.ecommerce.order.dto.OrderDto;

import java.util.List;

public interface AdminService {

    AdminDashboardDto getDashboard();


    List<AdminInventoryDto> getAllInventory();

    List<OrderDto> getAllOrders();

    List<AdminUserDto> getAllUsers();




}