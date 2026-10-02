package com.shashank.ecommerce.product.controller;

import com.shashank.ecommerce.product.dto.AddProductRequestDto;
import com.shashank.ecommerce.product.dto.ProductDto;
import com.shashank.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody @Valid AddProductRequestDto request) {

        ProductDto createdProduct = productService.createProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProduct);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {

        List<ProductDto> products = productService.getAllProducts();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(
            @PathVariable Long id) {

        ProductDto product = productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid AddProductRequestDto request) {

        ProductDto updatedProduct =
                productService.updateProduct(id, request);

        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}