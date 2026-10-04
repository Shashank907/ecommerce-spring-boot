package com.shashank.ecommerce.notification.service.impl;

import com.shashank.ecommerce.exception.ResourceNotFoundException;
import com.shashank.ecommerce.notification.dto.NotificationDto;
import com.shashank.ecommerce.notification.entity.Notification;
import com.shashank.ecommerce.notification.enums.NotificationType;
import com.shashank.ecommerce.notification.repository.NotificationRepository;
import com.shashank.ecommerce.notification.service.NotificationService;
import com.shashank.ecommerce.order.entity.Order;
import com.shashank.ecommerce.order.repository.OrderRepository;
import com.shashank.ecommerce.user.entity.User;
import com.shashank.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getUserNotifications(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public void createNotification(
            Long userId,
            Long orderId,
            NotificationType type,
            String message) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + orderId));

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setOrder(order);
        notification.setType(type);
        notification.setMessage(message);

        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAsRead(Long userId, Long notificationId) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with id: " + notificationId));

        if (!notification.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException(
                    "Notification does not belong to this user");
        }

        notification.setRead(true);

        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        List<Notification> notifications =
                notificationRepository
                        .findByUserIdOrderByCreatedAtDesc(userId);

        notifications.forEach(notification -> notification.setRead(true));

        notificationRepository.saveAll(notifications);
    }

    private NotificationDto mapToDto(Notification notification) {

        NotificationDto dto = new NotificationDto();

        dto.setId(notification.getId());
        dto.setUserId(notification.getUser().getId());

        if (notification.getOrder() != null) {
            dto.setOrderId(notification.getOrder().getId());
        }

        dto.setType(notification.getType());
        dto.setMessage(notification.getMessage());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());

        return dto;
    }
}