package com.shashank.ecommerce.cart.service.impl;

import com.shashank.ecommerce.cart.dto.AddCartItemRequestDto;
import com.shashank.ecommerce.cart.dto.CartItemDto;
import com.shashank.ecommerce.cart.entity.Cart;
import com.shashank.ecommerce.cart.entity.CartItem;
import com.shashank.ecommerce.cart.repository.CartItemRepository;
import com.shashank.ecommerce.cart.repository.CartRepository;
import com.shashank.ecommerce.cart.service.CartItemService;
import com.shashank.ecommerce.exception.ResourceNotFoundException;
import com.shashank.ecommerce.product.entity.Product;
import com.shashank.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public CartItemDto addItem(
            Long userId,
            AddCartItemRequestDto request) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found for user id: " + userId
                        ));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: "
                                        + request.getProductId()
                        ));

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(
                        cart.getId(),
                        product.getId()
                )
                .orElse(null);

        if (cartItem != null) {

            cartItem.setQuantity(
                    cartItem.getQuantity() + request.getQuantity()
            );

        } else {

            cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
        }

        CartItem savedItem = cartItemRepository.save(cartItem);

        return mapToDto(savedItem);
    }

    @Override
    public List<CartItemDto> getCartItems(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found for user id: " + userId
                        ));

        return cartItemRepository.findByCartId(cart.getId())
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public void removeItem(Long userId, Long itemId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found for user id: " + userId
                        ));

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found with id: " + itemId
                        ));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException(
                    "Cart item does not belong to this user's cart"
            );
        }

        cartItemRepository.delete(cartItem);
    }

    private CartItemDto mapToDto(CartItem cartItem) {

        CartItemDto dto = new CartItemDto();

        dto.setId(cartItem.getId());
        dto.setCartId(cartItem.getCart().getId());
        dto.setProductId(cartItem.getProduct().getId());
        dto.setProductName(cartItem.getProduct().getName());
        dto.setProductPrice(cartItem.getProduct().getPrice());
        dto.setQuantity(cartItem.getQuantity());

        dto.setSubtotal(
                cartItem.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
        );

        return dto;
    }
}