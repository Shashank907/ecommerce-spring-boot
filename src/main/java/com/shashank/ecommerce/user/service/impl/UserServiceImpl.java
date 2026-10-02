package com.shashank.ecommerce.user.service.impl;

import com.shashank.ecommerce.exception.ResourceNotFoundException;
import com.shashank.ecommerce.user.dto.CreateUserRequestDto;
import com.shashank.ecommerce.user.dto.UserDto;
import com.shashank.ecommerce.user.entity.Role;
import com.shashank.ecommerce.user.entity.User;
import com.shashank.ecommerce.user.entity.UserStatus;
import com.shashank.ecommerce.user.repository.UserRepository;
import com.shashank.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto createUser(CreateUserRequestDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());

        user.setRole(Role.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        return mapToDto(savedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public UserDto getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        ));

        return mapToDto(user);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        ));

        userRepository.delete(user);
    }

    private UserDto mapToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}