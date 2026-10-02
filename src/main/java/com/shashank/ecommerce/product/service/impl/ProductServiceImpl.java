package com.shashank.ecommerce.product.service.impl;

import com.shashank.ecommerce.category.entity.Category;
import com.shashank.ecommerce.category.repository.CategoryRepository;
import com.shashank.ecommerce.exception.ResourceNotFoundException;
import com.shashank.ecommerce.inventory.entity.Inventory;
import com.shashank.ecommerce.inventory.repository.InventoryRepository;
import com.shashank.ecommerce.product.dto.AddProductRequestDto;
import com.shashank.ecommerce.product.dto.ProductDto;
import com.shashank.ecommerce.product.entity.Product;
import com.shashank.ecommerce.product.repository.ProductRepository;
import com.shashank.ecommerce.product.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ProductDto createProduct(AddProductRequestDto request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: "
                                        + request.getCategoryId()
                        ));

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setBrand(request.getBrand());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        Inventory inventory = new Inventory();
        inventory.setProduct(savedProduct);
        inventory.setQuantity(request.getStockQuantity());
        inventory.setReservedQuantity(0);

        inventoryRepository.save(inventory);

        return mapToDto(savedProduct);
    }

    @Override
    public List<ProductDto> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public ProductDto getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id
                        ));

        return mapToDto(product);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(
            Long id,
            AddProductRequestDto request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id
                        ));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: "
                                        + request.getCategoryId()
                        ));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setBrand(request.getBrand());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        Inventory inventory = inventoryRepository
                .findByProductId(product.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory not found for product id: "
                                        + product.getId()
                        ));

        inventory.setQuantity(request.getStockQuantity());

        productRepository.save(product);
        inventoryRepository.save(inventory);

        return mapToDto(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id
                        ));

        Inventory inventory = inventoryRepository
                .findByProductId(product.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory not found for product id: "
                                        + product.getId()
                        ));

        inventoryRepository.delete(inventory);
        productRepository.delete(product);
    }

    private ProductDto mapToDto(Product product) {

        ProductDto dto = modelMapper.map(product, ProductDto.class);

        dto.setCategoryId(product.getCategory().getId());

        Inventory inventory = inventoryRepository
                .findByProductId(product.getId())
                .orElse(null);

        if (inventory != null) {
            dto.setStockQuantity(inventory.getQuantity());
            dto.setReservedQuantity(inventory.getReservedQuantity());
            dto.setAvailableQuantity(
                    inventory.getQuantity()
                            - inventory.getReservedQuantity()
            );
        }

        return dto;
    }
}