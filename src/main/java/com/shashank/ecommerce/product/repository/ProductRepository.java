package com.shashank.ecommerce.product.repository;

import com.shashank.ecommerce.product.dto.ProductDto;
import com.shashank.ecommerce.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByPriceGreaterThanEqual(BigDecimal minPrice);

    List<Product> findByPriceLessThanEqual(BigDecimal maxPrice);

    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByCategoryIdAndPriceBetween(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice
    );

    List<Product> findByBrandIgnoreCase(String brand);


}