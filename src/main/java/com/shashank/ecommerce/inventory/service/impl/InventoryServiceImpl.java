package com.shashank.ecommerce.inventory.service.impl;

import com.shashank.ecommerce.inventory.dto.InventoryDto;
import com.shashank.ecommerce.inventory.dto.UpdateInventoryRequestDto;
import com.shashank.ecommerce.inventory.entity.Inventory;
import com.shashank.ecommerce.inventory.repository.InventoryRepository;
import com.shashank.ecommerce.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public InventoryDto getInventoryByProductId(Long productId) {

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found for product: " + productId));

        return mapToDto(inventory);
    }

    @Override
    public InventoryDto updateInventory(
            Long productId,
            UpdateInventoryRequestDto request) {

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found for product: " + productId));

        if (request.getReservedQuantity() > request.getQuantity()) {
            throw new RuntimeException(
                    "Reserved quantity cannot be greater than total quantity"
            );
        }

        inventory.setQuantity(request.getQuantity());
        inventory.setReservedQuantity(request.getReservedQuantity());

        Inventory savedInventory = inventoryRepository.save(inventory);

        return mapToDto(savedInventory);
    }

    @Override
    public boolean checkStock(Long productId, Integer quantity) {

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found for product: " + productId));

        int availableQuantity =
                inventory.getQuantity() - inventory.getReservedQuantity();

        return availableQuantity >= quantity;
    }

    @Override
    public void reserveStock(Long productId, Integer quantity) {

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found for product: " + productId));

        int availableQuantity =
                inventory.getQuantity() - inventory.getReservedQuantity();

        if (availableQuantity < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() + quantity
        );

        inventoryRepository.save(inventory);
    }

    @Override
    public void releaseStock(Long productId, Integer quantity) {

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found for product: " + productId));

        int newReservedQuantity =
                inventory.getReservedQuantity() - quantity;

        if (newReservedQuantity < 0) {
            throw new RuntimeException("Invalid reserved quantity");
        }

        inventory.setReservedQuantity(newReservedQuantity);

        inventoryRepository.save(inventory);
    }

    @Override
    public void consumeStock(Long productId, Integer quantity) {

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Inventory not found for product: " + productId
                        ));

        if (inventory.getReservedQuantity() < quantity) {
            throw new RuntimeException(
                    "Insufficient reserved stock for product: " + productId
            );
        }

        inventory.setQuantity(
                inventory.getQuantity() - quantity
        );

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() - quantity
        );

        inventoryRepository.save(inventory);
    }

    @Override
    public void restoreStock(Long productId, Integer quantity) {

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Inventory not found for product: " + productId
                        ));

        inventory.setQuantity(
                inventory.getQuantity() + quantity
        );

        inventoryRepository.save(inventory);
    }

    private InventoryDto mapToDto(Inventory inventory) {

        int availableQuantity =
                inventory.getQuantity() - inventory.getReservedQuantity();

        return new InventoryDto(
                inventory.getId(),
                inventory.getProduct().getId(),
                inventory.getQuantity(),
                inventory.getReservedQuantity(),
                availableQuantity
        );
    }
}