package com.shashank.ecommerce.notification.dto;

import com.shashank.ecommerce.notification.enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDto {

    private Long id;

    private Long userId;

    private Long orderId;

    private NotificationType type;

    private String message;

    private boolean read;

    private LocalDateTime createdAt;
}