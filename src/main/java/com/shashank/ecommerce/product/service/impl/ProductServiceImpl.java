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
import com.shashank.ecommerce.product.specification.ProductSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public List<ProductDto> getProductsByCategoryAndPrice(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        return productRepository
                .findByCategoryIdAndPriceBetween(
                        categoryId,
                        minPrice,
                        maxPrice
                )
                .stream()
                .map(this::mapToDto)
                .toList();
    }
    @Override
    public Page<ProductDto> filterProducts(
            String name,
            Long categoryId,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String sortBy,
            String sortDir,
            int page,
            int size) {

        Specification<Product> specification = null;

        if (name != null && !name.isBlank()) {
            specification = ProductSpecification.hasName(name);
        }

        if (brand != null && !brand.isBlank()) {
            Specification<Product> brandSpec =
                    ProductSpecification.hasBrand(brand);

            specification = specification == null
                    ? brandSpec
                    : specification.and(brandSpec);
        }

        if (categoryId != null) {
            Specification<Product> categorySpec =
                    ProductSpecification.hasCategory(categoryId);

            specification = specification == null
                    ? categorySpec
                    : specification.and(categorySpec);
        }

        if (minPrice != null) {
            Specification<Product> minPriceSpec =
                    ProductSpecification.hasMinPrice(minPrice);

            specification = specification == null
                    ? minPriceSpec
                    : specification.and(minPriceSpec);
        }

        if (maxPrice != null) {
            Specification<Product> maxPriceSpec =
                    ProductSpecification.hasMaxPrice(maxPrice);

            specification = specification == null
                    ? maxPriceSpec
                    : specification.and(maxPriceSpec);
        }



        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> products = specification == null
                ? productRepository.findAll(pageable)
                : productRepository.findAll(specification, pageable);

        return products.map(this::mapToDto);
    }

    @Override
    public List<ProductDto> searchProducts(String name) {

        return productRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<ProductDto> getProductsByMinPrice(BigDecimal minPrice) {

        return productRepository
                .findByPriceGreaterThanEqual(minPrice)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<ProductDto> getProductsByMaxPrice(BigDecimal maxPrice) {

        return productRepository
                .findByPriceLessThanEqual(maxPrice)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<ProductDto> getProductsByPriceRange(
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        return productRepository
                .findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<ProductDto> getProductsByCategory(Long categoryId) {

        return productRepository
                .findByCategoryId(categoryId)
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