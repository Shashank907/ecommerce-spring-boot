package com.shashank.ecommerce.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UpdateInventoryRequestDto {

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity cannot be negative")
    private Integer quantity;

    @NotNull(message = "Reserved quantity is required")
    @PositiveOrZero(message = "Reserved quantity cannot be negative")
    private Integer reservedQuantity;
}