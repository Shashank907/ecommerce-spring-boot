package com.shashank.ecommerce.inventory.service;

import com.shashank.ecommerce.inventory.dto.InventoryDto;
import com.shashank.ecommerce.inventory.dto.UpdateInventoryRequestDto;

public interface InventoryService {

    InventoryDto getInventoryByProductId(Long productId);

    InventoryDto updateInventory(
            Long productId,
            UpdateInventoryRequestDto request
    );

    boolean checkStock(Long productId, Integer quantity);

    void reserveStock(Long productId, Integer quantity);

    void releaseStock(Long productId, Integer quantity);


    void consumeStock(Long productId, Integer quantity);

    void restoreStock(Long productId, Integer quantity);
}