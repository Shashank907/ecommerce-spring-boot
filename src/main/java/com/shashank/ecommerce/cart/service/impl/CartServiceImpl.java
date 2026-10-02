package com.shashank.ecommerce.cart.service.impl;

import com.shashank.ecommerce.cart.dto.CartDto;
import com.shashank.ecommerce.cart.entity.Cart;
import com.shashank.ecommerce.cart.entity.CartStatus;
import com.shashank.ecommerce.cart.repository.CartRepository;
import com.shashank.ecommerce.cart.service.CartService;
import com.shashank.ecommerce.exception.ResourceNotFoundException;
import com.shashank.ecommerce.user.entity.User;
import com.shashank.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public CartDto createCart(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        ));

        if (cartRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException(
                    "Cart already exists for user id: " + userId
            );
        }

        Cart cart = new Cart();

        cart.setUser(user);
        cart.setStatus(CartStatus.ACTIVE);

        Cart savedCart = cartRepository.save(cart);

        return mapToDto(savedCart);
    }

    @Override
    public CartDto getCartByUserId(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found for user id: " + userId
                        ));

        return mapToDto(cart);
    }

    @Override
    public void deleteCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found for user id: " + userId
                        ));

        cartRepository.delete(cart);
    }

    private CartDto mapToDto(Cart cart) {

        CartDto dto = modelMapper.map(cart, CartDto.class);

        dto.setUserId(cart.getUser().getId());

        return dto;
    }
}