package com.shashank.ecommerce.order.service.impl;

import com.shashank.ecommerce.cart.entity.Cart;
import com.shashank.ecommerce.cart.entity.CartItem;
import com.shashank.ecommerce.cart.repository.CartItemRepository;
import com.shashank.ecommerce.cart.repository.CartRepository;
import com.shashank.ecommerce.exception.ResourceNotFoundException;
import com.shashank.ecommerce.order.dto.OrderDto;
import com.shashank.ecommerce.order.entity.Order;
import com.shashank.ecommerce.order.entity.OrderItem;
import com.shashank.ecommerce.order.entity.OrderStatus;
import com.shashank.ecommerce.order.repository.OrderItemRepository;
import com.shashank.ecommerce.order.repository.OrderRepository;
import com.shashank.ecommerce.order.service.OrderService;
import com.shashank.ecommerce.user.entity.Address;
import com.shashank.ecommerce.user.entity.User;
import com.shashank.ecommerce.user.repository.AddressRepository;
import com.shashank.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public OrderDto createOrder(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        ));

        Address address = addressRepository.findByUserId(userId)
                .stream()
                .filter(Address::getIsDefault)
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Default address not found for user id: "
                                        + userId
                        ));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found for user id: " + userId
                        ));

        List<CartItem> cartItems =
                cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot create order from an empty cart"
            );
        }

        double totalAmount = cartItems.stream()
                .mapToDouble(item ->
                        item.getProduct().getPrice()
                                * item.getQuantity())
                .sum();

        // Create Order
        Order order = new Order();

        order.setUser(user);
        order.setOrderNumber(
                "ORD-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase()
        );
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(totalAmount);

        // Address snapshot
        order.setShippingAddressLine1(address.getAddressLine1());
        order.setShippingAddressLine2(address.getAddressLine2());
        order.setShippingCity(address.getCity());
        order.setShippingState(address.getState());
        order.setShippingPostalCode(address.getPostalCode());
        order.setShippingCountry(address.getCountry());

        Order savedOrder = orderRepository.save(order);

        // Create OrderItems
        for (CartItem cartItem : cartItems) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(savedOrder);
            orderItem.setProduct(cartItem.getProduct());

            orderItem.setProductName(
                    cartItem.getProduct().getName()
            );

            orderItem.setQuantity(
                    cartItem.getQuantity()
            );

            orderItem.setUnitPrice(
                    cartItem.getProduct().getPrice()
            );

            orderItem.setSubtotal(
                    cartItem.getProduct().getPrice()
                            * cartItem.getQuantity()
            );

            orderItemRepository.save(orderItem);
        }

        return mapToDto(savedOrder);
    }

    @Override
    public OrderDto getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + id
                        ));

        return mapToDto(order);
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId
                        ));

        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException(
                    "Order does not belong to this user"
            );
        }

        if (order.getStatus() == OrderStatus.SHIPPED
                || order.getStatus() == OrderStatus.DELIVERED) {

            throw new IllegalArgumentException(
                    "Order cannot be cancelled at this stage"
            );
        }

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);
    }

    private OrderDto mapToDto(Order order) {

        OrderDto dto = new OrderDto();

        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());

        dto.setShippingAddressLine1(
                order.getShippingAddressLine1()
        );
        dto.setShippingAddressLine2(
                order.getShippingAddressLine2()
        );
        dto.setShippingCity(order.getShippingCity());
        dto.setShippingState(order.getShippingState());
        dto.setShippingPostalCode(
                order.getShippingPostalCode()
        );
        dto.setShippingCountry(
                order.getShippingCountry()
        );

        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        return dto;
    }
}