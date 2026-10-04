package com.shashank.ecommerce.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUserDto {

    private Long id;
    private String name;
    private String email;
}