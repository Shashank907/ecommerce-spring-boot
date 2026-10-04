package com.shashank.ecommerce.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResponse {

    private Long id;
    private String name;
    private String email;
    private String role;
}