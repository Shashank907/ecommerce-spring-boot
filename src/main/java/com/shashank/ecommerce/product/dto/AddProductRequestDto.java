package com.shashank.ecommerce.product.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AddProductRequestDto {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @Size(max = 100, message = "Brand cannot exceed 100 characters")
    private String brand;

    private String imageUrl;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}
