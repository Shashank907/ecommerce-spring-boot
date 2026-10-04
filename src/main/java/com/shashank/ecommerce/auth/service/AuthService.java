package com.shashank.ecommerce.auth.service;

import com.shashank.ecommerce.auth.dto.LoginRequest;
import com.shashank.ecommerce.auth.dto.LoginResponse;
import com.shashank.ecommerce.auth.dto.SignupRequest;
import com.shashank.ecommerce.auth.dto.SignupResponse;

public interface AuthService {

    SignupResponse signup(SignupRequest request);

    LoginResponse login(LoginRequest request);
}