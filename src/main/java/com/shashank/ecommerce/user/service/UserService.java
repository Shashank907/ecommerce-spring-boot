package com.shashank.ecommerce.user.service;

import com.shashank.ecommerce.user.dto.CreateUserRequestDto;
import com.shashank.ecommerce.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(CreateUserRequestDto request);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    void deleteUser(Long id);
}