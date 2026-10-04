package com.shashank.ecommerce.notification.service;

import com.shashank.ecommerce.notification.dto.NotificationDto;
import com.shashank.ecommerce.notification.enums.NotificationType;

import java.util.List;

public interface NotificationService {

    List<NotificationDto> getUserNotifications(Long userId);

    void createNotification(
            Long userId,
            Long orderId,
            NotificationType type,
            String message
    );

    void markAsRead(Long userId, Long notificationId);

    void markAllAsRead(Long userId);
}