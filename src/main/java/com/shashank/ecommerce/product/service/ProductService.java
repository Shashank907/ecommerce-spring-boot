package com.shashank.ecommerce.product.service;

import com.shashank.ecommerce.product.dto.AddProductRequestDto;
import com.shashank.ecommerce.product.dto.ProductDto;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductDto createProduct(AddProductRequestDto request);

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long id);

    ProductDto updateProduct(Long id, AddProductRequestDto request);

    void deleteProduct(Long id);

    List<ProductDto> getProductsByMinPrice(BigDecimal minPrice);

    List<ProductDto> getProductsByMaxPrice(BigDecimal maxPrice);

    List<ProductDto> getProductsByPriceRange(
            BigDecimal minPrice,
            BigDecimal maxPrice
    );

    List<ProductDto> getProductsByCategoryAndPrice(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice
    );

    List<ProductDto> searchProducts(String name);

    List<ProductDto> getProductsByCategory(Long categoryId);

    Page<ProductDto> filterProducts(
            String name,
            Long categoryId,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String sortBy,
            String sortDir,
            int page,
            int size
    );

}
