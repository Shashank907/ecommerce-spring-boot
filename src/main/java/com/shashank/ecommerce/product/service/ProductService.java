package com.shashank.ecommerce.product.service;

import com.shashank.ecommerce.product.dto.AddProductRequestDto;
import com.shashank.ecommerce.product.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(AddProductRequestDto request);

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long id);

    ProductDto updateProduct(Long id, AddProductRequestDto request);

    void deleteProduct(Long id);
}
